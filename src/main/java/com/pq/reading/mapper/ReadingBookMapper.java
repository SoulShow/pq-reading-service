package com.pq.reading.mapper;

import com.pq.reading.entity.ReadingBook;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReadingBookMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReadingBook record);

    ReadingBook selectByPrimaryKey(Long id);

    List<ReadingBook> selectAll();

    int updateByPrimaryKey(ReadingBook record);

    List<ReadingBook> selectByAlbumId(@Param("albumId") Long albumId, @Param("offset")int offset, @Param("size")int size);
}