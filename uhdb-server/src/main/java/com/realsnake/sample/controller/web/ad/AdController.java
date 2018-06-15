package com.realsnake.sample.controller.web.ad;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.realsnake.sample.model.ad.AdAptCtgrMappVo;
import com.realsnake.sample.model.ad.AdDto;
import com.realsnake.sample.model.ad.AdVo;
import com.realsnake.sample.model.ad.ShopVo;
import com.realsnake.sample.model.ad.SponsorVo;
import com.realsnake.sample.model.common.AddressVo;
import com.realsnake.sample.service.ad.AdService;
import com.realsnake.sample.service.common.CommonService;
import com.realsnake.sample.util.PagingHelper;

@Controller
@RequestMapping(value = "/ad")
public class AdController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdController.class);

    @Autowired
    private AdService adService;

    @Autowired
    private CommonService commonService;

    @GetMapping
    public String index() throws Exception {
        // return "index";
        return "redirect:/ad/list";
    }

    @GetMapping(value = "/list")
    public String moveAdListPage(Model model, PagingHelper pagingHelper, AdDto param) throws Exception {
        LOGGER.debug("<<AdDto>> {}", param.toString());

        List<AdVo> adList = this.adService.findAdList(param);
        model.addAttribute("adList", adList);

        return "ad/list";
    }

    @GetMapping(value = "/reg")
    public String moveAdRegPage(Model model, AdDto param, Integer shopSeq) throws Exception {
        model.addAttribute("shop", this.adService.findShop(param, shopSeq));
        model.addAttribute("sponsor", param.getSponsor());
        model.addAttribute("sidoList", this.commonService.findSidoList());
        return "ad/reg";
    }

    @PostMapping(value = "/reg")
    public String regAd(HttpServletRequest request, AdDto param, AdVo ad, String[] targetAptIds) throws Exception {
        // 아파트-광고 카테고리 매핑 처리
        List<AdAptCtgrMappVo> adAptCtgrMappList = null;

        if (targetAptIds != null && targetAptIds.length > 0) {
            adAptCtgrMappList = new ArrayList<>();

            for (String targetAptId : targetAptIds) {
                LOGGER.debug("targetAptId: {}", targetAptId);

                String[] categoryCodes = request.getParameterValues("adCategoryCodes-" + targetAptId);

                for (String categoryCode : categoryCodes) {
                    if (!"00".equals(categoryCode)) {
                        LOGGER.debug("categoryCode: {}", categoryCode);

                        AdAptCtgrMappVo adAptCtgrMapp = new AdAptCtgrMappVo();
                        adAptCtgrMapp.setTargetAptId(targetAptId);
                        adAptCtgrMapp.setAdCtgrCode(categoryCode);

                        LOGGER.debug("adAptCtgrMapp: {}", adAptCtgrMapp.toString());

                        adAptCtgrMappList.add(adAptCtgrMapp);
                    }
                }
            }
        }

        this.adService.regAd(param, ad, adAptCtgrMappList);
        return "redirect:/ad/list";
    }

    @GetMapping(value = "/{seq}")
    public String moveAdViewPage(Model model, @PathVariable("seq") Integer seq, AdDto param) throws Exception {
        model.addAttribute("ad", this.adService.findAd(param, seq));
        model.addAttribute("sponsor", param.getSponsor());
        model.addAttribute("shop", param.getShop());
        model.addAttribute("attachFileList", param.getAttachFileList());
        model.addAttribute("sidoList", this.commonService.findSidoList());

        model.addAttribute("adAptCtgrMappList", param.getAdAptCtgrMappList());

        return "ad/view";
    }

    @PostMapping(value = "/modify/{seq}")
    public String modifyAd(HttpServletRequest request, @PathVariable("seq") Integer seq, AdDto param, AdVo ad, String[] targetAptIds) throws Exception {
        LOGGER.debug("<<AdDto>> {}", param.toString());

        // 아파트-광고 카테고리 매핑 처리
        List<AdAptCtgrMappVo> adAptCtgrMappList = null;

        if (targetAptIds != null && targetAptIds.length > 0) {
            adAptCtgrMappList = new ArrayList<>();

            for (String targetAptId : targetAptIds) {
                LOGGER.debug("targetAptId: {}", targetAptId);

                String[] categoryCodes = request.getParameterValues("adCategoryCodes-" + targetAptId);

                for (String categoryCode : categoryCodes) {
                    if (!"00".equals(categoryCode)) {
                        LOGGER.debug("categoryCode: {}", categoryCode);

                        AdAptCtgrMappVo adAptCtgrMapp = new AdAptCtgrMappVo();
                        adAptCtgrMapp.setTargetAptId(targetAptId);
                        adAptCtgrMapp.setAdCtgrCode(categoryCode);

                        LOGGER.debug("adAptCtgrMapp: {}", adAptCtgrMapp.toString());

                        adAptCtgrMappList.add(adAptCtgrMapp);
                    }
                }
            }
        }

        this.adService.modifyAd(param, ad, adAptCtgrMappList);
        return "redirect:/ad/list";
    }



    @GetMapping(value = "/sponsor/list")
    public String moveSponsorListPage(Model model, PagingHelper pagingHelper, AdDto param) throws Exception {
        LOGGER.debug("<<AdDto>> {}", param.toString());

        List<SponsorVo> sponsorList = this.adService.findSponsorList(param);
        model.addAttribute("sponsorList", sponsorList);

        return "ad/sponsor/list";
    }

    @GetMapping(value = "/sponsor/reg")
    public String moveSponsorRegPage() throws Exception {
        return "ad/sponsor/reg";
    }

    @PostMapping(value = "/sponsor/reg")
    public String regSponsor(AdDto param, SponsorVo sponsor) throws Exception {
        this.adService.regSponsor(param, sponsor);
        return "redirect:/ad/sponsor/list";
    }

    @GetMapping(value = "/sponsor/{seq}")
    public String moveSponsorViewPage(Model model, @PathVariable("seq") Integer seq) throws Exception {
        SponsorVo sponsor = this.adService.findSponsor(seq);
        model.addAttribute("sponsor", sponsor);
        return "ad/sponsor/view";
    }

    @PostMapping(value = "/sponsor/modify/{seq}")
    public String modifySponsor(@PathVariable("seq") Integer seq, AdDto param, SponsorVo sponsor) throws Exception {
        this.adService.modifySponsor(param, sponsor);
        return "redirect:/ad/sponsor/list";
    }



    @GetMapping(value = "/shop/list")
    public String moveShopListPage(Model model, PagingHelper pagingHelper, AdDto param) throws Exception {
        LOGGER.debug("<<AdDto>> {}", param.toString());

        List<ShopVo> shopList = this.adService.findShopList(param);
        model.addAttribute("shopList", shopList);

        return "ad/shop/list";
    }

    @GetMapping(value = "/shop/reg")
    public String moveShopRegPage(Model model, Integer sponsorSeq) throws Exception {
        model.addAttribute("sponsor", this.adService.findSponsor(sponsorSeq));
        return "ad/shop/reg";
    }

    @PostMapping(value = "/shop/reg")
    public String regShop(AdDto param, ShopVo shop, AddressVo address) throws Exception {
        this.adService.regShop(param, shop, address);
        return "redirect:/ad/shop/list";
    }

    @GetMapping(value = "/shop/{seq}")
    public String moveShopViewPage(Model model, @PathVariable("seq") Integer seq, AdDto param) throws Exception {
        model.addAttribute("shop", this.adService.findShop(param, seq));
        model.addAttribute("sponsor", param.getSponsor());
        model.addAttribute("address", (param.getAddressList() == null || param.getAddressList().isEmpty()) ? null : param.getAddressList().get(0));
        model.addAttribute("attachFileList", param.getAttachFileList());
        return "ad/shop/view";
    }

    @PostMapping(value = "/shop/modify/{seq}")
    public String modifyShop(@PathVariable("seq") Integer seq, AdDto param, ShopVo shop, AddressVo address) throws Exception {
        this.adService.modifyShop(param, shop, address);
        return "redirect:/ad/shop/list";
    }

}
