package com.pq.reading.service.impl;

import com.pq.common.exception.CommonErrors;
import com.pq.common.util.DateUtil;
import com.pq.reading.dto.*;
import com.pq.reading.entity.*;
import com.pq.reading.exception.ReadingErrorCode;
import com.pq.reading.exception.ReadingException;
import com.pq.reading.feign.AgencyFeign;
import com.pq.reading.feign.UserFeign;
import com.pq.reading.mapper.*;
import com.pq.reading.service.ReadingService;
import com.pq.reading.utils.Constants;
import com.pq.reading.utils.ReadingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private BookUserAlbumMapper userAlbumMapper;
    @Autowired
    private AgencyFeign agencyFeign;
    @Autowired
    private UserFeign userFeign;

    @Override
    public List<BookAlbum> getBookAlbumList(int type,int offset,int size){
        return bookAlbumMapper.selectByType(type,offset,size);
    }

    @Override
    public List<ReadingBook> getBookList(Long albumId, int offset, int size){
        return readingBookMapper.selectByAlbumId(albumId,offset,size);
    }


    @Override
    public List<BookChapter> getBookChapterList(Long bookId, int offset, int size){
        return bookChapterMapper.selectByBookId(bookId,offset,size);

    }

    @Override
    public void createReadingTask(CreateReadingTaskDto readingTaskDto){
        for(BookChapterDto bookChapterDto :readingTaskDto.getChapterList()){
            for(Long classId:readingTaskDto.getClassIdList()){
            ReadingTask readingTask = new ReadingTask();
            readingTask.setChapterId(bookChapterDto.getChapterId());
            readingTask.setName(bookChapterDto.getName());
            readingTask.setUserId(readingTaskDto.getUserId());
            readingTask.setClassId(classId);
            readingTask.setType(Constants.READING_TASK_TYPE_NORMAL);
            readingTask.setState(true);
            readingTask.setCreatedTime(DateUtil.currentTime());
            readingTask.setUpdatedTime(DateUtil.currentTime());
            readingTaskMapper.insert(readingTask);
            }
        }
   }
    @Override
    public List<NewReadingDto> getTeacherNewReadingList(Long classId, int offset, int size){
        List<ReadingTask> taskList = readingTaskMapper.selectByClassId(classId,offset,size);
        List<NewReadingDto> list = new ArrayList<>();
        for(ReadingTask readingTask:taskList){
            NewReadingDto newReadingDto = new NewReadingDto();

            Integer readCount = taskReadLogMapper.selectCountByTaskId(readingTask.getId());
            if(readCount==null){
                readCount = 0;
            }
            ReadingResult<Integer> result = agencyFeign.getStudentCount(classId);
            if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
                throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
            }
            Integer studentCount = result.getData();

            newReadingDto.setUnCommitCount(studentCount-readCount);
            newReadingDto.setTaskId(readingTask.getId());
            newReadingDto.setName(readingTask.getName());
            newReadingDto.setCreateTime(DateUtil.formatDate(readingTask.getCreatedTime(),DateUtil.DEFAULT_TIME_MINUTE));
            list.add(newReadingDto);
        }
        return list;
    }

    @Override
    public List<NewReadingDto> getStudentNewReadingList(Long studentId,String userId,Long classId, int offset, int size){

        List<ReadingTask> taskList = readingTaskMapper.selectByClassId(classId,offset,size);
        List<NewReadingDto> list = new ArrayList<>();
        for(ReadingTask readingTask:taskList){
            NewReadingDto newReadingDto = new NewReadingDto();
            newReadingDto.setTaskId(readingTask.getId());
            newReadingDto.setName(readingTask.getName());
            newReadingDto.setCreateTime(DateUtil.formatDate(readingTask.getCreatedTime(),DateUtil.DEFAULT_TIME_MINUTE));

            ReadingResult<AgencyClassDto> result = agencyFeign.getAgencyClassInfo(classId);
            if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
                throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
            }
            newReadingDto.setClassName(result.getData().getName());
            newReadingDto.setCreateTime(DateUtil.formatDate(readingTask.getCreatedTime(),DateUtil.DEFAULT_TIME_MINUTE));

            ReadingResult<UserDto> userResult = userFeign.getUserInfo(readingTask.getUserId());
            if(!CommonErrors.SUCCESS.getErrorCode().equals(userResult.getStatus())){
                throw new ReadingException(new ReadingErrorCode(result.getStatus(),result.getMessage()));
            }
            newReadingDto.setUserName(userResult.getData().getName());
            newReadingDto.setAvatar(userResult.getData().getAvatar());

            Integer readCount = taskReadLogMapper.selectCountByTaskIdAndStudentId(readingTask.getId(),studentId);
            if(readCount==null||readCount==0){
                newReadingDto.setReadingState(0);
            }else {
                newReadingDto.setReadingState(1);
            }

            list.add(newReadingDto);
        }
        return list;
    }

    @Override
    public int getUnReadCount(Long classId,String userId,Long studentId){
        Integer taskCount = readingTaskMapper.selectCountByClassId(classId);
        if(taskCount==null){
            taskCount=0;
        }
        Integer readCount = taskReadLogMapper.selectCountByUserIdAndStudentId(userId,studentId);
        if(readCount==null){
            readCount=0;
        }
        return taskCount-readCount;
    }

    @Override
    public BookChapter getReadingTaskDetail(Long taskId,Long studentId,String userId){
        ReadingTask readingTask = readingTaskMapper.selectByPrimaryKey(taskId);

        ReadingTaskReadLog taskReadLog = new ReadingTaskReadLog();
        taskReadLog.setTaskId(readingTask.getId());
        taskReadLog.setStudentId(studentId);
        taskReadLog.setUserId(userId);
        taskReadLog.setState(true);
        taskReadLog.setCreatedTime(DateUtil.currentTime());
        taskReadLog.setUpdatedTime(DateUtil.currentTime());
        taskReadLogMapper.insert(taskReadLog);

        return bookChapterMapper.selectByPrimaryKey(readingTask.getChapterId());
    }

}
