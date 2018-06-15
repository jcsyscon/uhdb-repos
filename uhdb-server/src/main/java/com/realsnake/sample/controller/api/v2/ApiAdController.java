package com.realsnake.sample.controller.api.v2;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realsnake.sample.constants.ApiResultCode;
import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.ad.AdDto;
import com.realsnake.sample.model.ad.AdVo;
import com.realsnake.sample.model.common.AttachFileVo;
import com.realsnake.sample.model.common.SimpleAd;
import com.realsnake.sample.model.common.api.ApiResponse;
import com.realsnake.sample.service.ad.AdService;
import com.realsnake.sample.util.MobilePagingHelper;

@RestController("ApiV2AdController")
@RequestMapping(value = "/api/v2/ad")
public class ApiAdController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAdController.class);

    @Autowired
    private AdService adService;

    /**
     * 광고 조회
     *
     * @param seq
     * @return
     * @throws CommonApiException
     */
    @GetMapping(value = "/{seq}")
    public ApiResponse<?> getAd(@PathVariable("seq") Integer seq, AdDto param) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiAdController.getAd>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            AdVo ad = this.adService.findAd(param, seq);

            List<AttachFileVo> attachFileList = param.getAttachFileList();
            AttachFileVo attachFileParam = null;
            for (AttachFileVo attachFile : attachFileList) {
                if (CommonConstants.AdImageType.PUSH.getValue().equals(attachFile.getSubGubun())) { // 일단 기본은 푸시 이미지
                    attachFileParam = attachFile;
                }
            }

            SimpleAd simpleAd = new SimpleAd(ad, param.getShop(), attachFileParam);

            ApiResponse<SimpleAd> apiResponse = new ApiResponse<>();
            apiResponse.setBody(simpleAd);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 이미지 별(시작(start)/종료(end)/배너(banner)/팝업(popup)/푸시(push)) 광고 조회
     *
     * @param gubun
     * @return
     * @throws CommonApiException
     */
    @GetMapping(value = "/gubun/{gubun}")
    public ApiResponse<?> getAdPerGubun(@PathVariable("gubun") String gubun, AdDto param) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiAdController.getAdPerGubun>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            AdVo ad = this.adService.findRandomAd(param);

            List<AttachFileVo> attachFileList = param.getAttachFileList();
            AttachFileVo attachFileParam = null;
            for (AttachFileVo attachFile : attachFileList) {
                if (gubun.equals(attachFile.getSubGubun())) {
                    attachFileParam = attachFile;
                }
            }

            SimpleAd simpleAd = new SimpleAd(ad, param.getShop(), attachFileParam);

            ApiResponse<SimpleAd> apiResponse = new ApiResponse<>();
            apiResponse.setBody(simpleAd);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }
    
    /**
     * 광고 카테고리 목록
     */
    @GetMapping(value = "/category/list")
    public ApiResponse<?> findCategoryList() throws CommonApiException {
    	try {
    		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

             if (authentication == null) {
                 LOGGER.debug("<<ApiAdController.getAd>> 인증 실패");
                 throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
             }
             
    		String[][] categories = {{"01", "Best20"}, {"02", "음식"}, {"03", "학원"}, {"04", "스포츠"}, {"05", "미용/뷰티"}, {"06", "생활편의"}};

            List<Map<String, Object>> categoryList = new ArrayList<>();
            for (String[] category : categories) {
                Map<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("categoryCode", category[0]);
                categoryMap.put("categoryName", category[1]);
                categoryList.add(categoryMap);
            }
    		
    		ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();
            apiResponse.setBody(categoryMapList);

            return apiResponse;
    	} catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 광고 목록 조회
     */
    @GetMapping(value = "/list")
    public ApiResponse<?> findAdList(MobilePagingHelper mobilePagingHelper, AdDto param, String categoryCode) throws CommonApiException {
    	try {
    		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

             if (authentication == null) {
                 LOGGER.debug("<<ApiAdController.getAd>> 인증 실패");
                 throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
             }
             
             List<AdCtgrVo> adCtgrList = this.adService.findAdList(param, categoryCode);
             
             List<Map<String, Object>> adCtgrList = new ArrayList<>();
             for (AdCtgrVo adCtgr : adCtgrList) {
            	 Map<String, Object> adCtgrMap = new HashMap<>();
            	 adCtgrMap.put("adSeq", adCtgr.getAdSeq());
            	 adCtgrMap.put("adTitle", adCtgr.getAdTitle());
            	 adCtgrMap.put("shopName", adCtgr.getShopName());
            	 adCtgrMap.put("shopTel", adCtgr.getShopTel());
            	 adCtgrList.add();
             }
             
             Map<String, Object> bodyMap = new HashMap<>();
             bodyMap.put("adCtgrList", adCtgrList);
             
             ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>(mobilePagingHelper.getNextPageToken(), mobilePagingHelper.getPageSize());
             apiResponse.setBody(categoryMapList);

             return apiResponse;
    	} catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }
}
