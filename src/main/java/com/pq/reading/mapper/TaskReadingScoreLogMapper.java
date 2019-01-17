package com.pq.reading.mapper;

import com.pq.reading.entity.TaskReadingScoreLog;

import java.util.List;

public interface TaskReadingScoreLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskReadingScoreLog record);

    TaskReadingScoreLog selectByPrimaryKey(Long id);

    List<TaskReadingScoreLog> selectAll();

    int updateByPrimaryKey(TaskReadingScoreLog record);
}