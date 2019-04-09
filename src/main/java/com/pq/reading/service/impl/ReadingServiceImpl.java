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
import com.pq.reading.service.ReadingService;
import com.pq.reading.utils.Constants;
import com.pq.reading.utils.ReadingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author liutao
 */
@Service
public class ReadingServiceImpl implements ReadingService {
    @Autowired
    private BookAlbumMapper bookAlbumMapper;
    @Autowired
    private BookChapterMapper bookChapterMapper;
    @Autowired
    private ReadingBookMapper readingBookMapper;
    @Autowired
    private ReadingTaskMapper readingTaskMapper;
    @Autowired
    private ReadingTaskReadLogMapper taskReadLogMapper;
    @Autowired
    private TaskReadingPlayLogMapper readingPlayLogMapper;
    @Autowired
    private StudentTaskReadingRecordMapper readingRecordMapper;
    @Autowired
    private TeacherReadingReadLogMapper teacherReadingReadLogMapper;

    @Autowired
    private AgencyFeign agencyFeign;
    @Autowired
    private UserFeign userFeign;
    @Value("${php.url}")
    private String phpUrl;

    @Override
    public List<BookAlbum> getBookAlbumList(int type, int offset, int size) {
        return bookAlbumMapper.selectByType(type, offset, size);
    }

    @Override
    public List<ReadingBook> getBookList(Long albumId, int offset, int size) {
        return readingBookMapper.selectByAlbumId(albumId, offset, size);
    }


    @Override
    public List<BookChapter> getBookChapterList(Long bookId, int offset, int size) {
        List<BookChapter> list = bookChapterMapper.selectByBookId(bookId, offset, size);
        for (BookChapter bookChapter : list) {
            ReadingBook readingBook = readingBookMapper.selectByPrimaryKey(bookChapter.getBookId());
            BookAlbum bookAlbum = bookAlbumMapper.selectByPrimaryKey(readingBook.getAlbumId());
            bookChapter.setBookName(bookAlbum.getName() + "·" + readingBook.getName());
            String[] articleUrlList = bookChapter.getArticleUrl().split("\\;");
            bookChapter.setArticleUrlList(Arrays.asList(articleUrlList));
            bookChapter.setType(bookAlbumMapper.selectByPrimaryKey(readingBook.getAlbumId()).getType());
        }
        return list;
    }

    @Override
    public void createReadingTask(CreateReadingTaskDto readingTaskDto) {
        for (BookChapterDto bookChapterDto : readingTaskDto.getChapterList()) {
            for (Long classId : readingTaskDto.getClassIdList()) {
                ReadingTask readingTask = new ReadingTask();
                readingTask.setChapterId(bookChapterDto.getChapterId());

                BookChapter bookChapter = bookChapterMapper.selectByPrimaryKey(bookChapterDto.getChapterId());
                readingTask.setName(bookChapter.getChapter() + "：" + bookChapter.getTitle());
                ReadingBook readingBook = readingBookMapper.selectByPrimaryKey(bookChapter.getBookId());
                BookAlbum bookAlbum = bookAlbumMapper.selectByPrimaryKey(readingBook.getAlbumId());
                readingTask.setBookName(bookAlbum.getName() + "·" + readingBook.getName());

                readingTask.setUserId(readingTaskDto.getUserId());
                readingTask.setClassId(classId);
                readingTask.setType(Constants.READING_TASK_TYPE_NORMAL);
                readingTask.setState(true);
                readingTask.setCreatedTime(DateUtil.currentTime());
                readingTask.setUpdatedTime(DateUtil.currentTime());
                readingTaskMapper.insert(readingTask);

                ReadingResult<List<Long>> result = agencyFeign.getStudentInfoList(classId);
                if (!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())) {
                    throw new ReadingException(new ReadingErrorCode(result.getStatus(), result.getMessage()));
                }
                ReadingResult<UserDto> userResult = userFeign.getUserInfo(readingTaskDto.getUserId());
                if (!CommonErrors.SUCCESS.getErrorCode().equals(userResult.getStatus())) {
                    throw new ReadingException(new ReadingErrorCode(userResult.getStatus(), userResult.getMessage()));
                }

                UserDto userDto = userResult.getData();
                for (Long studentId : result.getData()) {

                    ReadingResult<AgencyStudentDto> studentInfo = agencyFeign.getStudentInfo(studentId);
                    if(!CommonErrors.SUCCESS.getErrorCode().equals(studentInfo.getStatus())){
                        throw new ReadingException(new ReadingErrorCode(studentInfo.getStatus(),studentInfo.getMessage()));
                    }
                    for(ParentDto parentDto:studentInfo.getData().getParentList()) {
                        HashMap<String, Object> paramMap = new HashMap<>();
                        paramMap.put("parameterId", Constants.PUSH_TEMPLATE_ID_NOTICE_2);
                        paramMap.put("user",parentDto.getHuanxinId() );
                        paramMap.put("form", userDto.getHuanxinId());
                        paramMap.put("teacherName", userDto.getName());
                        paramMap.put("title", "八点阅读");

                        StudentNoticeDto studentNoticeDto = new StudentNoticeDto();
                        studentNoticeDto.setWorkId(readingTask.getId());
                        studentNoticeDto.setStudent_id(studentId);
                        studentNoticeDto.setStudent_name(studentInfo.getData().getName());
                        paramMap.put("studentName", studentInfo.getData().getName());
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
            }
        }
    }

    @Override
    public List<NewReadingDto> getTeacherNewReadingList(Long classId, String userId, int offset, int size) {
        List<ReadingTask> taskList = readingTaskMapper.selectByClassIdAndUserId(classId,userId,offset, size);
        List<NewReadingDto> list = new ArrayList<>();
        for (ReadingTask readingTask : taskList) {
            NewReadingDto newReadingDto = new NewReadingDto();
            newReadingDto.setChapterId(readingTask.getChapterId());

            ReadingTaskReadLog taskReadLog = taskReadLogMapper.selectByUserIdAndTaskId(userId, readingTask.getId());

            newReadingDto.setReadingState(taskReadLog == null ? 0 : 1);
            int unCommitCount = 0;
            List<StudentTaskReadingRecord> readingRecordList = readingRecordMapper.selectByTaskId(readingTask.getId());
            for(StudentTaskReadingRecord readingRecord:readingRecordList){
                TeacherReadingReadLog readLog = teacherReadingReadLogMapper.selectByUserIdAndReadingId(userId,readingRecord.getId());
                if(readLog==null){
                    unCommitCount = unCommitCount+1;
                }
            }
            newReadingDto.setUnCommitCount(unCommitCount);
            newReadingDto.setTaskId(readingTask.getId());
            newReadingDto.setName(readingTask.getName());
            ReadingResult<AgencyClassDto> result = agencyFeign.getAgencyClassInfo(classId);
            if (!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())) {
                throw new ReadingException(new ReadingErrorCode(result.getStatus(), result.getMessage()));
            }
            newReadingDto.setClassName(result.getData().getName());
            newReadingDto.setClassId(result.getData().getId());
            newReadingDto.setBookName(readingTask.getBookName());
            newReadingDto.setCreateTime(DateUtil.formatDate(readingTask.getCreatedTime(), DateUtil.DEFAULT_TIME_MINUTE));
            list.add(newReadingDto);
        }
        return list;
    }

    @Override
    public List<NewReadingDto> getStudentNewReadingList(Long studentId, String userId, Long classId, int offset, int size) {

        List<ReadingTask> taskList = readingTaskMapper.selectByClassIdAndUserId(classId,null, offset, size);
        List<NewReadingDto> list = new ArrayList<>();
        for (ReadingTask readingTask : taskList) {
            NewReadingDto newReadingDto = new NewReadingDto();
            newReadingDto.setTaskId(readingTask.getId());
            newReadingDto.setName(readingTask.getName());
            newReadingDto.setBookName(readingTask.getBookName());
            newReadingDto.setCreateTime(DateUtil.formatDate(readingTask.getCreatedTime(), DateUtil.DEFAULT_TIME_MINUTE));
            newReadingDto.setChapterId(readingTask.getChapterId());
            BookChapter bookChapter = bookChapterMapper.selectByPrimaryKey(readingTask.getChapterId());
            newReadingDto.setWithPinyin(bookChapter.getWithPinyin());
            ReadingResult<AgencyClassDto> result = agencyFeign.getAgencyClassInfo(classId);
            if (!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())) {
                throw new ReadingException(new ReadingErrorCode(result.getStatus(), result.getMessage()));
            }
            newReadingDto.setClassName(result.getData().getName());
            newReadingDto.setCreateTime(DateUtil.formatDate(readingTask.getCreatedTime(), DateUtil.DEFAULT_TIME_MINUTE));

            ReadingResult<UserDto> userResult = userFeign.getUserInfo(readingTask.getUserId());
            if (!CommonErrors.SUCCESS.getErrorCode().equals(userResult.getStatus())) {
                throw new ReadingException(new ReadingErrorCode(result.getStatus(), result.getMessage()));
            }
            newReadingDto.setUserName(userResult.getData().getName());
            newReadingDto.setAvatar(userResult.getData().getAvatar());

            Integer readCount = taskReadLogMapper.selectCountByTaskIdAndStudentId(readingTask.getId(), studentId);
            if (readCount == null || readCount == 0) {
                newReadingDto.setReadingState(0);
            } else {
                newReadingDto.setReadingState(1);
            }
            list.add(newReadingDto);
        }
        return list;
    }

    @Override
    public int getUnReadCount(Long classId, String userId, Long studentId) {
        Integer taskCount = 0;
        if(studentId!=null&& studentId>0){
            taskCount = readingTaskMapper.selectCountByClassIdAndUserId(classId,null);
        }else {
            taskCount = readingTaskMapper.selectCountByClassIdAndUserId(classId,userId);
        }

        if (taskCount == null) {
            taskCount = 0;
        }
        Integer readCount = taskReadLogMapper.selectCountByStudentId(studentId);
        if (readCount == null) {
            readCount = 0;
        }
        return taskCount - readCount;
    }

    @Override
    public BookChapterDetailDto getReadingTaskDetail(Long taskId, Long studentId, String userId) {
        ReadingTask readingTask = readingTaskMapper.selectByPrimaryKey(taskId);
        if (readingTask == null) {
            ReadingException.raise(ReadingErrors.READING_TASK_IS_NOT_EXIST);
        }
        BookChapter bookChapter = bookChapterMapper.selectByPrimaryKey(readingTask.getChapterId());

        BookAlbum bookAlbum = bookAlbumMapper.selectByPrimaryKey(readingBookMapper.selectByPrimaryKey(bookChapter.getBookId()).getAlbumId());

        BookChapterDetailDto bookChapterDetailDto = new BookChapterDetailDto();
        bookChapterDetailDto.setId(bookChapter.getId());
        bookChapterDetailDto.setChapterId(bookChapter.getId());
        bookChapterDetailDto.setAuthor(bookChapter.getAuthor());
        bookChapterDetailDto.setName(readingTask.getName());
        bookChapterDetailDto.setBookName(readingTask.getBookName());
        bookChapterDetailDto.setArticleUrl(bookChapter.getArticleUrl());
        bookChapterDetailDto.setVoiceUrl(bookChapter.getVoiceUrl());
        bookChapterDetailDto.setArticleUrlList(Arrays.asList(bookChapter.getArticleUrl().split("\\;")));
        bookChapterDetailDto.setType(bookAlbum.getType());
        bookChapterDetailDto.setReadCount(bookChapter.getReadCount());
        StudentTaskReadingRecord readingRecord = readingRecordMapper.selectByTaskIdAndStudentId(taskId, studentId);
        if (readingRecord != null) {
            bookChapterDetailDto.setReadingId(readingRecord.getId());
            bookChapterDetailDto.setBase64(readingRecord.getBase64());
            bookChapterDetailDto.setSuffix(readingRecord.getSuffix());
        }
        bookChapterDetailDto.setCreateTime(DateUtil.formatDate(readingTask.getCreatedTime(),DateUtil.DEFAULT_TIME_MINUTE));

        ReadingResult<UserDto> userResult = userFeign.getUserInfo(readingTask.getUserId());
        if (!CommonErrors.SUCCESS.getErrorCode().equals(userResult.getStatus())) {
            throw new ReadingException(new ReadingErrorCode(userResult.getStatus(), userResult.getMessage()));
        }
        bookChapterDetailDto.setTeacherName(userResult.getData().getName());

        ReadingResult<AgencyClassDto> result = agencyFeign.getAgencyClassInfo(readingTask.getClassId());
        if (!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())) {
            throw new ReadingException(new ReadingErrorCode(result.getStatus(), result.getMessage()));
        }
        bookChapterDetailDto.setClassName(result.getData().getName());

        return bookChapterDetailDto;
    }

    @Override
    public ChapterSearchListDto searchChapter(String name) {
        ChapterSearchListDto chapterSearchListDto = new ChapterSearchListDto();
        List<BookChapter> chineseList = bookChapterMapper.selectByChapterNameAndType(name, Constants.READING_ALBUM_TYPE_CHINESE);
        chapterSearchListDto.setChineseList(getDetail(chineseList));

        List<BookChapter> readingList = bookChapterMapper.selectByChapterNameAndType(name, Constants.READING_ALBUM_TYPE_OUTSIDE_READING);
        chapterSearchListDto.setReadingList(getDetail(readingList));

        List<BookChapter> englishList = bookChapterMapper.selectByChapterNameAndType(name, Constants.READING_ALBUM_TYPE_ENGLISH);
        chapterSearchListDto.setEnglishList(getDetail(englishList));

        return chapterSearchListDto;
    }


    private List<BookChapterDetailDto> getDetail(List<BookChapter> list) {
        List<BookChapterDetailDto> detailDtoList = new ArrayList<>();
        for (BookChapter bookChapter : list) {
            BookChapterDetailDto bookChapterDetailDto = new BookChapterDetailDto();
            bookChapterDetailDto.setId(bookChapter.getId());

            bookChapterDetailDto.setName(bookChapter.getChapter() + "：" + bookChapter.getTitle());
            ReadingBook readingBook = readingBookMapper.selectByPrimaryKey(bookChapter.getBookId());
            BookAlbum bookAlbum = bookAlbumMapper.selectByPrimaryKey(readingBook.getAlbumId());
            bookChapterDetailDto.setBookName(bookAlbum.getName() + "·" + readingBook.getName());
            bookChapterDetailDto.setArticleUrlList(Arrays.asList(bookChapter.getArticleUrl().split("\\;")));
            bookChapterDetailDto.setType(bookAlbum.getType());
            bookChapterDetailDto.setArticleUrl(bookChapter.getArticleUrl());
            bookChapterDetailDto.setVoiceUrl(bookChapter.getVoiceUrl());
            bookChapterDetailDto.setReadCount(bookChapter.getReadCount());
            bookChapterDetailDto.setChapterId(bookChapter.getId());
            bookChapterDetailDto.setWithPinyin(bookChapter.getWithPinyin());
            detailDtoList.add(bookChapterDetailDto);
        }
        return detailDtoList;
    }

    @Override
    public void chapterOrRecordPlay(TaskReadingPlayLogDto taskReadingPlayLogDto) {
        TaskReadingPlayLog taskReadingPlayLog = new TaskReadingPlayLog();
        taskReadingPlayLog.setReadingRecordId(taskReadingPlayLogDto.getReadingRecordId() == null ?
                0 : taskReadingPlayLogDto.getReadingRecordId());
        taskReadingPlayLog.setChapterId(taskReadingPlayLogDto.getChapterId()==null?0:taskReadingPlayLogDto.getChapterId());
        taskReadingPlayLog.setUserId(taskReadingPlayLogDto.getUserId());
        taskReadingPlayLog.setStudentId(taskReadingPlayLogDto.getStudentId() == null ?
                0 : taskReadingPlayLogDto.getStudentId());
        taskReadingPlayLog.setCreatedTime(DateUtil.currentTime());
        readingPlayLogMapper.insert(taskReadingPlayLog);

        if (taskReadingPlayLog.getReadingRecordId() == 0) {
            BookChapter bookChapter = bookChapterMapper.selectByPrimaryKey(taskReadingPlayLogDto.getChapterId());
            bookChapter.setReadCount(bookChapter.getReadCount() + 1);
            bookChapterMapper.updateByPrimaryKey(bookChapter);
        } else {
            StudentTaskReadingRecord readingRecord = readingRecordMapper.selectByPrimaryKey(taskReadingPlayLogDto.getReadingRecordId());
            readingRecord.setPlayCount(readingRecord.getPlayCount() + 1);
            readingRecordMapper.updateByPrimaryKey(readingRecord);
        }
    }
    @Override
    public RankingListDto getRankingList(int type,Long studentId,Long classId,int role){
        Date beginDate = null;
        Date endDate = null;
        //月排行
        if(type==1){
            beginDate =DateUtil.getBeginDayOfMonth();
            endDate = DateUtil.getLastDayOfMonth();
        }
        //周排行
        if(type==2){
            beginDate = DateUtil.getBeginDayOfWeek();
            endDate = DateUtil.getLastDayOfWeek();
        }

        RankingListDto rankingListDto = new RankingListDto();
        if(role==CommonConstants.PQ_LOGIN_ROLE_PARENT){
            StudentInfoDto studentInfoDto = new StudentInfoDto();
            RankingDto monthRankingDto = readingPlayLogMapper.selectStudentReadingCountAndIndex(1,studentId,DateUtil.getBeginDayOfMonth(),DateUtil.getLastDayOfMonth());
            RankingDto weekRankingDto = readingPlayLogMapper.selectStudentReadingCountAndIndex(2,studentId,DateUtil.getBeginDayOfWeek(),DateUtil.getLastDayOfWeek());

            studentInfoDto.setWeekCount(weekRankingDto==null?0:weekRankingDto.getCount());
            studentInfoDto.setWeekIndex(weekRankingDto==null?0:weekRankingDto.getRankIndex());
            studentInfoDto.setMonthCount(monthRankingDto==null?0:monthRankingDto.getCount());
            studentInfoDto.setMonthIndex(monthRankingDto==null?0:monthRankingDto.getRankIndex());

            ReadingResult<AgencyStudentDto> studentInfo = agencyFeign.getStudentInfo(studentId);
            if(!CommonErrors.SUCCESS.getErrorCode().equals(studentInfo.getStatus())){
                throw new ReadingException(new ReadingErrorCode(studentInfo.getStatus(),studentInfo.getMessage()));
            }

            studentInfoDto.setStudentId(studentId);
            studentInfoDto.setStudentName(studentInfo.getData().getName());
            studentInfoDto.setAvatar(studentInfo.getData().getAvatar());
            studentInfoDto.setClassName(studentInfo.getData().getClassName());
            rankingListDto.setStudentInfo(studentInfoDto);
        }
        ReadingResult<List<Long>> result = agencyFeign.getClassIds(classId);
        if (!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())) {
            throw new ReadingException(new ReadingErrorCode(result.getStatus(), result.getMessage()));
        }

        List<RankingDto> list = readingPlayLogMapper.selectReadingCount(type,beginDate,endDate,result.getData());
        for(RankingDto rankingDto:list){
            ReadingResult<AgencyStudentDto> studentInfo = agencyFeign.getStudentInfo(rankingDto.getStudentId());
            if(!CommonErrors.SUCCESS.getErrorCode().equals(studentInfo.getStatus())){
                throw new ReadingException(new ReadingErrorCode(studentInfo.getStatus(),studentInfo.getMessage()));
            }
            rankingDto.setStudentName(studentInfo.getData().getName());
            rankingDto.setAvatar(studentInfo.getData().getAvatar());
            rankingDto.setClassName(studentInfo.getData().getClassName());
            rankingDto.setClassId(studentInfo.getData().getClassId());
        }
        rankingListDto.setList(list);
        return rankingListDto;
    }

}
