/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 29.
 */
package com.realsnake.sample.model.fcm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 * Class Name : Fcm.java
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
public class FcmReqForm implements Serializable {

    /** SID */
    private static final long serialVersionUID = 8050391999357765387L;

    private Data data;

    /** FCM 토큰 */
    private String to;

    public FcmReqForm(Data data, String to) {
        super();
        this.data = data;
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
