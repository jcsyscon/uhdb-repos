/**
 * Copyright (c) 2016 realsnake1975@gmail.com
 *
 * 2016. 9. 22.
 */
package com.realsnake.sample.model.common;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.realsnake.sample.model.ad.AdVo;
import com.realsnake.sample.model.ad.ShopVo;

/**
 * <pre>
 * Class Name : SimpleAd.java
 * Description : 간단 광고
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2016. 9. 22.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2016. 9. 22.
 * @version 1.0
 */
public class SimpleAd implements Serializable {

    /** SID */
    private static final long serialVersionUID = 6812930664988898966L;

    private String title;

    private String copy;

    private String tel;

    private String imageUrl;

    private String link;

    public SimpleAd(AdVo ad, ShopVo shop, AttachFileVo attachFile) {
        super();

        this.title = ad.getTitle();
        this.copy = ad.getCopy();
        this.tel = shop.getTel();
        this.imageUrl = "/common/file/" + attachFile.getFileSeq();
        this.link = ad.getTargetUrl();
    }

    public String getTitle() {
        return title;
    }

    public String getCopy() {
        return copy;
    }

    public String getTel() {
        return tel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCopy(String copy) {
        this.copy = copy;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLink(String link) {
        this.link = link;
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
