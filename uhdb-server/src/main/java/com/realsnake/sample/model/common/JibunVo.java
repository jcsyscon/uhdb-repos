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
 * Class Name : Jibun.java
 * Description : Description
 * </pre>
 *
 * @author home
 * @since 2017. 5. 9.
 * @version 1.0
 */
public class JibunVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = 3408476658733623764L;

    /** 법정동코드 */
    private String beopjungdongCode;
    /** 시도명 */
    private String sidoName;
    /** 시군구명 */
    private String sigunguName;
    /** 법정읍면동명 */
    private String beopjungeopmyendongName;
    /** 법정리명 */
    private String beopjungriName;
    /** 산여부. 0:대지, 1:산 */
    private String sanYn;
    /** 지번본번(번지) */
    private Integer bunji;
    /** 지번부번(호) */
    private Integer ho;
    /** 도로명코드. 시군구코드(5)+도로명번호(7) */
    private String roadNameCode;
    /** 지하여부. 0:지상, 1:지하, 2:공중 */
    private String jihaYn;
    /** 건물본번 */
    private Integer buildingBonbun;
    /** 건물부번 */
    private Integer buildingBubun;
    /** 지번일련번호 */
    private Integer jibunSeq;
    /** 이동사유코드 */
    private String moveReasonCode;

    public String getBeopjungdongCode() {
        return beopjungdongCode;
    }

    public String getSidoName() {
        return sidoName;
    }

    public String getSigunguName() {
        return sigunguName;
    }

    public String getBeopjungeopmyendongName() {
        return beopjungeopmyendongName;
    }

    public String getBeopjungriName() {
        return beopjungriName;
    }

    public String getSanYn() {
        return sanYn;
    }

    public Integer getBunji() {
        return bunji;
    }

    public Integer getHo() {
        return ho;
    }

    public String getRoadNameCode() {
        return roadNameCode;
    }

    public String getJihaYn() {
        return jihaYn;
    }

    public Integer getBuildingBonbun() {
        return buildingBonbun;
    }

    public Integer getBuildingBubun() {
        return buildingBubun;
    }

    public Integer getJibunSeq() {
        return jibunSeq;
    }

    public String getMoveReasonCode() {
        return moveReasonCode;
    }

    public void setBeopjungdongCode(String beopjungdongCode) {
        this.beopjungdongCode = beopjungdongCode;
    }

    public void setSidoName(String sidoName) {
        this.sidoName = sidoName;
    }

    public void setSigunguName(String sigunguName) {
        this.sigunguName = sigunguName;
    }

    public void setBeopjungeopmyendongName(String beopjungeopmyendongName) {
        this.beopjungeopmyendongName = beopjungeopmyendongName;
    }

    public void setBeopjungriName(String beopjungriName) {
        this.beopjungriName = beopjungriName;
    }

    public void setSanYn(String sanYn) {
        this.sanYn = sanYn;
    }

    public void setBunji(Integer bunji) {
        this.bunji = bunji;
    }

    public void setHo(Integer ho) {
        this.ho = ho;
    }

    public void setRoadNameCode(String roadNameCode) {
        this.roadNameCode = roadNameCode;
    }

    public void setJihaYn(String jihaYn) {
        this.jihaYn = jihaYn;
    }

    public void setBuildingBonbun(Integer buildingBonbun) {
        this.buildingBonbun = buildingBonbun;
    }

    public void setBuildingBubun(Integer buildingBubun) {
        this.buildingBubun = buildingBubun;
    }

    public void setJibunSeq(Integer jibunSeq) {
        this.jibunSeq = jibunSeq;
    }

    public void setMoveReasonCode(String moveReasonCode) {
        this.moveReasonCode = moveReasonCode;
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
