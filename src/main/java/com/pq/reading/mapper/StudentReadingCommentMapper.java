package com.pq.reading.mapper;

import com.pq.reading.entity.StudentReadingComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentReadingCommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StudentReadingComment record);

    StudentReadingComment selectByPrimaryKey(Long id);

    List<StudentReadingComment> selectAll();

    int updateByPrimaryKey(StudentReadingComment record);

    Integer selectUnReadCountByStudentIdAndUserId(@Param("studentId") Long studentId, @Param("userId") String userId);

    Integer selectCountByReadingId(@Param("readingId")Long readingId);

    List<StudentReadingComment> selectByReadingId(@Param("readingId")Long readingId,
                                                  @Param("offset") int offset,
                                                  @Param("size") int size);

    List<StudentReadingComment> selectByStudentId(@Param("studentId")Long studentId,
                                                  @Param("offset") int offset,
                                                  @Param("size") int size);
}