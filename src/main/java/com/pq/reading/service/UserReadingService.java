package com.pq.reading.service;


import com.pq.reading.dto.*;

import java.util.List;

/**
 * 学生阅读
 * @author liutao
 */
public interface UserReadingService {

    /**
     * 创建学生专辑
     * @param userAlbumDto
     */
    void createUserAlbum(UserAlbumDto userAlbumDto);

    /**
     * 获取专辑列表
     * @param userId
     * @param studentId
     * @return
     */
    List<UserAlbumListDto> getUserAlbumList(String userId,Long studentId);

    /**
     * 上传阅读
     * @param userReadingRecordDto
     * @return
     */
    Long uploadUserReading(UserReadingRecordDto userReadingRecordDto);

    /**
     * 获取专辑详情
     * @param albumId
     * @return
     */
    UserAlbumListDto getAlbumDetail(Long albumId);

    /**
     * 更新个人专辑
     * @param userAlbumDto
     */
    void updateUserAlbum(UserAlbumDto userAlbumDto);

    /**
     * 删除个人专辑
     * @param userAlbumId
     */
    void delUserAlbum(Long userAlbumId);

    /**
     * 获取专辑阅读列表
     * @param userAlbumId
     * @param isPrivate
     * @param offset
     * @param size
     * @return
     */
    List<UserAlbumReadingDto> getUserAlbumReadingList(Long userAlbumId,int isPrivate,int offset,int size);

    /**
     * 获取个人隐私专辑
     * @param userId
     * @param studentId
     * @return
     */
    List<UserAlbumReadingDto> getUserPrivateReadingList(String userId,Long studentId);


    /**
     * 我的阅读
     * @param studentId
     * @param userId
     * @return
     */
    MyReadingDto getUserReading(Long studentId,String userId);

    /**
     * 获取个人阅读详情
     * @param userId
     * @param studentId
     * @param readingId
     * @param commentId
     * @param praiseStudentId
     * @param praiseUserId
     * @param role
     * @return
     */
    MyReadingDetailDto getUserReadingDetail(String userId,Long studentId,Long readingId,Long commentId,
                                            String praiseUserId,Long praiseStudentId,int role);

    /**
     * 获取阅读详情评论列表
     * @param readingId
     * @param classId
     * @param offset
     * @param size
     * @return
     */
    List<StudentReadingCommentDto> getReadingCommentList(Long readingId,Long classId, int offset,int size);

    /**
     * 获取消息列表
     * @param studentId
     * @param offset
     * @param size
     * @return
     */
    List<CommentMessageDto> getCommentMessageList(Long studentId,int offset,int size);

    /**
     * 点赞
     * @param praiseDto
     * @return
     */
    void praise(PraiseDto praiseDto);

    /**
     * 取消点赞
     * @param praiseDto
     */
    void praiseCancel(PraiseDto praiseDto);

    /**
     * 发表评论
     * @param commentDto
     * @return
     */
    void createComment(CommentDto commentDto);

    /**
     * 阅读排行榜
     * @param classId
     * @param chapterId
     * @param type
     * @param offset
     * @param size
     * @return
     */
    List<AgencyStudentDto> getReadingRankingList(Long chapterId,Long classId,int type,int offset,int size);


    /**
     * 删除个人阅读
     * @param readingId
     * @param studentId
     */
    void delUserReading(Long readingId,Long studentId);

    /**
     * 获取一对一阅读列表
     * @param classId
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    List<NewReadingDto> getTeacherOnoToOneList(Long classId,String userId, int offset,int size);

    /**
     * 获取提交列表
     * @param userId
     * @param classId
     * @param taskId
     * @return
     */
    NewReadingListDto getTeacherCommitList(String userId, Long classId, Long taskId);


    /**
     * 获取未提交信息
     * @param userId
     * @param classId
     * @param taskId
     * @return
     */
    AgencyStudentListDto getTeacherUnCommitList(String userId, Long classId, Long taskId);




}
