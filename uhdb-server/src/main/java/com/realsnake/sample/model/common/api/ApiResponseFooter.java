package com.realsnake.sample.model.common.api;

import java.io.Serializable;

public class ApiResponseFooter implements Serializable {

    /** SID */
    private static final long serialVersionUID = -8897946799315046268L;

    /** 목록의 마지막 글번호 */
    private int nextPageToken;
    /** 페이지 사이즈 */
    private int pageSize;


    public ApiResponseFooter() {

    }

    public ApiResponseFooter(int nextPageToken, int pageSize) {
        super();
        this.nextPageToken = nextPageToken;
        this.pageSize = pageSize;
    }

    public int getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(int nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
