/**
 *
 */
package com.realsnake.sample.model.uhdb;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Domain class mapped db-table called tb_ap0101
 */
@Alias(value = "AptVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AptVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = -6967253061791682815L;

    private String aptId;
    /** 아파트 상세명 */
    private String aptNm;
    /** 아파트 명 */
    private String aptsNm;
    private String zipCd;
    private String city;
    private String sigu;
    private String addr1;
    private String addr2;
    @JsonIgnore
    private Integer instCnt;

    public String getAptId() {
        return aptId;
    }

    public String getAptNm() {
        return aptNm;
    }

    public String getAptsNm() {
        return aptsNm;
    }

    public String getZipCd() {
        return zipCd;
    }

    public String getCity() {
        return city;
    }

    public String getSigu() {
        return sigu;
    }

    public String getAddr1() {
        return addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public Integer getInstCnt() {
        return instCnt;
    }

    public void setAptId(String aptId) {
        this.aptId = aptId;
    }

    public void setAptNm(String aptNm) {
        this.aptNm = aptNm;
    }

    public void setAptsNm(String aptsNm) {
        this.aptsNm = aptsNm;
    }

    public void setZipCd(String zipCd) {
        this.zipCd = zipCd;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSigu(String sigu) {
        this.sigu = sigu;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public void setInstCnt(Integer instCnt) {
        this.instCnt = instCnt;
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
