package com.pq.reading.service.impl;

import com.pq.common.util.DateUtil;
import com.pq.reading.dto.UserAlbumDto;
import com.pq.reading.dto.UserAlbumListDto;
import com.pq.reading.dto.UserReadingRecordDto;
import com.pq.reading.entity.BookUserAlbum;
import com.pq.reading.entity.StudentTaskReadingRecord;
import com.pq.reading.mapper.BookUserAlbumMapper;
import com.pq.reading.mapper.StudentTaskReadingRecordMapper;
import com.pq.reading.service.UserReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            userAlbumListDto.setName(bookUserAlbum.getName());
            userAlbumListDto.setImg(bookUserAlbum.getImg());

            Integer count = readingRecordMapper.selectCountByUserAlbumId(bookUserAlbum.getId());
            userAlbumListDto.setCount(count==null?0:count);
            albumListDtoList.add(userAlbumListDto);
        }
        return albumListDtoList;
    }
    @Override
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
        studentTaskReadingRecord.setIsPrivate(userReadingRecordDto.getIsPrivate());
        studentTaskReadingRecord.setState(true);
        studentTaskReadingRecord.setCreatedTime(DateUtil.currentTime());
        studentTaskReadingRecord.setUpdatedTime(DateUtil.currentTime());
        readingRecordMapper.insert(studentTaskReadingRecord);
    }



}
