/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 30.
 */
package com.realsnake.sample.model.uhdb;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 * Class Name : NfcVo.java
 * Description : tb_ap0301
 * </pre>
 *
 * @author gaon15
 * @since 2017. 5. 30.
 * @version 1.0
 */
@Alias(value = "NfcVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NfcVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = 7668820878378329917L;

    private Date insDt;
    private Date updDt;

    /** 아파트아이디 aptId */
    private String aptId;
    /** 택배함설치장소 aptPosi */
    private String aptPosi;
    /** 카드ID값 perid */
    private String perId;
    /** 동 aptdong */
    private String dong;
    /** 호 aptho */
    private String ho;
    /** 카드구분(카드/핸드폰) perid_gb */
    private String perIdGb;
    /** 카드명 pernm */
    private String perNm;
    /** 입주민/택배기사 구분 pergb */
    private String perGb;

    public Date getInsDt() {
        return insDt;
    }

    public Date getUpdDt() {
        return updDt;
    }

    public String getAptId() {
        return aptId;
    }

    public String getAptPosi() {
        return aptPosi;
    }

    public String getPerId() {
        return perId;
    }

    public String getDong() {
        return dong;
    }

    public String getHo() {
        return ho;
    }

    public String getPerIdGb() {
        return perIdGb;
    }

    public String getPerNm() {
        return perNm;
    }

    public String getPerGb() {
        return perGb;
    }

    public void setInsDt(Date insDt) {
        this.insDt = insDt;
    }

    public void setUpdDt(Date updDt) {
        this.updDt = updDt;
    }

    public void setAptId(String aptId) {
        this.aptId = aptId;
    }

    public void setAptPosi(String aptPosi) {
        this.aptPosi = aptPosi;
    }

    public void setPerId(String perId) {
        this.perId = perId;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public void setPerIdGb(String perIdGb) {
        this.perIdGb = perIdGb;
    }

    public void setPerNm(String perNm) {
        this.perNm = perNm;
    }

    public void setPerGb(String perGb) {
        this.perGb = perGb;
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
