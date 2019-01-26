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

    Integer selectCountByTaskIdAndStudentId(@Param("taskId")Long taskId,@Param("studentId")Long studentId);

    Integer selectCountByUserIdAndStudentId(@Param("userId")String userId,
                                            @Param("studentId")Long studentId);

    ReadingTaskReadLog selectByUserIdAndTaskId(@Param("userId")String userId,
                                               @Param("taskId")Long taskId);
}