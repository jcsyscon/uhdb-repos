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
 * Class Name : ShopVo.java
 * Description : Description
 * </pre>
 *
 * @author home
 * @since 2017. 5. 10.
 * @version 1.0
 */
@Alias(value = "ShopVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = -2743190258378602085L;

    /** 일련번호 */
    private Integer seq;
    /** 광고주일련번호 */
    private Integer sponsorSeq;
    /** 매장명 */
    private String name;
    /** 매장전화번호 */
    private String tel;
    /** 매장홈페이지 */
    private String homepage;
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

    public Integer getSponsorSeq() {
        return sponsorSeq;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getHomepage() {
        return homepage;
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

    public void setSponsorSeq(Integer sponsorSeq) {
        this.sponsorSeq = sponsorSeq;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
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
