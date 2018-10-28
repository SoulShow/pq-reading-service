package com.pq.reading.exception;


import com.pq.common.exception.ErrorCode;

public class ReadingErrorCode extends ErrorCode {

    private final static String PRE = "3";

    public ReadingErrorCode(String errorCode, String errorMsg) {
        super(PRE+errorCode, errorMsg);
    }
}

