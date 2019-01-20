package com.pq.reading.exception;


import com.pq.common.exception.CommonErrors;
import com.pq.common.exception.ErrorCode;

/**
 * @author liutao
 */
public class ReadingErrors extends CommonErrors {

    public final static ErrorCode READING_TASK_IS_NOT_EXIST = new ReadingErrorCode("0001", "八点阅读任务不存在");
    public final static ErrorCode READING_RECORD_IS_NOT_EXIST = new ReadingErrorCode("0002", "阅读不存在");



}
