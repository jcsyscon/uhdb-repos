/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 9.
 */
package com.realsnake.sample.model.common;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <pre>
 * Class Name : BuildingInfo.java
 * Description : Description
 * </pre>
 *
 * @author home
 * @since 2017. 5. 9.
 * @version 1.0
 */
public class RoadNameCodeVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = -3292025400725002926L;

    /** 시군구코드 */
    private String sigunguCode;
    /** 도로명번호 */
    private String roadNameNo;
    /** 도로명 */
    private String roadName;
    /** 도로명로마자 */
    private String roadNameRome;
    /** 읍면동일련번호 */
    private String eopmyendongSeq;
    /** 시도명 */
    private String sidoName;
    /** 시군구명 */
    private String sigunguName;
    /** 읍면동구분. 0:읍면, 1:동, 2:미부여 */
    private String eopmyendongGubun;
    /** 읍면동코드. 법정동기준읍면동코드 */
    private String eopmyendongCode;
    /** 읍면동명 */
    private String eopmyendongName;
    /** 상위도로명번호 */
    private String upperRoadNameNo;
    /** 상위도로명 */
    private String upperRoadName;
    /** 사용여부. 0:사용, 1:미사용 */
    private String useYn;
    /** 변경사유. 0:도로명변경, 1:도로명폐지, 2:시도.시군구, 3:읍면동, 4:영문명변경 */
    private String updateReason;
    /** 변경이력정보. 시군구코드(5)+도로명번호(7)+읍면동일련번호(2) */
    private String updateHistInfo;
    /** 시도명로마자 */
    private String sidoNameRome;
    /** 시군구명로마자 */
    private String sigunguNameRome;
    /** 읍면동명로마자 */
    private String eopmyendongNameRome;
    /** 고시일자. YYYYMMDD */
    private String dispDate;
    /** 말소일자. YYYYMMDD */
    private String endDate;

    public String getSigunguCode() {
        return sigunguCode;
    }

    public String getRoadNameNo() {
        return roadNameNo;
    }

    public String getRoadName() {
        return roadName;
    }

    public String getRoadNameRome() {
        return roadNameRome;
    }

    public String getEopmyendongSeq() {
        return eopmyendongSeq;
    }

    public String getSidoName() {
        return sidoName;
    }

    public String getSigunguName() {
        return sigunguName;
    }

    public String getEopmyendongGubun() {
        return eopmyendongGubun;
    }

    public String getEopmyendongCode() {
        return eopmyendongCode;
    }

    public String getEopmyendongName() {
        return eopmyendongName;
    }

    public String getUpperRoadNameNo() {
        return upperRoadNameNo;
    }

    public String getUpperRoadName() {
        return upperRoadName;
    }

    public String getUseYn() {
        return useYn;
    }

    public String getUpdateReason() {
        return updateReason;
    }

    public String getUpdateHistInfo() {
        return updateHistInfo;
    }

    public String getSidoNameRome() {
        return sidoNameRome;
    }

    public String getSigunguNameRome() {
        return sigunguNameRome;
    }

    public String getEopmyendongNameRome() {
        return eopmyendongNameRome;
    }

    public String getDispDate() {
        return dispDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setSigunguCode(String sigunguCode) {
        this.sigunguCode = sigunguCode;
    }

    public void setRoadNameNo(String roadNameNo) {
        this.roadNameNo = roadNameNo;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public void setRoadNameRome(String roadNameRome) {
        this.roadNameRome = roadNameRome;
    }

    public void setEopmyendongSeq(String eopmyendongSeq) {
        this.eopmyendongSeq = eopmyendongSeq;
    }

    public void setSidoName(String sidoName) {
        this.sidoName = sidoName;
    }

    public void setSigunguName(String sigunguName) {
        this.sigunguName = sigunguName;
    }

    public void setEopmyendongGubun(String eopmyendongGubun) {
        this.eopmyendongGubun = eopmyendongGubun;
    }

    public void setEopmyendongCode(String eopmyendongCode) {
        this.eopmyendongCode = eopmyendongCode;
    }

    public void setEopmyendongName(String eopmyendongName) {
        this.eopmyendongName = eopmyendongName;
    }

    public void setUpperRoadNameNo(String upperRoadNameNo) {
        this.upperRoadNameNo = upperRoadNameNo;
    }

    public void setUpperRoadName(String upperRoadName) {
        this.upperRoadName = upperRoadName;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public void setUpdateReason(String updateReason) {
        this.updateReason = updateReason;
    }

    public void setUpdateHistInfo(String updateHistInfo) {
        this.updateHistInfo = updateHistInfo;
    }

    public void setSidoNameRome(String sidoNameRome) {
        this.sidoNameRome = sidoNameRome;
    }

    public void setSigunguNameRome(String sigunguNameRome) {
        this.sigunguNameRome = sigunguNameRome;
    }

    public void setEopmyendongNameRome(String eopmyendongNameRome) {
        this.eopmyendongNameRome = eopmyendongNameRome;
    }

    public void setDispDate(String dispDate) {
        this.dispDate = dispDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
