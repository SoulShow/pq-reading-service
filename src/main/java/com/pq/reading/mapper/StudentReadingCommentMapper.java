package com.pq.reading.mapper;

import com.pq.reading.entity.StudentReadingComment;

import java.util.List;

public interface StudentReadingCommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StudentReadingComment record);

    StudentReadingComment selectByPrimaryKey(Long id);

    List<StudentReadingComment> selectAll();

    int updateByPrimaryKey(StudentReadingComment record);
}