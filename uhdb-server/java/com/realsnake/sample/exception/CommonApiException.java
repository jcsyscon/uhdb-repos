package com.realsnake.sample.exception;

import com.realsnake.sample.constants.ApiResultCode;

public class CommonApiException extends Exception {

    /** SID */
    private static final long serialVersionUID = 8241391858204766132L;

    private String resCode = null;
    private String resMessage = null;

    public CommonApiException(ApiResultCode resultCode) {
        super(resultCode.getMessage());
        this.resCode = resultCode.getCode();
        this.resMessage = resultCode.getMessage();
    }

    public CommonApiException(ApiResultCode resultCode, Throwable e) {
        super(e);
        this.resCode = resultCode.getCode();
        this.resMessage = resultCode.getMessage();
    }

    public String getResMessage() {
        return resMessage;
    }

    public String getResCode() {
        return resCode;
    }

}
