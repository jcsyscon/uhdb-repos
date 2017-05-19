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

    private Message message;

    public Data(Message message) {
        super();
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
