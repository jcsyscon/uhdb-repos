package com.realsnake.sample.controller.api.v1.uhdb;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realsnake.sample.constants.ApiResultCode;
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.common.api.ApiResponse;
import com.realsnake.sample.model.uhdb.AptVo;
import com.realsnake.sample.model.uhdb.UhdbLogVo;
import com.realsnake.sample.model.uhdb.UhdbVo;
import com.realsnake.sample.service.uhdb.UhdbService;

@RestController("ApiV1UhdbController")
@RequestMapping(value = "/api/v1/uhdb")
public class ApiUhdbController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUhdbController.class);

    @Autowired
    private UhdbService uhdbService;

    /**
     * 아파트 조회
     *
     * @param aptId
     * @return
     * @throws CommonApiException
     */
    @GetMapping(value = "/apt/{aptId}")
    public ApiResponse<?> getApt(@PathVariable("aptId") String aptId) throws CommonApiException {
        try {
            AptVo param = new AptVo();
            param.setAptId(aptId);

            List<AptVo> aptList = this.uhdbService.findAptList(param);
            AptVo apt = null;
            if (aptList != null && !aptList.isEmpty()) {
                apt = aptList.get(0);
            }

            ApiResponse<AptVo> apiResponse = new ApiResponse<>();
            apiResponse.setBody(apt);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 택배함 조회
     *
     * @param aptId
     * @param uhdbId
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/{aptId}/{uhdbId}")
    public ApiResponse<?> searchUhdb(@PathVariable("aptId") String aptId, @PathVariable("aptId") String uhdbId) throws CommonApiException {
        try {
            UhdbVo param = new UhdbVo();
            param.setAptId(aptId);
            param.setAptPosi(uhdbId);

            List<UhdbVo> uhdbList = this.uhdbService.findUhdbList(param);
            UhdbVo uhdb = null;
            if (uhdbList != null && !uhdbList.isEmpty()) {
                uhdb = uhdbList.get(0);
            }

            ApiResponse<UhdbVo> apiResponse = new ApiResponse<>();
            apiResponse.setBody(uhdb);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    @PostMapping(value = "/modify/{safeFunc}")
    public String modifyUhdbLog(@PathVariable("safeFunc") String safeFunc, UhdbLogVo param) {
        String result = "NOK";

        try {
            /**
             * Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
             *
             * if (authentication == null) { LOGGER.debug("<<ApiNoticeController.getNoticeList>> 인증 실패"); throw new CommonApiException(ApiResultCode.NOTFOUND_USER); }
             */

            param.setSafeFunc(safeFunc);

            this.uhdbService.modifyUhdbLog(param);
            result = "OK";
        } catch (Exception e) {
            LOGGER.error("<<modifyUhdbLog, 무인택배함 사용 기록 수정 중 오류>>", e);
        }

        return result;
    }

    /**
     * 무인택배함 락커 열기
     *
     * @param aptId
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/open")
    public ApiResponse<?> openBox(String aptId, String uhdbId, String boxNo, String password) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<무인택배함 락커 열기(/uhdb/open)>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            UhdbLogVo param = new UhdbLogVo();
            param.setAptId(aptId);
            param.setAptPosi(uhdbId);
            param.setBoxNo(boxNo);
            param.setPswd(password);
            String result = this.uhdbService.openBox(param);

            ApiResponse<String> apiResponse = new ApiResponse<>();
            apiResponse.setBody(result);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

}
