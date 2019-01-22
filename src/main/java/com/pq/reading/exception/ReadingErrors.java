package com.pq.reading.exception;


import com.pq.common.exception.CommonErrors;
import com.pq.common.exception.ErrorCode;

/**
 * @author liutao
 */
public class ReadingErrors extends CommonErrors {

    public final static ErrorCode READING_TASK_IS_NOT_EXIST = new ReadingErrorCode("0001", "八点阅读任务不存在");
    public final static ErrorCode READING_RECORD_IS_NOT_EXIST = new ReadingErrorCode("0002", "阅读不存在");
    public final static ErrorCode READING_RECORD_STUDENT_NOT_MATCH = new ReadingErrorCode("0003", "该用户无删除权限");
    public final static ErrorCode READING_PRAISE_IS_NOT_EXIST = new ReadingErrorCode("0004", "该点赞不存在");
    public final static ErrorCode READING_PRAISE_IS_EXIST = new ReadingErrorCode("0005", "抱歉，该用户已经点过赞");



}
