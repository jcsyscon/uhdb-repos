/**
 * 
 */
package com.realsnake.sample.model.ad;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 * Class Name : AdCtgrVo.java
 * Description : Description
 * </pre>
 *
 * @author home
 * @since 2018. 6. 15.
 * @version 1.0
 */
@Alias(value = "AdCtgrVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdCtgrVo implements Serializable {

	/** SID */
	private static final long serialVersionUID = 2390864033027022208L;

	/** 광고 일련번호 */
	private Integer adSeq;
	/** 광고 타이틀 */
	private String adTitle;
	/** 매장명 */
	private String shopName;
	/** 매장 전화번호 */
	private String shopTel;
	
	private String targetAptId;
	private String adCtgrCode;
	
	public Integer getAdSeq() {
		return adSeq;
	}
	
	public void setAdSeq(Integer adSeq) {
		this.adSeq = adSeq;
	}
	
	public String getAdTitle() {
		return adTitle;
	}
	
	public void setAdTitle(String adTitle) {
		this.adTitle = adTitle;
	}
	
	public String getShopName() {
		return shopName;
	}
	
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	public String getShopTel() {
		return shopTel;
	}
	
	public void setShopTel(String shopTel) {
		this.shopTel = shopTel;
	}

    public String getTargetAptId() {
		return targetAptId;
	}

	public void setTargetAptId(String targetAptId) {
		this.targetAptId = targetAptId;
	}

	public String getAdCtgrCode() {
		return adCtgrCode;
	}

	public void setAdCtgrCode(String adCtgrCode) {
		this.adCtgrCode = adCtgrCode;
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
