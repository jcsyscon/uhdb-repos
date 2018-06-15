/**
 * Copyright (c) 2017 JAHA SMART CORP., LTD ALL RIGHT RESERVED
 *
 * 2017. 4. 29.
 */
package com.realsnake.sample.model.fcm;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 * Class Name : Notification.java
 * Description : IOS용 FCM 메시지
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 29.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 29.
 * @version 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification implements Serializable {

    /** SID */
    private static final long serialVersionUID = 585319736318142103L;

    private String title;

    private String body;

    private String click_action;

    public Notification(String title, String body) {
        super();
        this.title = title;
        this.body = body;
    }

    public Notification(String title, String body, String click_action) {
        this(title, body);
        this.click_action = click_action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getClick_action() {
        return click_action;
    }

    public void setClick_action(String click_action) {
        this.click_action = click_action;
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
