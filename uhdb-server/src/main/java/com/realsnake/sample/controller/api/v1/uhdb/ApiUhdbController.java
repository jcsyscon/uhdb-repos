package com.realsnake.sample.controller.api.v1.uhdb;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realsnake.sample.constants.ApiResultCode;
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.common.api.ApiResponse;
import com.realsnake.sample.model.uhdb.AptVo;
import com.realsnake.sample.model.uhdb.NfcVo;
import com.realsnake.sample.model.uhdb.UhdbDto;
import com.realsnake.sample.model.uhdb.UhdbLogVo;
import com.realsnake.sample.model.uhdb.UhdbVo;
import com.realsnake.sample.service.uhdb.UhdbService;
import com.realsnake.sample.util.MobilePagingHelper;

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
    public ApiResponse<?> searchUhdb(@PathVariable("aptId") String aptId, @PathVariable("uhdbId") String uhdbId) throws CommonApiException {
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
        try {
            param.setSafeFunc(safeFunc);

            this.uhdbService.modifyUhdbLog(param);
            return "OK";
        } catch (Exception e) {
            LOGGER.error("<<modifyUhdbLog, 무인택배함 사용 기록 수정 중 오류>>", e);
            return String.format("NOK, %s", e.getMessage());
        }
    }

    @PostMapping(value = "/public-ip/modify")
    public String modifyUhdbPublicIp(UhdbVo param) {
        String result = "NOK";

        try {
            this.uhdbService.modifyUhdbGonginIp(param);
            result = "OK";
        } catch (Exception e) {
            LOGGER.error("<<modifyUhdbPublicIp, 무인택배함 공인아이피 수정 중 오류>>", e);
        }

        return result;
    }

    /**
     * 무인택배함 보관함 열기(앱)
     *
     * @param aptId
     * @param uhdbId
     * @param boxNo
     * @param password
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/open")
    public ApiResponse<?> openBox(String aptId, String uhdbId, String boxNo, String password) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<무인택배함 보관함 열기(/uhdb/open)>> 인증 실패");
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

    /**
     * 무인택배함 보관함 열기(관리자웹)
     *
     * @param param (aptId, aptPosi, boxNo)
     * @return
     */
    @PostMapping(value = "/admin/open")
    public String openBox4Admin(UhdbLogVo param) {
        String result = "NOK";

        try {
            result = this.uhdbService.openBox(param);
        } catch (Exception e) {
            LOGGER.error("<<openBox4Admin, 관리자 무인택배함 보관함 열기 중 오류>>", e);
        }

        return result;
    }

    /**
     * 무인택배함 보관함 초기화(앱에서 보관함 열기 중 닫기/확인/고객센터 버튼 클릭 시)
     *
     * @param aptId
     * @param uhdbId
     * @param boxNo
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/init")
    public ApiResponse<?> initBox(String aptId, String uhdbId, String boxNo) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<무인택배함 보관함 초기화(/uhdb/init)>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            UhdbLogVo param = new UhdbLogVo();
            param.setAptId(aptId);
            param.setAptPosi(uhdbId);
            param.setBoxNo(boxNo);
            String result = this.uhdbService.initBox(param);

            ApiResponse<String> apiResponse = new ApiResponse<>();
            apiResponse.setBody(result);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    @GetMapping(value = "/password")
    public String findUhdbUserPassword(UhdbLogVo param) {
        String result = "NOK";

        try {
            result = this.uhdbService.findUhdbUserPassword(param);
        } catch (Exception e) {
            LOGGER.error("<<findUhdbUserPassword, 무인택배함 세대 비밀번호 조회 중 오류>>", e);
        }

        return result;
    }

    @PostMapping(value = "/password")
    public String modifyUhdbUserPassword(UhdbLogVo param) {
        String result = "NOK";

        try {
            this.uhdbService.modifyUhdbUserPassword(param);
            result = "OK";
        } catch (Exception e) {
            LOGGER.error("<<findUhdbUserPassword, 무인택배함 사용자 비밀번호 수정 중 오류>>", e);
        }

        return result;
    }

    @PostMapping(value = "/nfc/reg")
    public String regNfc(NfcVo param) {
        String result = "NOK";

        try {
            this.uhdbService.regNfc(param);
            result = "OK";
        } catch (Exception e) {
            LOGGER.error("<<regNfc, 무인택배함 NFC 등록 중 오류>>", e);
        }

        return result;
    }

    @PostMapping(value = "/nfc/remove")
    public String removeNfc(NfcVo param) {
        String result = "NOK";

        try {
            this.uhdbService.removeNfc(param);
            result = "OK";
        } catch (Exception e) {
            LOGGER.error("<<regNfc, 무인택배함 NFC 삭제 중 오류>>", e);
        }

        return result;
    }

    /**
     * 택배함 보관함 사용내역 조회(앱)
     *
     * @param gubun past / now
     * @param seq
     * @param mobilePagingHelper
     * @param param
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/log/{gubun}/{userSeq}")
    public ApiResponse<?> searchUhdbLogList(@PathVariable("gubun") String gubun, @PathVariable("userSeq") Integer userSeq, MobilePagingHelper mobilePagingHelper, UhdbDto param)
            throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<무인택배함 사용내역 조회(/log/{}/{})>> 인증 실패", gubun, userSeq);
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            param.setUserSeq(userSeq);
            param.setGubun(gubun);

            List<UhdbLogVo> uhdbLogList = this.uhdbService.findUhdbLogList4Mobile(param);

            ApiResponse<List<UhdbLogVo>> apiResponse = new ApiResponse<>(mobilePagingHelper.getNextPageToken(), mobilePagingHelper.getPageSize());
            apiResponse.setBody(uhdbLogList);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 장기보관 알림 발송
     *
     * @param aptId
     * @param aptPosi
     * @param boxNo
     * @param gubun onlyPush / pushAndSms
     * @return
     */
    @PostMapping(value = "/long-box/alarm")
    public String sendAlarmLongBox(String aptId, String aptPosi, String boxNo, String gubun) {
        String result = "NOK";

        try {
            UhdbDto param = new UhdbDto();
            param.setAptId(aptId);
            param.setAptPosi(aptPosi);
            param.setBoxNo(boxNo);
            param.setGubun(gubun);

            this.uhdbService.sendAlarm4LongBox(param);
            result = "OK";
        } catch (Exception e) {
            LOGGER.error("<<sendAlarmLongBox, 무인택배함 장기보관 알림 처리 중 오류>>", e);
        }

        return result;
    }

    /**
     * 무인택배함 보관함 복구(관리자웹)
     *
     * @param param (aptId, aptPosi, boxNo, userId)
     * @param updateDatetime yyyyMMddHHmmss
     * @return
     */
    @PostMapping(value = "/restore")
    public String restoreBox(UhdbLogVo param, @RequestParam(value = "updateDatetime", required = false, defaultValue = StringUtils.EMPTY) String updateDatetime) {
        String result = "NOK";

        try {
            this.uhdbService.restoreBox(param, updateDatetime);
            result = "OK";
        } catch (Exception e) {
            LOGGER.error("<<restoreBox, 무인택배함 보관함 복구(관리자웹) 중 오류>>", e);
        }

        return result;
    }

}
