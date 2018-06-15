package com.realsnake.sample.model.common.api;

import java.io.Serializable;

public class ApiResponseHeader implements Serializable {

    /** SID */
    private static final long serialVersionUID = -452278311557514925L;

    /** 오류 코드 "00" : 요청 성공, 이외의 경우 오류상태 */
    private String resultCode = "00";
    /** 오류 메시지 */
    private String resultMessage = "SUCCESS";

    public ApiResponseHeader() {

    }

    public ApiResponseHeader(String resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

}
