package com.pq.reading.mapper;

import com.pq.reading.entity.StudentReadingPraise;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentReadingPraiseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StudentReadingPraise record);

    StudentReadingPraise selectByPrimaryKey(Long id);

    List<StudentReadingPraise> selectAll();

    int updateByPrimaryKey(StudentReadingPraise record);

    Integer selectCountByReadingId(@Param("readingId")Long readingId);

    StudentReadingPraise selectByReadingIdAndUserInfo(@Param("readingId")Long readingId,
                                                      @Param("praiseUserId")String praiseUserId,
                                                      @Param("praiseStudentId")Long praiseStudentId);
}