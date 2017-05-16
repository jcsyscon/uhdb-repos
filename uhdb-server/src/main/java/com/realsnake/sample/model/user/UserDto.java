/**
 * Copyright (c) 2016 realsnake1975@gmail.com
 *
 * 2016. 10. 13.
 */
package com.realsnake.sample.model.user;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.realsnake.sample.model.common.CommonDto;

/**
 * <pre>
 * Class Name : UserDto.java
 * Description : UserDto
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2016. 10. 13.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2016. 10. 13.
 * @version 1.0
 */
public class UserDto extends CommonDto implements Serializable {

    /** SID */
    private static final long serialVersionUID = 4567006840401302779L;

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
