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

    List<ReadingTask> selectByClassIdAndUserId(@Param("classId") Long classId, @Param("userId") String userId,
                                               @Param("offset") int offset, @Param("size") int size);

    List<ReadingTask> selectByUserId(@Param("userId") String userId);

    Integer selectCountByClassIdAndUserId(@Param("classId") Long classId,
                                          @Param("userId") String userId);
}