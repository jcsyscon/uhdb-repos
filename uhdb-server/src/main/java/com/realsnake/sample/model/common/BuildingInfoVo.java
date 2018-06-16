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
public class BuildingInfoVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = -8108118161195763413L;

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
    /** 도로명 */
    private String roadName;
    /** 지하여부. 0:지상, 1:지하, 2:공중 */
    private String jihaYn;
    /** 건물본번 */
    private Integer buildingBonbun;
    /** 건물부번 */
    private Integer buildingBubun;
    /** 건물명 */
    private String buildingName;
    /** 상세건물명 */
    private String detailBuildingName;
    /** 건물관리번호 */
    private String buildingMgtNo;
    /** 읍면동일련번호 */
    private String eopmyendongSeq;
    /** 행정동코드 */
    private String haengjungdongCode;
    /** 행정동명 */
    private String haengjungdongName;
    /** 우편번호. 15.8.1일부로기초구역번호제공 */
    private String zipCode;
    /** 우편일련번호. 15.8.1일부로공란 */
    private String zipSeq;
    /** 다량배달처명. 15.8.1일부로공란 */
    private String massDeliveryName;
    /** 이동사유코드 */
    private String moveReasonCode;
    /** 고시일자. YYYYMMDD */
    private String dispDate;
    /** 변경전도로명. 미사용 */
    private String beforeUpdateRoadName;
    /** 시군구용건물명 */
    private String buildingNamePerSigungu;
    /** 공동주택여부. 0:비공동주택, 1:공동주택 */
    private String unionHousingYn;
    /** 기초구역번호. 15.8.1일부로사용되는새우편번호 */
    private String newZipCode;
    /** 상세주소부여여부. 0:미부여, 1:부여 */
    private String detailAddrYn;
    /** 비고1 */
    private String bigo1;
    /** 비고2 */
    private String bigo2;

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

    public String getRoadName() {
        return roadName;
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

    public String getBuildingName() {
        return buildingName;
    }

    public String getDetailBuildingName() {
        return detailBuildingName;
    }

    public String getBuildingMgtNo() {
        return buildingMgtNo;
    }

    public String getEopmyendongSeq() {
        return eopmyendongSeq;
    }

    public String getHaengjungdongCode() {
        return haengjungdongCode;
    }

    public String getHaengjungdongName() {
        return haengjungdongName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getZipSeq() {
        return zipSeq;
    }

    public String getMassDeliveryName() {
        return massDeliveryName;
    }

    public String getMoveReasonCode() {
        return moveReasonCode;
    }

    public String getDispDate() {
        return dispDate;
    }

    public String getBeforeUpdateRoadName() {
        return beforeUpdateRoadName;
    }

    public String getBuildingNamePerSigungu() {
        return buildingNamePerSigungu;
    }

    public String getUnionHousingYn() {
        return unionHousingYn;
    }

    public String getNewZipCode() {
        return newZipCode;
    }

    public String getDetailAddrYn() {
        return detailAddrYn;
    }

    public String getBigo1() {
        return bigo1;
    }

    public String getBigo2() {
        return bigo2;
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

    public void setRoadName(String roadName) {
        this.roadName = roadName;
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

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public void setDetailBuildingName(String detailBuildingName) {
        this.detailBuildingName = detailBuildingName;
    }

    public void setBuildingMgtNo(String buildingMgtNo) {
        this.buildingMgtNo = buildingMgtNo;
    }

    public void setEopmyendongSeq(String eopmyendongSeq) {
        this.eopmyendongSeq = eopmyendongSeq;
    }

    public void setHaengjungdongCode(String haengjungdongCode) {
        this.haengjungdongCode = haengjungdongCode;
    }

    public void setHaengjungdongName(String haengjungdongName) {
        this.haengjungdongName = haengjungdongName;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setZipSeq(String zipSeq) {
        this.zipSeq = zipSeq;
    }

    public void setMassDeliveryName(String massDeliveryName) {
        this.massDeliveryName = massDeliveryName;
    }

    public void setMoveReasonCode(String moveReasonCode) {
        this.moveReasonCode = moveReasonCode;
    }

    public void setDispDate(String dispDate) {
        this.dispDate = dispDate;
    }

    public void setBeforeUpdateRoadName(String beforeUpdateRoadName) {
        this.beforeUpdateRoadName = beforeUpdateRoadName;
    }

    public void setBuildingNamePerSigungu(String buildingNamePerSigungu) {
        this.buildingNamePerSigungu = buildingNamePerSigungu;
    }

    public void setUnionHousingYn(String unionHousingYn) {
        this.unionHousingYn = unionHousingYn;
    }

    public void setNewZipCode(String newZipCode) {
        this.newZipCode = newZipCode;
    }

    public void setDetailAddrYn(String detailAddrYn) {
        this.detailAddrYn = detailAddrYn;
    }

    public void setBigo1(String bigo1) {
        this.bigo1 = bigo1;
    }

    public void setBigo2(String bigo2) {
        this.bigo2 = bigo2;
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
