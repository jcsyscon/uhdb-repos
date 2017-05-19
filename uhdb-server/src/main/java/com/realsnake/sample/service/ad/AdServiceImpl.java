/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 1.
 */
package com.realsnake.sample.service.ad;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.mapper.ad.AdMapper;
import com.realsnake.sample.mapper.common.CommonMapper;
import com.realsnake.sample.model.ad.AdDto;
import com.realsnake.sample.model.ad.AdVo;
import com.realsnake.sample.model.ad.ShopVo;
import com.realsnake.sample.model.ad.SponsorVo;
import com.realsnake.sample.model.common.AddressVo;
import com.realsnake.sample.model.common.AttachFileVo;
import com.realsnake.sample.model.common.Sort;
import com.realsnake.sample.service.common.CommonService;

/**
 * <pre>
 * Class Name : AdServiceImpl.java
 * Description : Description
 * </pre>
 *
 * @author 전강욱(realsnake1975@gmail.com)
 * @since 2017. 5. 1.
 * @version 1.0
 */
@Service
public class AdServiceImpl implements AdService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdMapper adMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private CommonService commonService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regSponsor(AdDto param, SponsorVo sponsor) throws Exception {
        sponsor.setRegUserSeq(param.getLoginUser().getSeq());
        this.adMapper.insertSponsor(sponsor);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifySponsor(AdDto param, SponsorVo sponsor) throws Exception {
        if ("Y".equalsIgnoreCase(sponsor.getDelYn())) {
            sponsor.setDelUserSeq(param.getLoginUser().getSeq());
            this.adMapper.deleteSponsor(sponsor);
        } else {
            sponsor.setModUserSeq(param.getLoginUser().getSeq());
            this.adMapper.updateSponsor(sponsor);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SponsorVo findSponsor(Integer param) throws Exception {
        return this.adMapper.selectSponsor(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SponsorVo> findSponsorList(AdDto param) throws Exception {
        // TODO: 페이징이 필요할 시 페이징 처리
        param.getPagingHelper().setEndNum(Integer.MAX_VALUE);
        Sort sort = new Sort();
        sort.setColumn("seq");
        sort.setAscOrDesc(CommonConstants.SortType.DESC.getValue());
        List<Sort> sortList = new ArrayList<Sort>();
        sortList.add(sort);
        param.getPagingHelper().setSortList(sortList);
        // TODO: 페이징이 필요할 시 페이징 처리

        param.getPagingHelper().setTotalRecordCount(this.adMapper.selectSponsorListCount(param));
        return this.adMapper.selectSponsorList(param);
    }



    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regShop(AdDto param, ShopVo shop, AddressVo address) throws Exception {
        Integer loginSeq = param.getLoginUser().getSeq();

        shop.setRegUserSeq(loginSeq);
        this.adMapper.insertShop(shop);

        Integer groupSeq = shop.getSeq();

        // <!-- 주소 저장
        address.setGubun(CommonConstants.AddressType.SHOP.getValue());
        address.setGroupSeq(groupSeq);
        // address.setZipCode(param.getZipCode());
        // address.setAddr1(param.getAddr1());
        // address.setAddr2(param.getAddr2());
        address.setRegUserSeq(loginSeq);
        this.commonService.regAddress(address);
        // 주소 저장 -->

        // <!-- 첨부파일 저장
        List<AttachFileVo> attachFileList = this.commonService.moveUploadedFile(param.getUploadedFiles(), groupSeq, CommonConstants.AttachFileFolderType.SHOP.getValue());

        if (attachFileList != null && !attachFileList.isEmpty()) {
            for (AttachFileVo attachFile : attachFileList) {
                attachFile.setGubun(CommonConstants.AttachFileFolderType.SHOP.getValue());
                attachFile.setGroupSeq(groupSeq);
                attachFile.setRegUserSeq(loginSeq);
                this.commonMapper.insertAttachFile(attachFile);
            }
        }
        // 첨부파일 저장 -->
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyShop(AdDto param, ShopVo shop, AddressVo address) throws Exception {
        Integer loginSeq = param.getLoginUser().getSeq();

        if ("Y".equalsIgnoreCase(shop.getDelYn())) {
            shop.setDelUserSeq(loginSeq);
            this.adMapper.deleteShop(shop);
        } else {
            shop.setModUserSeq(loginSeq);
            this.adMapper.updateShop(shop);

            Integer groupSeq = shop.getSeq();

            // <!-- 주소 수정
            address.setGubun(CommonConstants.AddressType.SHOP.getValue());
            address.setGroupSeq(groupSeq);
            // address.setZipCode(param.getZipCode());
            // address.setAddr1(param.getAddr1());
            // address.setAddr2(param.getAddr2());
            address.setRegUserSeq(loginSeq);
            address.setDelUserSeq(loginSeq);
            this.commonService.modifyAddress(address);
            // 주소 수정 -->

            // <!-- 첨부파일 저장
            List<AttachFileVo> attachFileList = this.commonService.moveUploadedFile(param.getUploadedFiles(), groupSeq, CommonConstants.AttachFileFolderType.SHOP.getValue());

            if (attachFileList != null && !attachFileList.isEmpty()) {
                for (AttachFileVo attachFile : attachFileList) {
                    attachFile.setGubun(CommonConstants.AttachFileFolderType.SHOP.getValue());
                    attachFile.setGroupSeq(groupSeq);
                    attachFile.setRegUserSeq(loginSeq);
                    this.commonMapper.insertAttachFile(attachFile);
                }
            }
            // 첨부파일 저장 -->
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ShopVo findShop(AdDto param, Integer shopSeq) throws Exception {
        ShopVo shop = this.adMapper.selectShop(shopSeq);
        // 스폰서 조회
        param.setSponsor(this.adMapper.selectSponsor(shop.getSponsorSeq()));

        // 주소 조회
        AddressVo address = new AddressVo();
        address.setGubun(CommonConstants.AddressType.SHOP.getValue());
        address.setGroupSeq(shopSeq);
        param.setAddressList(this.commonMapper.selectAddressList(address));

        // 첨부파일 조회
        AttachFileVo attachFile = new AttachFileVo();
        attachFile.setGubun(CommonConstants.AttachFileFolderType.SHOP.getValue());
        attachFile.setGroupSeq(shopSeq);
        param.setAttachFileList(this.commonMapper.selectAttachFileList(attachFile));

        return shop;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopVo> findShopList(AdDto param) throws Exception {
        // TODO: 페이징이 필요할 시 페이징 처리
        param.getPagingHelper().setEndNum(Integer.MAX_VALUE);
        Sort sort = new Sort();
        sort.setColumn("seq");
        sort.setAscOrDesc(CommonConstants.SortType.DESC.getValue());
        List<Sort> sortList = new ArrayList<Sort>();
        sortList.add(sort);
        param.getPagingHelper().setSortList(sortList);
        // TODO: 페이징이 필요할 시 페이징 처리

        param.getPagingHelper().setTotalRecordCount(this.adMapper.selectShopListCount(param));
        return this.adMapper.selectShopList(param);
    }



    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regAd(AdDto param, AdVo ad) throws Exception {
        Integer loginSeq = param.getLoginUser().getSeq();

        ad.setRegUserSeq(loginSeq);
        this.adMapper.insertAd(ad);

        Integer groupSeq = ad.getSeq();

        // <!-- 첨부파일 저장
        List<AttachFileVo> attachFileList = this.commonService.moveUploadedFile(param.getUploadedFiles(), groupSeq, CommonConstants.AttachFileFolderType.AD.getValue());

        if (attachFileList != null && !attachFileList.isEmpty()) {
            for (AttachFileVo attachFile : attachFileList) {
                attachFile.setGubun(CommonConstants.AttachFileFolderType.AD.getValue());
                attachFile.setGroupSeq(groupSeq);
                attachFile.setRegUserSeq(loginSeq);
                this.commonMapper.insertAttachFile(attachFile);
            }
        }
        // 첨부파일 저장 -->
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyAd(AdDto param, AdVo ad) throws Exception {
        Integer loginSeq = param.getLoginUser().getSeq();

        if ("Y".equalsIgnoreCase(ad.getDelYn())) {
            ad.setDelUserSeq(loginSeq);
            this.adMapper.deleteAd(ad);
        } else {
            ad.setModUserSeq(loginSeq);
            this.adMapper.updateAd(ad);

            Integer groupSeq = ad.getSeq();

            // <!-- 첨부파일 저장
            List<AttachFileVo> attachFileList = this.commonService.moveUploadedFile(param.getUploadedFiles(), groupSeq, CommonConstants.AttachFileFolderType.AD.getValue());

            if (attachFileList != null && !attachFileList.isEmpty()) {
                for (AttachFileVo attachFile : attachFileList) {
                    attachFile.setGubun(CommonConstants.AttachFileFolderType.AD.getValue());
                    attachFile.setGroupSeq(groupSeq);
                    attachFile.setRegUserSeq(loginSeq);
                    this.commonMapper.insertAttachFile(attachFile);
                }
            }
            // 첨부파일 저장 -->
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AdVo findAd(AdDto param, Integer adSeq) throws Exception {
        AdVo ad = this.adMapper.selectAd(adSeq);

        // 매장 조회
        ShopVo shop = this.adMapper.selectShop(ad.getShopSeq());
        param.setShop(shop);
        // 스폰서 조회
        param.setSponsor(this.adMapper.selectSponsor(shop.getSponsorSeq()));

        // 첨부파일 조회
        AttachFileVo attachFile = new AttachFileVo();
        attachFile.setGubun(CommonConstants.AttachFileFolderType.AD.getValue());
        attachFile.setGroupSeq(ad.getSeq());
        param.setAttachFileList(this.commonMapper.selectAttachFileList(attachFile));

        return ad;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdVo> findAdList(AdDto param) throws Exception {
        // TODO: 페이징이 필요할 시 페이징 처리
        param.getPagingHelper().setEndNum(Integer.MAX_VALUE);
        Sort sort = new Sort();
        sort.setColumn("seq");
        sort.setAscOrDesc(CommonConstants.SortType.DESC.getValue());
        List<Sort> sortList = new ArrayList<Sort>();
        sortList.add(sort);
        param.getPagingHelper().setSortList(sortList);
        // TODO: 페이징이 필요할 시 페이징 처리

        param.getPagingHelper().setTotalRecordCount(this.adMapper.selectAdListCount(param));
        return this.adMapper.selectAdList(param);
    }

}