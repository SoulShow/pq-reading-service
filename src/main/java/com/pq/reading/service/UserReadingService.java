package com.pq.reading.service;


import com.pq.reading.dto.UserAlbumDto;
import com.pq.reading.dto.UserAlbumListDto;
import com.pq.reading.dto.UserAlbumReadingDto;
import com.pq.reading.dto.UserReadingRecordDto;
import com.pq.reading.entity.BookAlbum;

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

    /**
     * 获取专辑详情
     * @param albumId
     * @return
     */
    UserAlbumListDto getAlbumDetail(Long albumId);

    /**
     * 更新个人专辑
     * @param userAlbumDto
     */
    void updateUserAlbum(UserAlbumDto userAlbumDto);

    /**
     * 删除个人专辑
     * @param userAlbumId
     */
    void delUserAlbum(Long userAlbumId);

    /**
     * 获取专辑阅读列表
     * @param userAlbumId
     * @return
     */
    List<UserAlbumReadingDto> getUserAlbumReadingList(Long userAlbumId);

}
