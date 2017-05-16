/**
 *
 */
package com.realsnake.sample.model.sample;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Domain class mapped db-table called sample
 */
@Alias(value = "SampleVo")
public class SampleVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = -6124430220361780470L;

    /** 일련번호 */
    private Integer id;

    /** 이름 */
    private String name;

    /** 등록일시 */
    private Date regDate;

    public Integer getId() {
        return id;
    }
    // public void setId(Integer id) {
    // this.id = id;
    // }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
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
