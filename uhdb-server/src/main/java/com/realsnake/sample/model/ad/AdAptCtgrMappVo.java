/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 10.
 */
package com.realsnake.sample.model.ad;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 * Class Name : AdAptCtgrMappVo.java
 * Description : 광고-아파트-광고카테고리 매핑 VO
 * </pre>
 *
 * @author home
 * @since 2017. 5. 10.
 * @version 1.0
 */
@Alias(value = "AdAptCtgrMappVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdAptCtgrMappVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = -6037815959349248412L;

    private Integer aacmSeq;
    
    private Integer adSeq;
    
    private String targetAptId;
    
    private String adCtgrCode;
    
    private String adCtgrCodeWithComma;
    
    private String targetAptName;

    public Integer getAacmSeq() {
        return aacmSeq;
    }

    public void setAacmSeq(Integer aacmSeq) {
        this.aacmSeq = aacmSeq;
    }

    public Integer getAdSeq() {
        return adSeq;
    }

    public void setAdSeq(Integer adSeq) {
        this.adSeq = adSeq;
    }

    public String getTargetAptId() {
        return targetAptId;
    }

    public void setTargetAptId(String targetAptId) {
        this.targetAptId = targetAptId;
    }

    public String getAdCtgrCode() {
        return adCtgrCode;
    }

    public void setAdCtgrCode(String adCtgrCode) {
        this.adCtgrCode = adCtgrCode;
    }

    public String getAdCtgrCodeWithComma() {
        return adCtgrCodeWithComma;
    }

    public void setAdCtgrCodeWithComma(String adCtgrCodeWithComma) {
        this.adCtgrCodeWithComma = adCtgrCodeWithComma;
    }

    public String getTargetAptName() {
        return targetAptName;
    }

    public void setTargetAptName(String targetAptName) {
        this.targetAptName = targetAptName;
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
