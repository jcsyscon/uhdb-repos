/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 10.
 */
package com.realsnake.sample.model.common;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

/**
 * <pre>
 * Class Name : AddressVo.java
 * Description : Description
 * </pre>
 *
 * @author home
 * @since 2017. 5. 10.
 * @version 1.0
 */
@Alias(value = "AddressVo")
public class AddressVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = 4944846478462281299L;

    private Integer addrSeq;
    /** user / sponsor / shop */
    private String gubun;
    private Integer groupSeq;
    private String zipCode;
    private String addr1;
    private String addr2;
    private String lat;
    private String lng;
    private Integer regUserSeq;
    private Date regDate;
    private String delYn = "N";
    private Integer delUserSeq;
    private Date delDate;

    public Integer getAddrSeq() {
        return addrSeq;
    }

    public String getGubun() {
        return gubun;
    }

    public Integer getGroupSeq() {
        return groupSeq;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getAddr1() {
        return addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public Integer getRegUserSeq() {
        return regUserSeq;
    }

    public Date getRegDate() {
        return regDate;
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

    public void setAddrSeq(Integer addrSeq) {
        this.addrSeq = addrSeq;
    }

    public void setGubun(String gubun) {
        this.gubun = gubun;
    }

    public void setGroupSeq(Integer groupSeq) {
        this.groupSeq = groupSeq;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setRegUserSeq(Integer regUserSeq) {
        this.regUserSeq = regUserSeq;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
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
