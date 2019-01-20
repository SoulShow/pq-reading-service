package com.pq.reading.mapper;

import com.pq.reading.entity.UserReadingBanner;

import java.util.List;

public interface UserReadingBannerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserReadingBanner record);

    UserReadingBanner selectByPrimaryKey(Long id);

    List<UserReadingBanner> selectAll();

    int updateByPrimaryKey(UserReadingBanner record);
}