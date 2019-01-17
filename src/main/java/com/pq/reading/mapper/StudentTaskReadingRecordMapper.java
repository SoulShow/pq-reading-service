package com.pq.reading.mapper;

import com.pq.reading.entity.StudentTaskReadingRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentTaskReadingRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StudentTaskReadingRecord record);

    StudentTaskReadingRecord selectByPrimaryKey(Long id);

    List<StudentTaskReadingRecord> selectAll();

    int updateByPrimaryKey(StudentTaskReadingRecord record);

    Integer selectCountByUserAlbumId(@Param("albumId")Long albumId);
}