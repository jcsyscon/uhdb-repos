/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 10.
 */
package com.realsnake.sample.model.ad;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 * Class Name : SponsorVo.java
 * Description : Description
 * </pre>
 *
 * @author home
 * @since 2017. 5. 10.
 * @version 1.0
 */
@Alias(value = "SponsorVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SponsorVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = 6961567100188843999L;

    /** 일련번호 */
    private Integer seq;
    /** 사용자일련번호(user.seq) */
    private Integer userSeq;
    /** 광고주명 */
    private String name;
    /** 광고주핸드폰번호 */
    private String mobile;
    /** 광고주이메일 */
    private String email;
    /** 비고 */
    private String memo;
    /** 등록자 일련번호 */
    @JsonIgnore
    private Integer regUserSeq;
    /** 등록일시 */
    private Date regDate;
    /** 수정자 일련번호 */
    @JsonIgnore
    private Integer modUserSeq;
    /** 수정일시 */
    @JsonIgnore
    private Date modDate;
    /** 삭제여부 */
    @JsonIgnore
    private String delYn = "N";
    /** 삭제자 일련번호 */
    @JsonIgnore
    private Integer delUserSeq;
    /** 삭제일시 */
    @JsonIgnore
    private Date delDate;

    public Integer getSeq() {
        return seq;
    }

    public Integer getUserSeq() {
        return userSeq;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getMemo() {
        return memo;
    }

    public Integer getRegUserSeq() {
        return regUserSeq;
    }

    public Date getRegDate() {
        return regDate;
    }

    public Integer getModUserSeq() {
        return modUserSeq;
    }

    public Date getModDate() {
        return modDate;
    }

    public String getDelYn() {
        return delYn;
    }

    public Integer getDelUserSeq() {
        return delUserSeq;
    }

    public Date getDelDate() {
        return delDate;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public void setUserSeq(Integer userSeq) {
        this.userSeq = userSeq;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setRegUserSeq(Integer regUserSeq) {
        this.regUserSeq = regUserSeq;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public void setModUserSeq(Integer modUserSeq) {
        this.modUserSeq = modUserSeq;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public void setDelYn(String delYn) {
        this.delYn = delYn;
    }

    public void setDelUserSeq(Integer delUserSeq) {
        this.delUserSeq = delUserSeq;
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
