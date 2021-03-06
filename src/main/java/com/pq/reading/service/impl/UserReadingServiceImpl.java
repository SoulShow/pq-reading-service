package com.pq.reading.service.impl;

import com.alibaba.fastjson.JSON;
import com.pq.common.constants.CommonConstants;
import com.pq.common.exception.CommonErrors;
import com.pq.common.util.DateUtil;
import com.pq.common.util.HttpUtil;
import com.pq.reading.dto.*;
import com.pq.reading.entity.*;
import com.pq.reading.exception.ReadingErrorCode;
import com.pq.reading.exception.ReadingErrors;
import com.pq.reading.exception.ReadingException;
import com.pq.reading.feign.AgencyFeign;
import com.pq.reading.feign.UserFeign;
import com.pq.reading.mapper.*;
import com.pq.reading.service.UserReadingService;
import com.pq.reading.utils.Constants;
import com.pq.reading.utils.ReadingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author liutao
 */
@Service
public class UserReadingServiceImpl implements UserReadingService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserReadingServiceImpl.class);

    @Autowired
    private BookUserAlbumMapper userAlbumMapper;
    @Autowired
    private StudentTaskReadingRecordMapper readingRecordMapper;
    @Autowired
    private BookChapterMapper bookChapterMapper;
    @Autowired
    private ReadingTaskMapper readingTaskMapper;
    @Autowired
    private ReadingTaskReadLogMapper taskReadLogMapper;
    @Autowired
    private ReadingBookMapper readingBookMapper;
    @Autowired
    private BookAlbumMapper bookAlbumMapper;
    @Autowired
    private StudentReadingCommentMapper readingCommentMapper;
    @Autowired
    private StudentReadingPraiseMapper praiseMapper;
    @Autowired
    private TeacherReadingReadLogMapper teacherReadingReadLogMapper;
    @Autowired
    private TaskReadingPlayLogMapper readingPlayLogMapper;
    @Autowired
    private AgencyFeign agencyFeign;
    @Autowired
    private UserFeign userFeign;
    @Value("${php.url}")
    private String phpUrl;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Long createUserAlbum(UserAlbumDto userAlbumDto){
        BookUserAlbum bookUserAlbum = new BookUserAlbum();
        bookUserAlbum.setName(userAlbumDto.getName());
        bookUserAlbum.setImg(userAlbumDto.getImg());
        bookUserAlbum.setUserId(userAlbumDto.getUserId());
        bookUserAlbum.setStudentId(userAlbumDto.getStudentId());
        bookUserAlbum.setState(true);
        bookUserAlbum.setType(2);
        bookUserAlbum.setCreatedTime(DateUtil.currentTime());
        bookUserAlbum.setUpdatedTime(DateUtil.currentTime());
        userAlbumMapper.insert(bookUserAlbum);
        return bookUserAlbum.getId();
    }
    @Override
    public List<UserAlbumListDto> getUserAlbumList(Long originatorStudentId,String userId, Long studentId){
        //type==1 同一个人  type==2 不同一个人
        int type = 1;
        if(originatorStudentId==null||!studentId.equals(originatorStudentId)){
            type=2;
        }

        List<BookUserAlbum> list = userAlbumMapper.selectValidAlbum(studentId);
        List<UserAlbumListDto> albumListDtoList = new ArrayList<>();
        for(BookUserAlbum bookUserAlbum: list){
            UserAlbumListDto userAlbumListDto = new UserAlbumListDto();
            userAlbumListDto.setId(bookUserAlbum.getId());
            userAlbumListDto.setName(bookUserAlbum.getName());
            userAlbumListDto.setImg(bookUserAlbum.getImg());
            userAlbumListDto.setType(bookUserAlbum.getType());
            Integer count = readingRecordMapper.selectCountByUserAlbumId(bookUserAlbum.getId(),studentId,type);
            userAlbumListDto.setCount(count==null?0:count);
            albumListDtoList.add(userAlbumListDto);
        }
        return albumListDtoList;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long uploadUserReading(UserReadingRecordDto userReadingRecordDto){
        StudentTaskReadingRecord studentTaskReadingRecord = new StudentTaskReadingRecord();
        studentTaskReadingRecord.setTaskId(userReadingRecordDto.getTaskId()==null?0:userReadingRecordDto.getTaskId());
        studentTaskReadingRecord.setUserAlbumIId(userReadingRecordDto.getUserAlbumId());
        studentTaskReadingRecord.setStudentId(userReadingRecordDto.getStudentId());
        studentTaskReadingRecord.setUserId(userReadingRecordDto.getUserId());
        studentTaskReadingRecord.setVoiceUrl(userReadingRecordDto.getVoiceUrl());
        studentTaskReadingRecord.setImgUrl(userReadingRecordDto.getImg());
        studentTaskReadingRecord.setTeacherId(userReadingRecordDto.getOneToOneUserId());
        studentTaskReadingRecord.setScore(0);
        studentTaskReadingRecord.setPlayCount(0);
        studentTaskReadingRecord.setChapterId(userReadingRecordDto.getChapterId());
        studentTaskReadingRecord.setIsPrivate(userReadingRecordDto.getIsPrivate());
        studentTaskReadingRecord.setState(true);
        studentTaskReadingRecord.setCreatedTime(DateUtil.currentTime());
        studentTaskReadingRecord.setUpdatedTime(DateUtil.currentTime());
        studentTaskReadingRecord.setDuration(userReadingRecordDto.getDuration());
        studentTaskReadingRecord.setClassId(userReadingRecordDto.getClassId());
        studentTaskReadingRecord.setBase64(userReadingRecordDto.getBase64());
        studentTaskReadingRecord.setSuffix(userReadingRecordDto.getSuffix());
        BookChapter bookChapter = bookChapterMapper.selectByPrimaryKey(userReadingRecordDto.getChapterId());
        studentTaskReadingRecord.setName(bookChapter.getChapter()+"："+bookChapter.getTitle());
        ReadingBook readingBook = readingBookMapper.selectByPrimaryKey(bookChapter.getBookId());
        BookAlbum bookAlbum = bookAlbumMapper.selectByPrimaryKey(readingBook.getAlbumId());
        studentTaskReadingRecord.setBookName(bookAlbum.getName()+"·"+readingBook.getName());
        readingRecordMapper.insert(studentTaskReadingRecord);

        bookChapter.setReadCount(bookChapter.getReadCount()+1);
        bookChapterMapper.updateByPrimaryKey(bookChapter);

        ReadingTask readingTask = readingTaskMapper.selectByPrimaryKey(userReadingRecordDto.getTaskId());
        if(readingTask!=null){
           Integer count = taskReadLogMapper.selectCountByTaskIdAndStudentId(readingTask.getId(),
                    userReadingRecordDto.getStudentId());
            if(count==null||count==0){
                ReadingTaskReadLog taskReadLog= new ReadingTaskReadLog();
                taskReadLog.setTaskId(readingTask.getId());
                taskReadLog.setStudentId(userReadingRecordDto.getStudentId());
                taskReadLog.setClassId(userReadingRecordDto.getClassId());
                taskReadLog.setUserId(userReadingRecordDto.getUserId());
                taskReadLog.setState(true);
                taskReadLog.setCreatedTime(DateUtil.currentTime());
                taskReadLog.setUpdatedTime(DateUtil.currentTime());
                taskReadLogMapper.insert(taskReadLog);
            }
        }
        return studentTaskReadingRecord.getId();
    }

    @Override
    public UserAlbumListDto getAlbumDetail(Long albumId){
        BookUserAlbum bookUserAlbum = userAlbumMapper.selectByPrimaryKey(albumId);
        UserAlbumListDto userAlbumListDto = new UserAlbumListDto();
        userAlbumListDto.setId(bookUserAlbum.getId());
        userAlbumListDto.setName(bookUserAlbum.getName());
        userAlbumListDto.setImg(bookUserAlbum.getImg());
        userAlbumListDto.setType(bookUserAlbum.getType());
        return userAlbumListDto;
    }

    @Override
    public Long updateUserAlbum(UserAlbumDto userAlbumDto){
        BookUserAlbum bookUserAlbum = userAlbumMapper.selectByPrimaryKey(userAlbumDto.getId());
        bookUserAlbum.setName(userAlbumDto.getName());
        bookUserAlbum.setImg(userAlbumDto.getImg());
        bookUserAlbum.setUpdatedTime(DateUtil.currentTime());
        userAlbumMapper.updateByPrimaryKey(bookUserAlbum);
        return bookUserAlbum.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delUserAlbum(Long userAlbumId){
        BookUserAlbum bookUserAlbum = userAlbumMapper.selectByPrimaryKey(userAlbumId);
        bookUserAlbum.setState(false);
        bookUserAlbum.setUpdatedTime(DateUtil.currentTime());
        userAlbumMapper.updateByPrimaryKey(bookUserAlbum);
        List<StudentTaskReadingRecord> list = readingRecordMapper.selectByUserAlbumId(userAlbumId);
        for(StudentTaskReadingRecord readingRecord:list){
            delUserReading(readingRecord.getId(),readingRecord.getStudentId());
        }
    }

    @Override
    public List<UserAlbumReadingDto> getUserAlbumReadingList(Long userAlbumId,Long studentId,int isPrivate,int offset,int size){
        List<StudentTaskReadingRecord> list = readingRecordMapper.selectByUserAlbumIdAndPrivate(userAlbumId, studentId,isPrivate,offset,size);
        return getReadingDto(list);
    }
    @Override
    public List<UserAlbumReadingDto> getUserPrivateReadingList(String userId,Long studentId){
        List<StudentTaskReadingRecord> list = readingRecordMapper.selectPrivateByUserIdAndStudentId(userId,studentId);
        return getReadingDto(list);
    }
    private List<UserAlbumReadingDto> getReadingDto(List<StudentTaskReadingRecord> list){
        List<UserAlbumReadingDto> readingDtoList = new ArrayList<>();
        for(StudentTaskReadingRecord readingRecord:list ){
            UserAlbumReadingDto userAlbumReadingDto = new UserAlbumReadingDto();
            userAlbumReadingDto.setId(readingRecord.getId());
            userAlbumReadingDto.setName(readingRecord.getName());
            userAlbumReadingDto.setBookName(readingRecord.getBookName());
            userAlbumReadingDto.setImg(readingRecord.getImgUrl());
            userAlbumReadingDto.setVoiceUrl(readingRecord.getVoiceUrl());
            userAlbumReadingDto.setCreateTime(DateUtil.formatDate(readingRecord.getCreatedTime(),
                    DateUtil.DEFAULT_TIME_MINUTE));
            userAlbumReadingDto.setChapterId(readingRecord.getChapterId());
            BookChapter bookChapter = bookChapterMapper.selectByPrimaryKey(readingRecord.getChapterId());
            userAlbumReadingDto.setWithPinyin(bookChapter.getWithPinyin());
            userAlbumReadingDto.setAuthor(bookChapter.getAuthor());
            userAlbumReadingDto.setArticleUrl(bookChapter.getArticleUrl());
            readingDtoList.add(userAlbumReadingDto);
        }
        return readingDtoList;
    }
    @Override
    public MyReadingDto getUserReading(Long studentId, String userId){
        ReadingResult<AgencyStudentDto> result = agencyFeign.getStudentInfo(studentId);

        if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
            throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
        }
        MyReadingDto myReadingDto = new MyReadingDto();
        myReadingDto.setAvatar(result.getData().getAvatar());
        myReadingDto.setUserName(result.getData().getName());
        myReadingDto.setClassName(result.getData().getClassName());
        Integer count = readingCommentMapper.selectUnReadCountByStudentIdAndUserId(studentId);
        myReadingDto.setMessageCount(count==null?0:count);
        return myReadingDto;
    }

    @Override
    public MyReadingDetailDto getUserReadingDetail(String userId, Long studentId,Long readingId,Long commentId,
                                                   String praiseUserId,Long praiseStudentId,int role){

        if(commentId!=null && commentId!=0){
            StudentReadingComment readingComment = readingCommentMapper.selectByPrimaryKey(commentId);
            if(readingComment.getIsRead()==0){
                readingComment.setIsRead(1);
                readingComment.setUpdatedTime(DateUtil.currentTime());
                readingCommentMapper.updateByPrimaryKey(readingComment);
            }
        }

        ReadingResult<AgencyStudentDto> result = agencyFeign.getStudentInfo(studentId);
        if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
            throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
        }
        StudentTaskReadingRecord readingRecord = readingRecordMapper.selectByPrimaryKey(readingId);
        if(readingRecord.getState()==false){
            ReadingException.raise(ReadingErrors.READING_RECORD_IS_NOT_EXIST);
        }
        MyReadingDetailDto myReadingDetailDto = new MyReadingDetailDto();
        myReadingDetailDto.setReadingId(readingId);
        myReadingDetailDto.setName(readingRecord.getName());
        myReadingDetailDto.setBookName(readingRecord.getBookName());
        myReadingDetailDto.setImg(readingRecord.getImgUrl());
        myReadingDetailDto.setVoiceUrl(readingRecord.getVoiceUrl());
        myReadingDetailDto.setCreateTime(DateUtil.formatDate(readingRecord.getCreatedTime(),DateUtil.DEFAULT_TIME_MINUTE));
        myReadingDetailDto.setDuration(readingRecord.getDuration());

        myReadingDetailDto.setAvatar(result.getData().getAvatar());
        myReadingDetailDto.setUserName(result.getData().getName());
        myReadingDetailDto.setClassName(result.getData().getClassName());
        myReadingDetailDto.setPlayCount(readingRecord.getPlayCount());

        Integer praiseCount = praiseMapper.selectCountByReadingId(readingId);
        myReadingDetailDto.setPraiseCount(praiseCount==null?0:praiseCount);

        Integer commentCount = readingCommentMapper.selectCountByReadingId(readingId);
        myReadingDetailDto.setCommentCount(commentCount==null?0:commentCount);

        StudentReadingPraise readingPraise = praiseMapper.selectByReadingIdAndUserInfo(readingId,praiseUserId,praiseStudentId);
        myReadingDetailDto.setIsPraise(readingPraise==null?0:1);
        myReadingDetailDto.setBase64(readingRecord.getBase64());
        myReadingDetailDto.setSuffix(readingRecord.getSuffix());
        if(role==CommonConstants.PQ_LOGIN_ROLE_TEACHER){
            TeacherReadingReadLog teacherReadingReadLog = teacherReadingReadLogMapper.selectByUserIdAndReadingId(userId,readingId);
            if(teacherReadingReadLog==null){
                teacherReadingReadLog = new TeacherReadingReadLog();
                teacherReadingReadLog.setUserId(userId);
                teacherReadingReadLog.setReadingRecordId(readingId);
                teacherReadingReadLog.setClassId(readingRecord.getClassId());
                teacherReadingReadLog.setCreatedTime(DateUtil.currentTime());
                teacherReadingReadLogMapper.insert(teacherReadingReadLog);
            }
        }
        return myReadingDetailDto;
    }

    @Override
    public List<StudentReadingCommentDto> getReadingCommentList(Long readingId,Long classId,int offset,int size){
        List<StudentReadingComment> commentList = readingCommentMapper.selectByReadingId(readingId,offset,size);

        List<StudentReadingCommentDto> list = new ArrayList<>();
        for(StudentReadingComment readingComment : commentList){
            StudentReadingCommentDto studentReadingCommentDto = new StudentReadingCommentDto();
            studentReadingCommentDto.setId(readingComment.getId());
            studentReadingCommentDto.setOriginatorUserId(readingComment.getOriginatorUserId());
            studentReadingCommentDto.setOriginatorStudentId(readingComment.getOriginatorStudentId());
            studentReadingCommentDto.setOriginatorName(readingComment.getOriginatorName());

            ReadingResult<UserDto> result = userFeign.getUserInfo(readingComment.getOriginatorUserId());
            if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
                throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
            }
            studentReadingCommentDto.setOriginatorAvatar(result.getData().getAvatar());

            ReadingResult<AgencyClassDto> classInfo = agencyFeign.getAgencyClassInfo(classId);
            if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
                throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
            }
            studentReadingCommentDto.setClassName(classInfo.getData().getName());

            studentReadingCommentDto.setReceiverUserId(readingComment.getReceiverUserId());
            studentReadingCommentDto.setReceiverStudentId(readingComment.getReceiverStudentId());
            studentReadingCommentDto.setReceiverName(readingComment.getReceiverName());
            studentReadingCommentDto.setContent(readingComment.getContent());
            studentReadingCommentDto.setCreatedTime(DateUtil.formatDate(readingComment.getCreatedTime(),DateUtil.DEFAULT_DATETIME_FORMAT));
            list.add(studentReadingCommentDto);
        }
        return list;
    }
    @Override
    public List<CommentMessageDto> getCommentMessageList(Long studentId,Long classId,int offset,int size){
        List<StudentReadingComment> commentList = readingCommentMapper.selectByStudentId(studentId,offset,size);

        List<CommentMessageDto> list = new ArrayList<>();
        for(StudentReadingComment readingComment : commentList){
            CommentMessageDto commentMessageDto = new CommentMessageDto();
            commentMessageDto.setId(readingComment.getId());
            commentMessageDto.setOriginatorUserId(readingComment.getOriginatorUserId());
            commentMessageDto.setOriginatorStudentId(readingComment.getOriginatorStudentId());
            commentMessageDto.setOriginatorName(readingComment.getOriginatorName());
            if(readingComment.getOriginatorStudentId()!=null&&readingComment.getOriginatorStudentId()!=0){
                ReadingResult<AgencyStudentDto> studentInfo = agencyFeign.getStudentInfo(readingComment.getOriginatorStudentId());
                if(!CommonErrors.SUCCESS.getErrorCode().equals(studentInfo.getStatus())){
                    throw new ReadingException(new ReadingErrorCode(studentInfo.getStatus(),studentInfo.getMessage()));
                }
                commentMessageDto.setOriginatorAvatar(studentInfo.getData().getAvatar());
                commentMessageDto.setClassName(studentInfo.getData().getClassName());
            }else {
                ReadingResult<UserDto> result = userFeign.getUserInfo(readingComment.getOriginatorUserId());
                if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
                    throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
                }
                commentMessageDto.setOriginatorAvatar(result.getData().getAvatar());
                commentMessageDto.setOriginatorName(result.getData().getName());

                ReadingResult<AgencyClassDto> classInfo = agencyFeign.getAgencyClassInfo(classId);
                if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
                    throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
                }
                commentMessageDto.setClassName(classInfo.getData().getName());

            }
            commentMessageDto.setReceiverUserId(readingComment.getReceiverUserId());
            commentMessageDto.setReceiverStudentId(readingComment.getReceiverStudentId());
            commentMessageDto.setReceiverName(readingComment.getReceiverName());
            commentMessageDto.setContent(readingComment.getContent());
            commentMessageDto.setCreatedTime(DateUtil.formatDate(readingComment.getCreatedTime(),DateUtil.DEFAULT_TIME_MINUTE));
            commentMessageDto.setIsRead(readingComment.getIsRead());
            commentMessageDto.setType(readingComment.getType());
            StudentTaskReadingRecord readingRecord = readingRecordMapper.selectByPrimaryKey(readingComment.getReadingRecordId());
            if(readingRecord==null){
                ReadingException.raise(ReadingErrors.READING_RECORD_IS_NOT_EXIST);
            }
            commentMessageDto.setReadingImg(readingRecord.getImgUrl());
            commentMessageDto.setName(readingRecord.getName());
            commentMessageDto.setBookName(readingRecord.getBookName());
            commentMessageDto.setReadingId(readingRecord.getId());
            list.add(commentMessageDto);
        }
        return list;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void praise(PraiseDto praiseDto){
        StudentReadingPraise readingPraise = praiseMapper.selectByReadingIdAndUserInfo(praiseDto.getReadingId(),
                praiseDto.getUserId(),praiseDto.getStudentId());
        if(readingPraise!=null){
            ReadingException.raise(ReadingErrors.READING_PRAISE_IS_EXIST);
        }
        StudentReadingPraise praise = new StudentReadingPraise();
        praise.setReadingRecordId(praiseDto.getReadingId());
        praise.setUserId(praiseDto.getUserId());
        praise.setName(praiseDto.getName());
        praise.setStudentId(praiseDto.getStudentId());
        praise.setState(1);
        praise.setCreatedTime(DateUtil.currentTime());
        praise.setUpdatedTime(DateUtil.currentTime());
        praiseMapper.insert(praise);


        StudentReadingComment studentReadingComment = new StudentReadingComment();
        studentReadingComment.setReadingRecordId(praiseDto.getReadingId());
        studentReadingComment.setOriginatorUserId(praiseDto.getUserId());
        studentReadingComment.setOriginatorName(praiseDto.getName());
        studentReadingComment.setOriginatorStudentId(praiseDto.getStudentId());
        studentReadingComment.setContent("点赞");
        studentReadingComment.setIsRead(0);
        studentReadingComment.setState(1);
        studentReadingComment.setType(2);
        studentReadingComment.setCreatedTime(DateUtil.currentTime());
        studentReadingComment.setUpdatedTime(DateUtil.currentTime());
        readingCommentMapper.insert(studentReadingComment);
    }

    @Override
    public void praiseCancel(PraiseDto praiseDto){
        StudentReadingPraise readingPraise = praiseMapper.selectByReadingIdAndUserInfo(praiseDto.getReadingId(),
                praiseDto.getUserId(),praiseDto.getStudentId());
        if(praiseDto==null){
            ReadingException.raise(ReadingErrors.READING_PRAISE_IS_NOT_EXIST);
        }
        readingPraise.setState(0);
        readingPraise.setUpdatedTime(DateUtil.currentTime());
        praiseMapper.updateByPrimaryKey(readingPraise);
    }


    @Override
    public void createComment(CommentDto commentDto){
        StudentReadingComment studentReadingComment = new StudentReadingComment();
        studentReadingComment.setReadingRecordId(commentDto.getReadingId());
        studentReadingComment.setOriginatorUserId(commentDto.getOriginatorUserId());
        studentReadingComment.setOriginatorName(commentDto.getOriginatorName());
        studentReadingComment.setOriginatorStudentId(commentDto.getOriginatorStudentId());
        studentReadingComment.setReceiverName(commentDto.getReceiverName());
        studentReadingComment.setReceiverUserId(commentDto.getReceiverUserId());
        studentReadingComment.setReceiverStudentId(commentDto.getReceiverStudentId());
        studentReadingComment.setContent(commentDto.getContent());
        studentReadingComment.setIsRead(0);
        studentReadingComment.setState(1);
        studentReadingComment.setType(1);
        studentReadingComment.setCreatedTime(DateUtil.currentTime());
        studentReadingComment.setUpdatedTime(DateUtil.currentTime());
        readingCommentMapper.insert(studentReadingComment);
    }
    @Override
    public List<AgencyStudentDto> getReadingRankingList(Long chapterId,Long classId,int type,int offset,int size){
        List<AgencyStudentDto> studentDtos = new ArrayList<>();
        //阅读之星
        if(type==1){
            ReadingResult<List<Long>> result = agencyFeign.getAllStudentList(classId);
            if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
                throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
            }
            studentDtos = getAgencyStudentDtoList(chapterId,result.getData(),offset,size);
        }
        //同班同学
        if(type==2){
            ReadingResult<List<Long>> result = agencyFeign.getStudentInfoList(classId);
            if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
                throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
            }
            studentDtos = getAgencyStudentDtoList(chapterId,result.getData(),offset,size);
        }
        return studentDtos;
    }

    private List<AgencyStudentDto> getAgencyStudentDtoList(Long chapterId,List<Long> studentList,int offset,int size){
        List<AgencyStudentDto> studentDtos = new ArrayList<>();

        List<StudentTaskReadingRecord> list = readingRecordMapper.selectByChapterIdAndStudentList(chapterId,studentList,offset,size);


        for(StudentTaskReadingRecord readingRecord:list){
            ReadingResult<AgencyStudentDto> studentInfo = agencyFeign.getStudentInfo(readingRecord.getStudentId());
            if(!CommonErrors.SUCCESS.getErrorCode().equals(studentInfo.getStatus())){
                throw new ReadingException(new ReadingErrorCode(studentInfo.getStatus(),studentInfo.getMessage()));
            }
            AgencyStudentDto agencyStudentDto = new AgencyStudentDto();
            agencyStudentDto.setStudentId(studentInfo.getData().getStudentId());
            agencyStudentDto.setClassId(readingRecord.getClassId());
            agencyStudentDto.setAvatar(studentInfo.getData().getAvatar());
            agencyStudentDto.setClassName(studentInfo.getData().getClassName());
            agencyStudentDto.setName(studentInfo.getData().getName());
            agencyStudentDto.setReadingId(readingRecord.getId());
            studentDtos.add(agencyStudentDto);
        }
        return studentDtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delUserReading(Long readingId,Long studentId){
        StudentTaskReadingRecord readingRecord = readingRecordMapper.selectByPrimaryKey(readingId);
        if(readingRecord == null){
            ReadingException.raise(ReadingErrors.READING_RECORD_IS_NOT_EXIST);
        }
        if(!readingRecord.getStudentId().equals(studentId)){
            ReadingException.raise(ReadingErrors.READING_RECORD_STUDENT_NOT_MATCH);
        }
        readingRecord.setState(false);
        readingRecord.setUpdatedTime(DateUtil.currentTime());
        readingRecordMapper.updateByPrimaryKey(readingRecord);

        List<TaskReadingPlayLog> logList = readingPlayLogMapper.selectByReadingId(readingId);
        for(TaskReadingPlayLog readingPlayLog:logList){
            readingPlayLogMapper.deleteByPrimaryKey(readingPlayLog.getId());
        }
        ReadingTaskReadLog readingTaskReadLog = taskReadLogMapper.selectByUserIdAndStudentIdAndTaskId(readingRecord.getUserId(),
                readingRecord.getStudentId(),readingRecord.getTaskId());
        if(readingTaskReadLog !=null){
            //删除阅读任务阅读记录
            taskReadLogMapper.deleteByPrimaryKey(readingTaskReadLog.getId());
        }
    }
    @Override
    public List<NewReadingDto> getTeacherOnoToOneList(Long classId,String userId, int offset,int size){
        ReadingResult<List<Long>> result = agencyFeign.getStudentInfoList(classId);
        if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
            throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
        }
        if(result.getData()==null||result.getData().size()==0){
            return null;
        }
        List<StudentTaskReadingRecord> list = readingRecordMapper.selectByTeacherUserIdAndStudentId(userId,result.getData(),offset,size);

        return getReadingList(list,userId,classId);
    }

    @Override
    public NewReadingListDto getTeacherCommitList(String userId,Long classId, Long taskId){
        ReadingResult<List<Long>> result = agencyFeign.getStudentInfoList(classId);
        if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
            throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
        }
        List<StudentTaskReadingRecord> readingRecordList = readingRecordMapper.selectByTaskId(taskId);
        NewReadingListDto newReadingListDto = new NewReadingListDto();
        List<NewReadingDto> readingDtos = getReadingList(readingRecordList,userId,classId);
        newReadingListDto.setList(readingDtos);
        newReadingListDto.setCount(readingDtos.size());

        ReadingTaskReadLog readingTaskReadLog = taskReadLogMapper.selectByUserIdAndTaskId(userId,taskId);
        if(readingTaskReadLog==null){
            readingTaskReadLog = new ReadingTaskReadLog();
            readingTaskReadLog.setTaskId(taskId);
            readingTaskReadLog.setUserId(userId);
            readingTaskReadLog.setClassId(classId);
            readingTaskReadLog.setState(true);
            readingTaskReadLog.setCreatedTime(DateUtil.currentTime());
            readingTaskReadLog.setUpdatedTime(DateUtil.currentTime());
            readingTaskReadLog.setStudentId(0L);
            taskReadLogMapper.insert(readingTaskReadLog);
        }
        return newReadingListDto;
    }

    private List<NewReadingDto> getReadingList(List<StudentTaskReadingRecord> list, String userId,Long classId){
        List<NewReadingDto> readingDtos = new ArrayList<>();
        for(StudentTaskReadingRecord taskReadingRecord:list){
            NewReadingDto newReadingDto = new NewReadingDto();
            newReadingDto.setTaskId(taskReadingRecord.getTaskId());
            newReadingDto.setChapterId(taskReadingRecord.getChapterId());
            newReadingDto.setName(taskReadingRecord.getName());
            newReadingDto.setBookName(taskReadingRecord.getBookName());
            ReadingResult<AgencyStudentDto> studentInfo = agencyFeign.getStudentInfo(taskReadingRecord.getStudentId());
            if(!CommonErrors.SUCCESS.getErrorCode().equals(studentInfo.getStatus())){
                throw new ReadingException(new ReadingErrorCode(studentInfo.getStatus(),studentInfo.getMessage()));
            }
            newReadingDto.setClassName(studentInfo.getData().getClassName());
            newReadingDto.setUserName(studentInfo.getData().getName());
            newReadingDto.setAvatar(studentInfo.getData().getAvatar());
            newReadingDto.setCreateTime(DateUtil.formatDate(taskReadingRecord.getCreatedTime(),DateUtil.DEFAULT_TIME_MINUTE));
            newReadingDto.setStudentId(taskReadingRecord.getStudentId());
            newReadingDto.setReadingId(taskReadingRecord.getId());
            newReadingDto.setClassId(classId);
            TeacherReadingReadLog readingReadLog = teacherReadingReadLogMapper.selectByUserIdAndReadingId(userId,taskReadingRecord.getId());
            newReadingDto.setReadingState(readingReadLog==null?0:1);
            readingDtos.add(newReadingDto);
        }
        return readingDtos;
    }

    @Override
    public AgencyStudentListDto getTeacherUnCommitList(String userId, Long classId, Long taskId){
        ReadingResult<List<Long>> result = agencyFeign.getStudentInfoList(classId);
        if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
            throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
        }
        AgencyStudentListDto agencyStudentListDto = new AgencyStudentListDto();
        List<AgencyStudentDto> list = new ArrayList<>();
        for(Long studentId:result.getData()){
            StudentTaskReadingRecord readingRecord = readingRecordMapper.selectByTaskIdAndStudentId(taskId,studentId);
            if(readingRecord==null){
                ReadingResult<AgencyStudentDto> studentInfo = agencyFeign.getStudentInfo(studentId);
                if(!CommonErrors.SUCCESS.getErrorCode().equals(studentInfo.getStatus())){
                    throw new ReadingException(new ReadingErrorCode(studentInfo.getStatus(),studentInfo.getMessage()));
                }
                AgencyStudentDto agencyStudentDto = new AgencyStudentDto();
                agencyStudentDto.setStudentId(studentId);
                agencyStudentDto.setName(studentInfo.getData().getName());
                agencyStudentDto.setClassName(studentInfo.getData().getClassName());
                agencyStudentDto.setAvatar(studentInfo.getData().getAvatar());
                agencyStudentDto.setParentList(studentInfo.getData().getParentList());
                list.add(agencyStudentDto);
            }
        }
        agencyStudentListDto.setList(list);
        agencyStudentListDto.setCount(list.size());
        return agencyStudentListDto;
    }
    @Override
    public  void unCommitListPush(String userId,Long taskId,Long classId){
        if(redisTemplate.hasKey(Constants.READING_TASK_USER_INFO+userId+classId+taskId)){
            ReadingException.raise(ReadingErrors.READING_PUSH_MORE_60_ERROR);
        }
        AgencyStudentListDto studentListDto = getTeacherUnCommitList( userId, classId, taskId);
        List<AgencyStudentDto> list = studentListDto.getList();


        ReadingResult<UserDto> userResult = userFeign.getUserInfo(userId);
        if (!CommonErrors.SUCCESS.getErrorCode().equals(userResult.getStatus())) {
            throw new ReadingException(new ReadingErrorCode(userResult.getStatus(), userResult.getMessage()));
        }
        LOGGER.info("学生列表为：————————————"+JSON.toJSON(list));

        UserDto userDto = userResult.getData();
        for(AgencyStudentDto studentDto:list){
            LOGGER.info("家长列表为：————————————"+JSON.toJSON(studentDto.getParentList()));
            for(ParentDto parentDto:studentDto.getParentList()){
                LOGGER.info("家长环信id为：————————————"+parentDto.getHuanxinId());
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("parameterId", Constants.PUSH_TEMPLATE_ID_NOTICE_8);
                paramMap.put("user", parentDto.getHuanxinId());
                paramMap.put("form", userDto.getHuanxinId());
                paramMap.put("teacherName", userDto.getName());
                paramMap.put("title", "八点阅读");

                StudentNoticeDto studentNoticeDto = new StudentNoticeDto();
                studentNoticeDto.setWorkId(taskId);
                studentNoticeDto.setStudent_id(studentDto.getStudentId());
                studentNoticeDto.setStudent_name(studentDto.getName());
                paramMap.put("ext", studentNoticeDto);

                String huanxResult = null;
                try {
                    huanxResult = HttpUtil.sendJson(phpUrl + "push", new HashMap<>(), JSON.toJSONString(paramMap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ReadingResult pushResult = JSON.parseObject(huanxResult, ReadingResult.class);
                if (pushResult == null || !CommonErrors.SUCCESS.getErrorCode().equals(userResult.getStatus())) {
                    ReadingException.raise(ReadingErrors.READING_PUSH_ERROR);
                }
            }
        }
        redisTemplate.opsForValue().set(Constants.READING_TASK_USER_INFO+userId+classId+taskId,"阅读提醒",60,TimeUnit.MINUTES);
    }

    @Override
    public TeacherReadingIndexDto getIndexStatus(String userId){
        TeacherReadingIndexDto readingIndexDto = new TeacherReadingIndexDto();
        List<ReadingTask> taskList = readingTaskMapper.selectByUserId(userId);
        readingIndexDto.setReadingTaskStatus(0);
        for (ReadingTask readingTask : taskList) {
            if(readingIndexDto.getReadingTaskStatus()==0){
                List<StudentTaskReadingRecord> readingRecordList = readingRecordMapper.selectByTaskId(readingTask.getId());
                for(StudentTaskReadingRecord record :readingRecordList){
                    TeacherReadingReadLog readLog = teacherReadingReadLogMapper.selectByUserIdAndReadingId(userId,record.getId());
                    if(readLog==null){
                        readingIndexDto.setReadingTaskStatus(1);
                        break;
                    }
                }
            }else {
                break;
            }
        }
        List<StudentTaskReadingRecord> oneToOneList = readingRecordMapper.selectByTeacherId(userId);
        for(StudentTaskReadingRecord taskReadingRecord:oneToOneList) {
            TeacherReadingReadLog readingReadLog = teacherReadingReadLogMapper.selectByUserIdAndReadingId(userId,taskReadingRecord.getId());
            if(readingReadLog==null){
                readingIndexDto.setOneToOneStatus(1);
                break;
            }else {
                readingIndexDto.setOneToOneStatus(0);
            }

        }
        return readingIndexDto;
    }

    @Override
    public List<AgencyClassDto> getTeacherClassUnReadCount(String userId,int type){
        ReadingResult<List<AgencyClassDto>> result = agencyFeign.getTeacherClassList(userId);
        if (!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())) {
            throw new ReadingException(new ReadingErrorCode(result.getStatus(), result.getMessage()));
        }
        int count = 0;
        List<AgencyClassDto> classDtos = result.getData();
        for(AgencyClassDto agencyClassDto:classDtos){
            agencyClassDto.setStatus(0);
            //新阅读
            if(type==1){
                List<ReadingTask> list = readingTaskMapper.selectAllByClassIdAndUserId(agencyClassDto.getId(),userId);
                for(ReadingTask readingTask:list){
                    List<StudentTaskReadingRecord> readingRecordList = readingRecordMapper.selectByTaskId(readingTask.getId());
                    for(StudentTaskReadingRecord record :readingRecordList){
                        TeacherReadingReadLog readLog = teacherReadingReadLogMapper.selectByUserIdAndReadingId(userId,record.getId());
                        if(readLog==null){
                            agencyClassDto.setStatus(1);
                            break;
                        }
                    }
                }
            }
            //1对1
            if(type==2){
                List<StudentTaskReadingRecord> list = readingRecordMapper.selectCountByClassIdAndTeacherId(agencyClassDto.getId(),userId);
                for(StudentTaskReadingRecord record :list){
                    TeacherReadingReadLog readLog = teacherReadingReadLogMapper.selectByUserIdAndReadingId(userId,record.getId());
                    if(readLog==null){
                        agencyClassDto.setStatus(1);
                        break;
                    }
                }
            }
        }
        return classDtos;
    }



}
