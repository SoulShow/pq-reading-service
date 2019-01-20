package com.pq.reading.service.impl;

import com.pq.common.exception.CommonErrors;
import com.pq.common.util.DateUtil;
import com.pq.reading.dto.*;
import com.pq.reading.entity.*;
import com.pq.reading.exception.ReadingErrorCode;
import com.pq.reading.exception.ReadingErrors;
import com.pq.reading.exception.ReadingException;
import com.pq.reading.feign.AgencyFeign;
import com.pq.reading.mapper.*;
import com.pq.reading.service.UserReadingService;
import com.pq.reading.utils.ReadingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liutao
 */
@Service
public class UserReadingServiceImpl implements UserReadingService {
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
    private AgencyFeign agencyFeign;

    @Override
    public void createUserAlbum(UserAlbumDto userAlbumDto){
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
    }
    @Override
    public List<UserAlbumListDto> getUserAlbumList(String userId, Long studentId){
        List<BookUserAlbum> list = userAlbumMapper.selectValidAlbum(userId,studentId);
        List<UserAlbumListDto> albumListDtoList = new ArrayList<>();
        for(BookUserAlbum bookUserAlbum: list){
            UserAlbumListDto userAlbumListDto = new UserAlbumListDto();
            userAlbumListDto.setId(bookUserAlbum.getId());
            userAlbumListDto.setName(bookUserAlbum.getName());
            userAlbumListDto.setImg(bookUserAlbum.getImg());
            userAlbumListDto.setType(bookUserAlbum.getType());
            Integer count = readingRecordMapper.selectCountByUserAlbumId(bookUserAlbum.getId());
            userAlbumListDto.setCount(count==null?0:count);
            albumListDtoList.add(userAlbumListDto);
        }
        return albumListDtoList;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadUserReading(UserReadingRecordDto userReadingRecordDto){
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

        BookChapter bookChapter = bookChapterMapper.selectByPrimaryKey(userReadingRecordDto.getChapterId());
        studentTaskReadingRecord.setName(bookChapter.getChapter()+"："+bookChapter.getTitle());
        ReadingBook readingBook = readingBookMapper.selectByPrimaryKey(bookChapter.getBookId());
        BookAlbum bookAlbum = bookAlbumMapper.selectByPrimaryKey(readingBook.getAlbumId());
        studentTaskReadingRecord.setBookName(bookAlbum.getName()+"·"+readingBook.getName());
        readingRecordMapper.insert(studentTaskReadingRecord);

        bookChapter.setReadCount(bookChapter.getReadCount()+1);
        bookChapterMapper.updateByPrimaryKey(bookChapter);

        ReadingTask readingTask = readingTaskMapper.selectByPrimaryKey(userReadingRecordDto.getTaskId());

        ReadingTaskReadLog taskReadLog = new ReadingTaskReadLog();
        taskReadLog.setTaskId(readingTask.getId());
        taskReadLog.setStudentId(userReadingRecordDto.getStudentId());
        taskReadLog.setUserId(userReadingRecordDto.getUserId());
        taskReadLog.setState(true);
        taskReadLog.setCreatedTime(DateUtil.currentTime());
        taskReadLog.setUpdatedTime(DateUtil.currentTime());
        taskReadLogMapper.insert(taskReadLog);

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
    public void updateUserAlbum(UserAlbumDto userAlbumDto){
        BookUserAlbum bookUserAlbum = userAlbumMapper.selectByPrimaryKey(userAlbumDto.getId());
        bookUserAlbum.setName(userAlbumDto.getName());
        bookUserAlbum.setImg(userAlbumDto.getImg());
        bookUserAlbum.setUpdatedTime(DateUtil.currentTime());
        userAlbumMapper.updateByPrimaryKey(bookUserAlbum);
    }

    @Override
    public void delUserAlbum(Long userAlbumId){
        BookUserAlbum bookUserAlbum = userAlbumMapper.selectByPrimaryKey(userAlbumId);
        bookUserAlbum.setState(false);
        bookUserAlbum.setUpdatedTime(DateUtil.currentTime());
        userAlbumMapper.updateByPrimaryKey(bookUserAlbum);
    }

    @Override
    public List<UserAlbumReadingDto> getUserAlbumReadingList(Long userAlbumId,int isPrivate,int offset,int size){
        List<StudentTaskReadingRecord> list = readingRecordMapper.selectByUserAlbumIdAndPrivate(userAlbumId, isPrivate,offset,size);
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
        Integer count = readingCommentMapper.selectUnReadCountByStudentIdAndUserId(studentId,userId);
        myReadingDto.setMessageCount(count==null?0:count);
        return myReadingDto;
    }

    @Override
    public MyReadingDetailDto getUserReadingDetail(Long studentId,Long readingId,Long commentId){

        ReadingResult<AgencyStudentDto> result = agencyFeign.getStudentInfo(studentId);
        if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
            throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
        }
        MyReadingDetailDto myReadingDetailDto = new MyReadingDetailDto();
        myReadingDetailDto.setAvatar(result.getData().getAvatar());
        myReadingDetailDto.setUserName(result.getData().getName());
        myReadingDetailDto.setClassName(result.getData().getClassName());
        StudentTaskReadingRecord readingRecord = readingRecordMapper.selectByPrimaryKey(readingId);
        myReadingDetailDto.setPlayCount(readingRecord.getPlayCount());

        Integer praiseCount = praiseMapper.selectCountByReadingId(readingId);
        myReadingDetailDto.setPraiseCount(praiseCount==null?0:praiseCount);

        Integer commentCount = readingCommentMapper.selectCountByReadingId(readingId);
        myReadingDetailDto.setCommentCount(commentCount==null?0:commentCount);
        if(commentId!=null && commentId!=0){
            StudentReadingComment readingComment = readingCommentMapper.selectByPrimaryKey(commentId);
            if(readingComment.getIsRead()==0){
                readingComment.setIsRead(1);
                readingComment.setCreatedTime(DateUtil.currentTime());
                readingCommentMapper.updateByPrimaryKey(readingComment);
            }
        }
        return myReadingDetailDto;
    }

    @Override
    public List<StudentReadingCommentDto> getReadingCommentList(Long readingId,int offset,int size){
        List<StudentReadingComment> commentList = readingCommentMapper.selectByReadingId(readingId,offset,size);

        List<StudentReadingCommentDto> list = new ArrayList<>();
        for(StudentReadingComment readingComment : commentList){
            StudentReadingCommentDto studentReadingCommentDto = new StudentReadingCommentDto();
            studentReadingCommentDto.setId(readingComment.getId());
            studentReadingCommentDto.setOriginatorUserId(readingComment.getOriginatorUserId());
            studentReadingCommentDto.setOriginatorStudentId(readingComment.getOriginatorStudentId());
            studentReadingCommentDto.setOriginatorName(readingComment.getOriginatorName());

            ReadingResult<AgencyStudentDto> studentInfo = agencyFeign.getStudentInfo(readingComment.getOriginatorStudentId());
            if(!CommonErrors.SUCCESS.getErrorCode().equals(studentInfo.getStatus())){
                throw new ReadingException(new ReadingErrorCode(studentInfo.getStatus(),studentInfo.getMessage()));
            }
            studentReadingCommentDto.setOriginatorAvatar(studentInfo.getData().getAvatar());
            studentReadingCommentDto.setClassName(studentInfo.getData().getClassName());
            studentReadingCommentDto.setReceiverUserId(readingComment.getReceiverUserId());
            studentReadingCommentDto.setReceiverStudentId(readingComment.getReceiverStudentId());
            studentReadingCommentDto.setReceiverName(readingComment.getReceiverName());
            studentReadingCommentDto.setContent(readingComment.getContent());
            studentReadingCommentDto.setCreatedTime(DateUtil.formatDate(readingComment.getCreatedTime(),DateUtil.DEFAULT_TIME_MINUTE));
            list.add(studentReadingCommentDto);
        }
        return list;
    }
    @Override
    public List<CommentMessageDto> getCommentMessageList(Long studentId,int offset,int size){
        List<StudentReadingComment> commentList = readingCommentMapper.selectByStudentId(studentId,offset,size);

        List<CommentMessageDto> list = new ArrayList<>();
        for(StudentReadingComment readingComment : commentList){
            CommentMessageDto commentMessageDto = new CommentMessageDto();
            commentMessageDto.setId(readingComment.getId());
            commentMessageDto.setOriginatorUserId(readingComment.getOriginatorUserId());
            commentMessageDto.setOriginatorStudentId(readingComment.getOriginatorStudentId());
            commentMessageDto.setOriginatorName(readingComment.getOriginatorName());

            ReadingResult<AgencyStudentDto> studentInfo = agencyFeign.getStudentInfo(readingComment.getOriginatorStudentId());
            if(!CommonErrors.SUCCESS.getErrorCode().equals(studentInfo.getStatus())){
                throw new ReadingException(new ReadingErrorCode(studentInfo.getStatus(),studentInfo.getMessage()));
            }
            commentMessageDto.setOriginatorAvatar(studentInfo.getData().getAvatar());
            commentMessageDto.setClassName(studentInfo.getData().getClassName());
            commentMessageDto.setReceiverUserId(readingComment.getReceiverUserId());
            commentMessageDto.setReceiverStudentId(readingComment.getReceiverStudentId());
            commentMessageDto.setReceiverName(readingComment.getReceiverName());
            commentMessageDto.setContent(readingComment.getContent());
            commentMessageDto.setCreatedTime(DateUtil.formatDate(readingComment.getCreatedTime(),DateUtil.DEFAULT_TIME_MINUTE));
            commentMessageDto.setIsRead(readingComment.getIsRead());

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
    public void praise(PraiseDto praiseDto){
        StudentReadingPraise praise = new StudentReadingPraise();
        praise.setReadingRecordId(praiseDto.getReadingId());
        praise.setUserId(praiseDto.getUserId());
        praise.setName(praiseDto.getName());
        praise.setStudentId(praiseDto.getStudentId());
        praise.setState(1);
        praise.setCreatedTime(DateUtil.currentTime());
        praise.setUpdatedTime(DateUtil.currentTime());
        praiseMapper.insert(praise);
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
        studentReadingComment.setCreatedTime(DateUtil.currentTime());
        studentReadingComment.setUpdatedTime(DateUtil.currentTime());
        readingCommentMapper.insert(studentReadingComment);
    }


}
