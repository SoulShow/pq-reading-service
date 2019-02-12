package com.pq.reading.mapper;

import com.pq.reading.dto.RankingDto;
import com.pq.reading.entity.TaskReadingPlayLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TaskReadingPlayLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskReadingPlayLog record);

    TaskReadingPlayLog selectByPrimaryKey(Long id);

    List<TaskReadingPlayLog> selectAll();

    int updateByPrimaryKey(TaskReadingPlayLog record);

    List<RankingDto> selectReadingCount(@Param("type")int type,
                                        @Param("beginDate")Date beginDate,
                                        @Param("endDate")Date endDate);

    RankingDto selectStudentReadingCountAndIndex(@Param("type")int type,
                                                 @Param("studentId") Long studentId,
                                                 @Param("beginDate")Date beginDate,
                                                 @Param("endDate")Date endDate);

    List<TaskReadingPlayLog> selectByReadingId(@Param("readingId") Long readingId);

}