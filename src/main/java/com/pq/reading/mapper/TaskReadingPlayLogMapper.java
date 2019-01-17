package com.pq.reading.mapper;

import com.pq.reading.entity.TaskReadingPlayLog;

import java.util.List;

public interface TaskReadingPlayLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskReadingPlayLog record);

    TaskReadingPlayLog selectByPrimaryKey(Long id);

    List<TaskReadingPlayLog> selectAll();

    int updateByPrimaryKey(TaskReadingPlayLog record);
}