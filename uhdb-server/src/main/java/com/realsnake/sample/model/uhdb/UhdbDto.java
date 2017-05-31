/**
 * Copyright (c) 2016 realsnake1975@gmail.com
 *
 * 2016. 10. 13.
 */
package com.realsnake.sample.model.uhdb;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.realsnake.sample.model.common.CommonDto;

/**
 * <pre>
 * Class Name : UhdbDto.java
 * Description : UhdbDto
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2016. 10. 13.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2016. 10. 13.
 * @version 1.0
 */
public class UhdbDto extends CommonDto implements Serializable {

    /** SID */
    private static final long serialVersionUID = -67906733739787001L;

    private Integer userSeq;
    private String aptId;
    private String aptPosi;
    private String dong;
    private String ho;
    /** past / now */
    private String gubun = "past";

    public Integer getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Integer userSeq) {
        this.userSeq = userSeq;
    }

    public String getAptId() {
        return aptId;
    }

    public String getAptPosi() {
        return aptPosi;
    }

    public String getDong() {
        return dong;
    }

    public String getHo() {
        return ho;
    }

    public String getGubun() {
        return gubun;
    }

    public void setAptId(String aptId) {
        this.aptId = aptId;
    }

    public void setAptPosi(String aptPosi) {
        this.aptPosi = aptPosi;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public void setGubun(String gubun) {
        this.gubun = gubun;
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
