package com.pq.reading.mapper;

import com.pq.reading.entity.ReadingTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReadingTaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReadingTask record);

    ReadingTask selectByPrimaryKey(Long id);

    List<ReadingTask> selectAll();

    int updateByPrimaryKey(ReadingTask record);

    List<ReadingTask> selectByClassId(@Param("classId") Long classId, @Param("offset") int offset,
                                      @Param("size") int size);

    List<ReadingTask> selectAllByClassId(@Param("classId") Long classId);

    Integer selectCountByClassId(@Param("classId") Long classId);
}