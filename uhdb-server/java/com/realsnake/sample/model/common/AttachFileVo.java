/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 30.
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
 * Class Name : AttachFileVo.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 30.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 30.
 * @version 1.0
 */
@Alias(value = "AttachFileVo")
public class AttachFileVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = 2313901094875780226L;

    private Integer fileSeq;
    /** notice(공지사항)/ad(광고)/sponsor(광고주)/store(매장) */
    private String gubun;
    /** 광고의 경우 시작(start)/종료(end)/배너(banner)/팝업(popup)/푸시(push) */
    private String subGubun;
    /** 그룹 일련번호 */
    private Integer groupSeq;
    private String path;
    private String orgName;
    /** 저장된 파일명 */
    private String name;
    private Integer size;
    private String ext;
    private Integer regUserSeq;
    private Date regDate;
    private Integer modUserSeq;
    private Date modDate;
    private String delYn = "N";
    private Integer delUserSeq;
    private Date delDate;

    public Integer getFileSeq() {
        return fileSeq;
    }

    public void setFileSeq(Integer fileSeq) {
        this.fileSeq = fileSeq;
    }

    public String getGubun() {
        return gubun;
    }

    public void setGubun(String gubun) {
        this.gubun = gubun;
    }

    public String getSubGubun() {
        return subGubun;
    }

    public void setSubGubun(String subGubun) {
        this.subGubun = subGubun;
    }

    public Integer getGroupSeq() {
        return groupSeq;
    }

    public void setGroupSeq(Integer groupSeq) {
        this.groupSeq = groupSeq;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Integer getRegUserSeq() {
        return regUserSeq;
    }

    public void setRegUserSeq(Integer regUserSeq) {
        this.regUserSeq = regUserSeq;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Integer getModUserSeq() {
        return modUserSeq;
    }

    public void setModUserSeq(Integer modUserSeq) {
        this.modUserSeq = modUserSeq;
    }

    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public String getDelYn() {
        return delYn;
    }

    public void setDelYn(String delYn) {
        this.delYn = delYn;
    }

    public Integer getDelUserSeq() {
        return delUserSeq;
    }

    public void setDelUserSeq(Integer delUserSeq) {
        this.delUserSeq = delUserSeq;
    }

    public Date getDelDate() {
        return delDate;
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
