package com.pq.reading.mapper;

import com.pq.reading.entity.BookAlbum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookAlbumMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BookAlbum record);

    BookAlbum selectByPrimaryKey(Long id);

    List<BookAlbum> selectAll();

    int updateByPrimaryKey(BookAlbum record);

    List<BookAlbum> selectByType(@Param("type") int type,@Param("offset")int offset,@Param("size")int size);
}