package com.pq.reading.mapper;

import com.pq.reading.entity.StudentReadingPraise;

import java.util.List;

public interface StudentReadingPraiseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StudentReadingPraise record);

    StudentReadingPraise selectByPrimaryKey(Long id);

    List<StudentReadingPraise> selectAll();

    int updateByPrimaryKey(StudentReadingPraise record);
}