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

    Integer selectCountByUserAlbumId(@Param("albumId")Long albumId,@Param("studentId")Long studentId,
                                     @Param("type")int type);

    List<StudentTaskReadingRecord> selectByUserAlbumIdAndPrivate(@Param("albumId")Long albumId,
                                                                 @Param("studentId")Long studentId,
                                                                 @Param("isPrivate")int isPrivate,
                                                                 @Param("offset")int offset, @Param("size")int size);

    List<StudentTaskReadingRecord> selectPrivateByUserIdAndStudentId(@Param("userId")String userId,
                                                              @Param("studentId")Long studentId);

    StudentTaskReadingRecord selectByTaskIdAndStudentId(@Param("taskId")Long taskId,
                                                        @Param("studentId")Long studentId);

    List<StudentTaskReadingRecord> selectByChapterIdAndStudentList(@Param("chapterId")Long chapterId,@Param("studentList")List<Long> studentList,
                                                                    @Param("offset")int offset, @Param("size")int size);

    List<StudentTaskReadingRecord> selectByTeacherUserIdAndStudentId(@Param("userId")String userId,@Param("studentList")List<Long> studentList,
                                                                   @Param("offset")int offset, @Param("size")int size);

    List<StudentTaskReadingRecord> selectByTaskId(@Param("taskId")Long taskId);

    List<StudentTaskReadingRecord> selectByTeacherId(@Param("teacherId")String teacherId);

    Integer selectCountByTaskId(@Param("taskId")Long taskId);


    Integer selectCountByClassIdAndTeacherId(@Param("classId")Long classId,@Param("userId")String userId);


    List<StudentTaskReadingRecord> selectByUserAlbumId(@Param("albumId")Long albumId);
}