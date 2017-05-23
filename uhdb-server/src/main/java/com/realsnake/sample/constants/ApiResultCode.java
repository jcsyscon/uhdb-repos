package com.realsnake.sample.constants;

public enum ApiResultCode {

    /* @formatter:off */
    COMMON_SUCCESS("00", "SUCCESS")
    , COMMON_FAIL("99", "FAIL")
    , NOTFOUND_USER("01", "USER NOT FOUND")
    ;
    /* @formatter:on */

    ApiResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("Code:%s, Message:%s", getCode(), getMessage());
    }

}
