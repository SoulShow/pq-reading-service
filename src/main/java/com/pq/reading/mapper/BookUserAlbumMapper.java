package com.pq.reading.mapper;

import com.pq.reading.entity.BookUserAlbum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookUserAlbumMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BookUserAlbum record);

    BookUserAlbum selectByPrimaryKey(Long id);

    List<BookUserAlbum> selectAll();

    int updateByPrimaryKey(BookUserAlbum record);

    List<BookUserAlbum> selectValidAlbum(@Param("userId")String userId,@Param("studentId")Long studentId);
}