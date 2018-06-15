/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 19.
 */
package com.realsnake.sample.model.token;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <pre>
 * Class Name : AuthToken.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 19.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 19.
 * @version 1.0
 */
public class AuthToken implements Serializable {

    /** SID */
    private static final long serialVersionUID = 2791796420669081079L;

    /** 세션ID */
    private String sessionId;
    /** 사용자 일련번호(user.seq) */
    private Integer seq;
    /** 사용자 아이디 */
    private String username;

    public AuthToken(String sessionId, Integer seq, String username) {
        this.sessionId = sessionId;
        this.seq = seq;
        this.username = username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Integer getSeq() {
        return seq;
    }

    public String getUsername() {
        return username;
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
