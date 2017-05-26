/**
 * Copyright (c) 2017 JAHA SMART CORP., LTD ALL RIGHT RESERVED
 *
 * 2017. 4. 29.
 */
package com.realsnake.sample.model.fcm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 * Class Name : Content.java
 * Description : Description
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
public class Message implements Serializable {

    /** SID */
    private static final long serialVersionUID = -3654217538876123328L;

    private String title;

    private String body;

    private String adUrl;

    public Message(String title, String body) {
        super();
        this.title = title;
        this.body = body;
    }

    public Message(String title, String body, String adUrl) {
        this(title, body);
        this.adUrl = adUrl;
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

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

}
