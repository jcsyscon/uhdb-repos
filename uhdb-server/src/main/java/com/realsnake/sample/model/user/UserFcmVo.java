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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.realsnake.sample.constants.CommonConstants.UserDeviceType;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Domain class mapped db-table called user_fcm
 */
@Alias(value = "UserFcmVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserFcmVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = 3999449175665829798L;

    /** 일련번호 */
    private Integer seq;
    /** 사용자일련번호 */
    private Integer userSeq;
    /** FCM토큰 */
    private String fcmToken;
    /** 앱버전 */
    private String appVersion;
    /** 디바이스유형(안드로이드/아이폰/none) */
    private String deviceType = UserDeviceType.ANDROID.getValue();
    /** 등록일시 */
    private Date regDate;

    private String delYn = "N";
    private Integer delUserSeq;
    private Date delDate;

    public Integer getSeq() {
        return seq;
    }

    public Integer getUserSeq() {
        return userSeq;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public void setUserSeq(Integer userSeq) {
        this.userSeq = userSeq;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
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

    public void setDelYn(String delYn) {
        this.delYn = delYn;
    }

    public void setDelUserSeq(Integer delUserSeq) {
        this.delUserSeq = delUserSeq;
    }

    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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
