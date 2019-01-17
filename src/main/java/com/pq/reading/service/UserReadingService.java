package com.pq.reading.service;


import com.pq.reading.dto.UserAlbumDto;
import com.pq.reading.dto.UserAlbumListDto;
import com.pq.reading.dto.UserReadingRecordDto;

import java.util.List;

/**
 * 学生阅读
 * @author liutao
 */
public interface UserReadingService {

    /**
     * 创建学生专辑
     * @param userAlbumDto
     */
    void createUserAlbum(UserAlbumDto userAlbumDto);

    /**
     * 获取专辑列表
     * @param userId
     * @param studentId
     * @return
     */
    List<UserAlbumListDto> getUserAlbumList(String userId,Long studentId);

    /**
     * 上传阅读
     * @param userReadingRecordDto
     */
    void uploadUserReading(UserReadingRecordDto userReadingRecordDto);


}
