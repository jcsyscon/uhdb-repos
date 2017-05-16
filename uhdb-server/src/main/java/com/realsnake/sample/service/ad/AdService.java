/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 1.
 */
package com.realsnake.sample.service.ad;

import java.util.List;

import com.realsnake.sample.model.ad.AdDto;
import com.realsnake.sample.model.ad.AdVo;
import com.realsnake.sample.model.ad.ShopVo;
import com.realsnake.sample.model.ad.SponsorVo;
import com.realsnake.sample.model.common.AddressVo;

/**
 * <pre>
 * Class Name : AdService.java
 * Description : Description
 * </pre>
 *
 * @author 전강욱(realsnake1975@gmail.com)
 * @since 2017. 5. 1.
 * @version 1.0
 */
public interface AdService {

    void regSponsor(AdDto param, SponsorVo sponsor) throws Exception;

    void modifySponsor(AdDto param, SponsorVo sponsor) throws Exception;

    SponsorVo findSponsor(Integer param) throws Exception;

    List<SponsorVo> findSponsorList(AdDto param) throws Exception;



    void regShop(AdDto param, ShopVo shop, AddressVo address) throws Exception;

    void modifyShop(AdDto param, ShopVo shop, AddressVo address) throws Exception;

    ShopVo findShop(AdDto param, Integer shopSeq) throws Exception;

    List<ShopVo> findShopList(AdDto param) throws Exception;



    void regAd(AdDto param, AdVo ad) throws Exception;

    void modifyAd(AdDto param, AdVo ad) throws Exception;

    AdVo findAd(AdDto param, Integer adSeq) throws Exception;

    List<AdVo> findAdList(AdDto param) throws Exception;

}
