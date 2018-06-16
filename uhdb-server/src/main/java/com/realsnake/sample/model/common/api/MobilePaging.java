/**
 * Copyright (c) 2016 realsnake1975@gmail.com
 *
 * 2016. 9. 22.
 */
package com.realsnake.sample.model.common.api;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author 전강욱(realsnake1975@gmail.com)
 * @설명 : 모바일 페이징 공통 VO
 */
public class MobilePaging implements Serializable {

    /** SID */
    private static final long serialVersionUID = -870422203027990095L;

    /** 마지막 게시글 수 */
    // @JsonView({PostView.FaqList.class, PostView.SystemNoticeList.class, PostView.GroupList.class, PostView.EventList.class, CommentView.CommentList.class})
    private Integer nextPageToken;
    /** 한 페이지 당 게시물 개수 */
    // @JsonView({PostView.FaqList.class, PostView.SystemNoticeList.class, PostView.GroupList.class, PostView.EventList.class, CommentView.CommentList.class})
    private int pageSize = 20;
    /** 전체 게시글 수 */
    // @JsonView({PostView.FaqList.class, PostView.SystemNoticeList.class, PostView.GroupList.class, PostView.EventList.class, CommentView.CommentList.class})
    private long totalCount;

    public Integer getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(Integer nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}
