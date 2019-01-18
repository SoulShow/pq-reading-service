package com.pq.reading.mapper;

import com.pq.reading.entity.BookChapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookChapterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BookChapter record);

    BookChapter selectByPrimaryKey(Long id);

    List<BookChapter> selectAll();

    int updateByPrimaryKey(BookChapter record);

    List<BookChapter> selectByBookId(@Param("bookId") Long bookId, @Param("offset")int offset, @Param("size")int size);

    List<BookChapter> selectByChapterNameAndType(@Param("name") String name,@Param("type") int type);
}