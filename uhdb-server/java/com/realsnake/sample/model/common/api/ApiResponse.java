package com.realsnake.sample.model.common.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ApiResponse<T> implements Serializable {

    /** SID */
    private static final long serialVersionUID = -2304048675131141492L;

    private ApiResponseHeader header;

    @JsonInclude(Include.NON_NULL)
    private T body;

    @JsonInclude(Include.NON_NULL)
    private ApiResponseFooter footer;

    public ApiResponse() {
        this.header = new ApiResponseHeader();
        // this.footer = new ApiResponseFooter();
    }

    public ApiResponse(String resultCode, String resultMessage) {
        this.header = new ApiResponseHeader(resultCode, resultMessage);
    }

    public ApiResponse(String resultCode, String resultMessage, T body) {
        this.header = new ApiResponseHeader(resultCode, resultMessage);
        this.body = body;
    }

    public ApiResponse(String resultCode, String resultMessage, T body, int nextPageToken, int pageSize) {
        this.header = new ApiResponseHeader(resultCode, resultMessage);
        this.body = body;
        this.footer = new ApiResponseFooter(nextPageToken, pageSize);
    }

    public ApiResponse(int nextPageToken, int pageSize) {
        this.header = new ApiResponseHeader();
        this.footer = new ApiResponseFooter(nextPageToken, pageSize);
    }

    public ApiResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ApiResponseHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public ApiResponseFooter getFooter() {
        return footer;
    }

    public void setFooter(ApiResponseFooter footer) {
        this.footer = footer;
    }

}
