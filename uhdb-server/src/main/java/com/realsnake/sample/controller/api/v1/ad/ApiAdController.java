package com.realsnake.sample.controller.api.v1.ad;

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

@RestController("ApiV1AdController")
@RequestMapping(value = "/api/v1/ad")
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

}
