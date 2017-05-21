/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 30.
 */
package com.realsnake.sample.model.board;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 * Class Name : NoticeVo.java
 * Description : Description
 * </pre>
 *
 * @author 전강욱(realsnake1975@gmail.com)
 * @since 2017. 4. 30.
 * @version 1.0
 */
@Alias(value = "NoticeVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoticeVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = -3856600668750638361L;

    private Integer seq;
    private String title;
    private String content;
    @JsonIgnore
    private Integer regUserSeq;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDate;
    @JsonIgnore
    private Integer modUserSeq;
    @JsonIgnore
    private Date modDate;
    @JsonIgnore
    private String delYn = "N";
    @JsonIgnore
    private Integer delUserSeq;
    @JsonIgnore
    private Date delDate;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRegUserSeq() {
        return regUserSeq;
    }

    public void setRegUserSeq(Integer regUserSeq) {
        this.regUserSeq = regUserSeq;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Integer getModUserSeq() {
        return modUserSeq;
    }

    public void setModUserSeq(Integer modUserSeq) {
        this.modUserSeq = modUserSeq;
    }

    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public String getDelYn() {
        return delYn;
    }

    public void setDelYn(String delYn) {
        this.delYn = delYn;
    }

    public Integer getDelUserSeq() {
        return delUserSeq;
    }

    public void setDelUserSeq(Integer delUserSeq) {
        this.delUserSeq = delUserSeq;
    }

    public Date getDelDate() {
        return delDate;
    }

    public void setDelDate(Date delDate) {
        this.delDate = delDate;
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
