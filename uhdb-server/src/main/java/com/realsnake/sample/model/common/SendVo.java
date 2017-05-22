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
 * Class Name : SendVo.java
 * Description : Description
 * </pre>
 *
 * @author home
 * @since 2017. 5. 10.
 * @version 1.0
 */
@Alias(value = "SendVo")
public class SendVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = -1644933266960898292L;

    private Integer seq;
    /** 푸시/SMS 발송 유형 - 인증번호발송: sms-auth-number, 무인택배함정보: sms-uhdb, 가입권유: sms-apply, 가입권유: push-uhdb, 푸시광고: push-ad */
    private String gubun;
    private Long uhdbLogSeq;
    private Integer userSeq;
    private String mobile;
    private String fcmToken;
    private String sendMessage;
    private Date sendDate;
    private String resultMessageId;
    private String resultMessage;
    private String googleFcmToken;
    private String successYn = "N";
    private String etc;

    public Integer getSeq() {
        return seq;
    }

    public String getGubun() {
        return gubun;
    }

    public Long getUhdbLogSeq() {
        return uhdbLogSeq;
    }

    public Integer getUserSeq() {
        return userSeq;
    }

    public String getMobile() {
        return mobile;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public String getResultMessageId() {
        return resultMessageId;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public String getGoogleFcmToken() {
        return googleFcmToken;
    }

    public String getSuccessYn() {
        return successYn;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public void setGubun(String gubun) {
        this.gubun = gubun;
    }

    public void setUhdbLogSeq(Long uhdbLogSeq) {
        this.uhdbLogSeq = uhdbLogSeq;
    }

    public void setUserSeq(Integer userSeq) {
        this.userSeq = userSeq;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public void setResultMessageId(String resultMessageId) {
        this.resultMessageId = resultMessageId;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public void setGoogleFcmToken(String googleFcmToken) {
        this.googleFcmToken = googleFcmToken;
    }

    public void setSuccessYn(String successYn) {
        this.successYn = successYn;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
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
