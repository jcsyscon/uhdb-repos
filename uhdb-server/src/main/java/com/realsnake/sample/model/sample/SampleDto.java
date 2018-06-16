/**
 *
 */
package com.realsnake.sample.model.sample;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import com.realsnake.sample.model.common.CommonDto;

/**
 * @author 전강욱(realsnake1975@gmail.com)
 */
@Alias(value = "SampleDto")
public class SampleDto extends CommonDto implements Serializable {

    /** SID */
    private static final long serialVersionUID = -112359107292528664L;

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
