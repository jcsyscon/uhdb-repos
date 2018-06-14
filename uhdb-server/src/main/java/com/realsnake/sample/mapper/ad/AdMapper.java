/**
 *
 */
package com.realsnake.sample.mapper.ad;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.realsnake.sample.model.ad.AdAptCtgrMappVo;
import com.realsnake.sample.model.ad.AdDto;
import com.realsnake.sample.model.ad.AdVo;
import com.realsnake.sample.model.ad.ShopVo;
import com.realsnake.sample.model.ad.SponsorVo;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Mapper class mapped db-table called sponsor, shop, ad
 */
@Mapper
public interface AdMapper {

    int insertSponsor(SponsorVo param);

    int updateSponsor(SponsorVo param);

    int deleteSponsor(SponsorVo param);

    SponsorVo selectSponsor(Integer param);

    int selectSponsorListCount(AdDto param);

    List<SponsorVo> selectSponsorList(AdDto param);



    int insertShop(ShopVo param);

    int updateShop(ShopVo param);

    int deleteShop(ShopVo param);

    ShopVo selectShop(Integer param);

    int selectShopListCount(AdDto param);

    List<ShopVo> selectShopList(AdDto param);



    int insertAd(AdVo param);

    int updateAd(AdVo param);

    int deleteAd(AdVo param);

    AdVo selectAd(Integer param);

    int selectAdListCount(AdDto param);

    List<AdVo> selectAdList(AdDto param);

    AdVo selectRandomAd(AdDto param);

    AdVo selectRandomAd4All(AdDto param);

    
    
    void insertAdAptCtgrMapp(AdAptCtgrMappVo adAptCtgrMapp);
    
}
