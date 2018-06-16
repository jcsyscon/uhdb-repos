/**
 *
 */
package com.realsnake.sample.model.uhdb;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Domain class mapped db-table called tb_ap0102
 */
@Alias(value = "UhdbVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UhdbVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = 6429708273183823755L;

    private String aptId;
    private String aptPosi;
    private String aptPosiNm;
    @JsonIgnore
    private String routerIp;
    @JsonIgnore
    private String routerId;
    @JsonIgnore
    private String gonginIp;
    @JsonIgnore
    private String portFw;

    public String getAptId() {
        return aptId;
    }

    public String getAptPosi() {
        return aptPosi;
    }

    public String getAptPosiNm() {
        return aptPosiNm;
    }

    public String getRouterIp() {
        return routerIp;
    }

    public String getRouterId() {
        return routerId;
    }

    public String getGonginIp() {
        return gonginIp;
    }

    public String getPortFw() {
        return portFw;
    }

    public void setAptId(String aptId) {
        this.aptId = aptId;
    }

    public void setAptPosi(String aptPosi) {
        this.aptPosi = aptPosi;
    }

    public void setAptPosiNm(String aptPosiNm) {
        this.aptPosiNm = aptPosiNm;
    }

    public void setRouterIp(String routerIp) {
        this.routerIp = routerIp;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public void setGonginIp(String gonginIp) {
        this.gonginIp = gonginIp;
    }

    public void setPortFw(String portFw) {
        this.portFw = portFw;
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
