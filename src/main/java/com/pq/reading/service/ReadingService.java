package com.pq.reading.service;


import com.pq.reading.dto.*;
import com.pq.reading.entity.BookAlbum;
import com.pq.reading.entity.BookChapter;
import com.pq.reading.entity.ReadingBook;

import java.util.List;

/**
 * 机构服务
 * @author liutao
 */
public interface ReadingService {


    /**
     * 获取专辑列表
     * @param type
     * @param offset
     * @param size
     * @return
     */
    List<BookAlbum> getBookAlbumList(int type,int offset,int size);

    /**
     * 获取书籍列表
     * @param albumId
     * @param offset
     * @param size
     * @return
     */
    List<ReadingBook> getBookList(Long albumId,int offset,int size);


    /**
     * 获取书籍章节列表
     * @param bookId
     * @param offset
     * @param size
     * @return
     */
    List<BookChapter> getBookChapterList(Long bookId,int offset,int size);


    /**
     * 创建阅读任务
     * @param readingTaskDto
     */
    void createReadingTask(CreateReadingTaskDto readingTaskDto);


    /**
     * 获取老师新阅读列表
     * @param classId
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    List<NewReadingDto> getTeacherNewReadingList(Long classId,String userId, int offset, int size);


    /**
     * 获取学生端新阅读列表
     * @param studentId
     * @param userId
     * @param classId
     * @param offset
     * @param size
     * @return
     */
    List<NewReadingDto> getStudentNewReadingList(Long studentId,String userId,Long classId, int offset, int size);


    /**
     * 获取未读数量
     * @param classId
     * @param userId
     * @param studentId
     * @return
     */
    int getUnReadCount(Long classId,String userId,Long studentId);

    /**
     * 获取任务章节详情
     * @param taskId
     * @param userId
     * @param studentId
     * @return
     */
    BookChapterDetailDto getReadingTaskDetail(Long taskId, Long studentId, String userId);


    /**
     * 获取章节列表
     * @param name
     * @return
     */
    ChapterSearchListDto searchChapter(String name);

    /**
     * 章节阅读/学生阅读播放
     * @param taskReadingPlayLogDto
     */
    void chapterOrRecordPlay(TaskReadingPlayLogDto taskReadingPlayLogDto);


    /**
     * 排行榜
     * @param type
     * @param studentId
     * @param role
     * @return
     */
    RankingListDto getRankingList(int type,Long studentId,int role);


}
