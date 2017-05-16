/**
 *
 */
package com.realsnake.sample.model.user;

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
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Domain class mapped db-table called user_uhdb
 */
@Alias(value = "UserUhdbVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUhdbVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = 4400098346424222410L;

    /** 일련번호 */
    private Integer seq;
    /** 사용자일련번호 */
    private Integer userSeq;
    /** 아파트아이디 */
    private String aptId;
    /** 무인택배함아이디 */
    private String uhdbId;
    /** 동 */
    private String dong;
    /** 호 */
    private String ho;
    @JsonIgnore
    private Integer regUserSeq;
    private Date regDate;
    @JsonIgnore
    private String delYn = "N";
    @JsonIgnore
    private Integer delUserSeq;
    @JsonIgnore
    private Date delDate;

    public Integer getSeq() {
        return seq;
    }

    public Integer getUserSeq() {
        return userSeq;
    }

    public String getAptId() {
        return aptId;
    }

    public String getUhdbId() {
        return uhdbId;
    }

    public String getDong() {
        return dong;
    }

    public String getHo() {
        return ho;
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

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public void setUserSeq(Integer userSeq) {
        this.userSeq = userSeq;
    }

    public void setAptId(String aptId) {
        this.aptId = aptId;
    }

    public void setUhdbId(String uhdbId) {
        this.uhdbId = uhdbId;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public void setHo(String ho) {
        this.ho = ho;
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
