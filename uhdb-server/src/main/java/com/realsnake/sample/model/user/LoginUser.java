/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 19.
 */
package com.realsnake.sample.model.user;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.realsnake.sample.constants.CommonConstants;

/**
 * <pre>
 * Class Name : LoginUser.java
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
public class LoginUser extends User implements Serializable {

    /** SID */
    private static final long serialVersionUID = -7759679265785466005L;

    /** 사용자 일련번호(user.seq) */
    private Integer seq;
    /** 관리자 여부 */
    private Boolean isAdmin;

    public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        for (GrantedAuthority ga : authorities) {
            if (CommonConstants.RollType.ROLL_ADMIN.getValue().equalsIgnoreCase(ga.getAuthority())) {
                this.isAdmin = true;
            }
        }
    }

    public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Integer seq) {
        super(username, password, authorities);
        this.seq = seq;

        for (GrantedAuthority ga : authorities) {
            if (CommonConstants.RollType.ROLL_ADMIN.getValue().equalsIgnoreCase(ga.getAuthority())) {
                this.isAdmin = true;
            }
        }
    }

    public Integer getSeq() {
        return seq;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
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
