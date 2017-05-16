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
 * Class Name : Data.java
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
public class Data implements Serializable {

    /** SID */
    private static final long serialVersionUID = 3275884604586144493L;

    private Content content;

    private Etc etc;

    public Data(Content content) {
        super();
        this.content = content;
    }

    public Data(Content content, Etc etc) {
        super();
        this.content = content;
        this.etc = etc;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Etc getEtc() {
        return etc;
    }

    public void setEtc(Etc etc) {
        this.etc = etc;
    }

}
