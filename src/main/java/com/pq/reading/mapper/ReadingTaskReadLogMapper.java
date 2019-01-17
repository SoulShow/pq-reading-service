package com.pq.reading.mapper;

import com.pq.reading.entity.ReadingTaskReadLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReadingTaskReadLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReadingTaskReadLog record);

    ReadingTaskReadLog selectByPrimaryKey(Long id);

    List<ReadingTaskReadLog> selectAll();

    int updateByPrimaryKey(ReadingTaskReadLog record);

    Integer selectCountByTaskId(@Param("taskId")Long taskId);

    Integer selectCountByUserIdAndStudentId(@Param("userId")String userId,
                                            @Param("studentId")Long studentId);
}