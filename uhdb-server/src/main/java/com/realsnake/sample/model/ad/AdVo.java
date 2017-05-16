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
 * Class Name : AdVo.java
 * Description : Description
 * </pre>
 *
 * @author home
 * @since 2017. 5. 10.
 * @version 1.0
 */
@Alias(value = "AdVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = 1101507531834267603L;

    /** 일련번호 */
    private Integer seq;
    /** 매장일련번호 */
    private Integer shopSeq;
    /** 광고타이틀 */
    private String title;
    /** 광고카피 */
    private String copy;
    /** 광고방식. CPC, CPM, PPC, CPS */
    private String way;
    /** 광고단가 */
    private Integer unitPrice;
    /** 광고시작일시(yyyyMMddHHmmss) */
    private String startDate;
    /** 광고종료일시(yyyyMMddHHmmss) */
    private String endDate;
    /** 타겟지역. 예)경기도 광명시 */
    private String targetArea;
    /** 타겟연령 */
    private String targetAge;
    /** 타겟성별. 남자:M,여자:F */
    private String targetSex;
    /** 타겟URL */
    private String targetUrl;
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

    public Integer getShopSeq() {
        return shopSeq;
    }

    public String getTitle() {
        return title;
    }

    public String getCopy() {
        return copy;
    }

    public void setCopy(String copy) {
        this.copy = copy;
    }

    public String getWay() {
        return way;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getTargetArea() {
        return targetArea;
    }

    public String getTargetAge() {
        return targetAge;
    }

    public String getTargetSex() {
        return targetSex;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
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

    public void setShopSeq(Integer shopSeq) {
        this.shopSeq = shopSeq;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setTargetArea(String targetArea) {
        this.targetArea = targetArea;
    }

    public void setTargetAge(String targetAge) {
        this.targetAge = targetAge;
    }

    public void setTargetSex(String targetSex) {
        this.targetSex = targetSex;
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
