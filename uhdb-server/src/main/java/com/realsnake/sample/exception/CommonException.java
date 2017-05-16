package com.realsnake.sample.exception;

public class CommonException extends Exception {

    /** SID */
    private static final long serialVersionUID = 9187825194139896559L;

    public CommonException(String message) {
        super(message);
    }

    public CommonException(Throwable e) {
        super(e);
    }

}
