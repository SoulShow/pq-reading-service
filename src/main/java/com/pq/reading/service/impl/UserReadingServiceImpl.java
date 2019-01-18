package com.pq.reading.service.impl;

import com.pq.common.util.DateUtil;
import com.pq.reading.dto.UserAlbumDto;
import com.pq.reading.dto.UserAlbumListDto;
import com.pq.reading.dto.UserAlbumReadingDto;
import com.pq.reading.dto.UserReadingRecordDto;
import com.pq.reading.entity.*;
import com.pq.reading.mapper.*;
import com.pq.reading.service.UserReadingService;
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
    public List<UserAlbumReadingDto> getUserAlbumReadingList(Long userAlbumId){
        List<StudentTaskReadingRecord> list = readingRecordMapper.selectByUserAlbumId(userAlbumId);
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

}
