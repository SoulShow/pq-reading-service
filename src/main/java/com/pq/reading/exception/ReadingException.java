package com.pq.reading.exception;


import com.pq.common.exception.BaseException;
import com.pq.common.exception.ErrorCode;

/**
 * @author liutao
 */
public class ReadingException extends BaseException {

    public ReadingException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ReadingException(ErrorCode errorCode) {
        super(errorCode);
    }


    /**
     * 抛UserException异常
     * @param errorCode
     */
    public static void raise(ErrorCode errorCode){
        throw new ReadingException(errorCode);
    }


    /**
     * 抛UserException异常
     * @param errorCode
     * @param cause 异常
     */
    public static void raise(ErrorCode errorCode, Throwable cause){
        throw new ReadingException(errorCode, cause);
    }
}
