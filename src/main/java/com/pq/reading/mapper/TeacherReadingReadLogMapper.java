package com.pq.reading.mapper;

import com.pq.reading.entity.TeacherReadingReadLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherReadingReadLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TeacherReadingReadLog record);

    TeacherReadingReadLog selectByPrimaryKey(Long id);

    List<TeacherReadingReadLog> selectAll();

    int updateByPrimaryKey(TeacherReadingReadLog record);

    TeacherReadingReadLog selectByUserIdAndReadingId(@Param("userId")String userId,
                                                     @Param("readingId")Long readingId);

    Integer selectCountByClassIdAndTeacherId(@Param("classId")Long classId,@Param("userId")String userId);
}