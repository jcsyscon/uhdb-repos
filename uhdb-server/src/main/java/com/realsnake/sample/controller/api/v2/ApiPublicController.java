package com.realsnake.sample.controller.api.v2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realsnake.sample.constants.ApiResultCode;
import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.common.SendVo;
import com.realsnake.sample.model.common.api.ApiResponse;
import com.realsnake.sample.model.common.api.ApiResponseHeader;
import com.realsnake.sample.model.uhdb.UhdbVo;
import com.realsnake.sample.model.user.UserFcmVo;
import com.realsnake.sample.model.user.UserUhdbVo;
import com.realsnake.sample.model.user.UserVo;
import com.realsnake.sample.service.common.CommonService;
import com.realsnake.sample.service.uhdb.UhdbService;
import com.realsnake.sample.service.user.UserService;
import com.realsnake.sample.util.RandomKeys;
import com.realsnake.sample.util.SmsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="인증이 필요없는 API들", description="인증이 필요없는 API들")
@RestController("ApiV2PublicController")
@RequestMapping(value = "/api/v2/public")
public class ApiPublicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiPublicController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UhdbService uhdbService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private SmsUtils smsUtils;

    /**
     * 핸드폰번호 중복체크(UNKNOWN/NONEXIST/EXIST)
     *
     * @param mobileNo 핸드폰번호(포맷: 010-XXXX-XXXX)
     * @return
     */
    @ApiOperation(value = "핸드폰번호 중복체크(UNKNOWN/NONEXIST/EXIST)", response = ApiResponse.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "mobileNo", value = "핸드폰번호", required = true, dataType = "string", paramType = "query", defaultValue = "")
    })
    @PostMapping(value = "/double-check")
    public ApiResponse<?> doubleCheckProcessing(String mobileNo) {
        ApiResponse<String> apiResponse = new ApiResponse<>();

        try {
            String result = this.userService.checkDoubleUser("mobile", mobileNo); // UNKNOWN/NONEXIST/EXIST
            apiResponse.setBody(result);
        } catch (Exception e) {
            LOGGER.error("[핸드폰번호 중복체크 API 오류]", e);

            ApiResponseHeader header = new ApiResponseHeader();
            header.setResultCode(ApiResultCode.COMMON_FAIL.getCode());
            header.setResultMessage(e.getMessage());
            apiResponse.setHeader(header);
        }

        return apiResponse;
    }

    /**
     * 모바일 인증번호 발송
     *
     * @param mobileNo 핸드폰번호
     * @return
     */
    @ApiOperation(value = "모바일 인증번호 발송", response = ApiResponse.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "mobileNo", value = "핸드폰번호", required = true, dataType = "string", paramType = "query", defaultValue = "")
    })
    @PostMapping(value = "/mobile-auth-num")
    public ApiResponse<?> getMobileAuthNum(String mobileNo) {
        ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>();

        try {
            String code = String.format("%06d", (int) (Math.random() * 1000000));
            String randomKey = RandomKeys.make(32);

            Map<String, String> codeAndKey = new HashMap<String, String>();
            codeAndKey.put("code", code);
            codeAndKey.put("key", randomKey);

            // SMS 발송
            String message = String.format("모바일 인증 번호는 [%s]입니다.", code);
            CompletableFuture<String> result = this.smsUtils.send(mobileNo, message);
            // 발송 로그 저장
            SendVo send = new SendVo();
            send.setGubun(CommonConstants.SendType.SMS_AUTH_NUMBER.getValue());
            send.setMobile(mobileNo);
            send.setSendMessage(message);
            send.setResultMessage(result.get());
            send.setEtc(randomKey);
            this.commonService.regSendLog(send);

            apiResponse.setBody(codeAndKey);
        } catch (Exception e) {
            LOGGER.error("[모바일 인증번호 발송요청 API 오류]", e);

            ApiResponseHeader header = new ApiResponseHeader();
            header.setResultCode(ApiResultCode.COMMON_FAIL.getCode());
            header.setResultMessage(e.getMessage());
            apiResponse.setHeader(header);
        }

        return apiResponse;
    }

    /**
     * 모바일 인증번호 검증
     *
     * @param code 6자리 인증번호
     * @param key 32자리 랜덤키
     * @return
     */
    @ApiOperation(value = "모바일 인증번호 검증", response = ApiResponse.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "code", value = "6자리 인증번호", required = true, dataType = "string", paramType = "query", defaultValue = "")
        , @ApiImplicitParam(name = "key", value = "32자리 랜덤키", required = true, dataType = "string", paramType = "query", defaultValue = "")
    })
    @PostMapping(value = "/mobile-auth-num/check")
    public ApiResponse<?> checkMobileAuthNum(String code, String key) {
        ApiResponse<String> apiResponse = new ApiResponse<>();

        try {
            if (this.commonService.compareAuthMobileCode(code, key)) {
                apiResponse.setBody("OK");
            } else {
                apiResponse.setBody("NOK");
            }
        } catch (Exception e) {
            LOGGER.error("[모바일 인증번호 검증 API 오류]", e);

            ApiResponseHeader header = new ApiResponseHeader();
            header.setResultCode(ApiResultCode.COMMON_FAIL.getCode());
            header.setResultMessage(e.getMessage());
            apiResponse.setHeader(header);
        }

        return apiResponse;
    }

    /**
     * 아파트아이디로 무인택배함 조회
     *
     * @param aptId
     * @return
     * @throws CommonApiException
     */
    @ApiOperation(value = "아파트아이디로 무인택배함 목록 조회", response = ApiResponse.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "aptId", value = "아파트아이디", required = true, dataType = "string", paramType = "query", defaultValue = "")
    })
    @GetMapping(value = "/search-uhdb")
    public ApiResponse<?> searchUhdbByAptId(String aptId) throws CommonApiException {
        try {
            UhdbVo param = new UhdbVo();
            param.setAptId(aptId);

            List<UhdbVo> uhdbList = this.uhdbService.findUhdbList(param);

            ApiResponse<List<UhdbVo>> apiResponse = new ApiResponse<>();
            apiResponse.setBody(uhdbList);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 회원정보 등록
     *
     * @param mobileNo 핸드폰번호
     * @param uhdbNo 무인택배함번호(아파트아이디-아파트위치)
     * @param fcmToken FCM토큰
     * @param appVersion 앱버전
     * @param deviceType 디바이스유형(android/ios/none)
     * @return
     */
    @ApiOperation(value = "회원정보 등록", response = ApiResponse.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "mobileNo", value = "핸드폰번호", required = true, dataType = "string", paramType = "query", defaultValue = "")
        , @ApiImplicitParam(name = "uhdbNo", value = "무인택배함번호(아파트아이디-아파트위치)", required = true, dataType = "string", paramType = "query", defaultValue = "")
        , @ApiImplicitParam(name = "fcmToken", value = "FCM토큰", required = true, dataType = "string", paramType = "query", defaultValue = "")
        , @ApiImplicitParam(name = "appVersion", value = "앱버전", required = true, dataType = "string", paramType = "query", defaultValue = "")
        , @ApiImplicitParam(name = "deviceType", value = "디바이스유형(android/ios/none)", required = true, dataType = "string", paramType = "query", defaultValue = "")
    })
    @PostMapping(value = "/join")
    public ApiResponse<?> regUser(String mobileNo, String uhdbNo, String fcmToken, String appVersion, String deviceType) {
        ApiResponse<UserVo> apiResponse = new ApiResponse<>();

        try {
            UserVo userParam = new UserVo();
            userParam.setUsername(mobileNo); // 암호화안함
            userParam.setName(mobileNo); // 이름 핸드폰번호로, 아래 서비스 단에서 암호화
            userParam.setPassword(mobileNo); // 핸드폰번호를 해싱 처리, 아래 서비스 단에서 해싱 처리
            userParam.setMobile(mobileNo); // 아래 서비스 단에서 암호화

            String[] temps = uhdbNo.split("-", -1);
            String aptId = temps[0];
            String aptPosi = temps[1];
            
            UserUhdbVo userUhdbParam = new UserUhdbVo();
            userUhdbParam.setAptId(aptId); // 아파트아이디
            userUhdbParam.setUhdbId(aptPosi); // 아파트위치
            // userUhdbParam.setDong(dong);
            // userUhdbParam.setHo(ho);

            UserFcmVo userFcmParam = new UserFcmVo();
            userFcmParam.setFcmToken(fcmToken);
            userFcmParam.setAppVersion(appVersion);
            userFcmParam.setDeviceType(deviceType);

            this.userService.regUserFromMobile(userParam, userUhdbParam, userFcmParam);

            // apiResponse.setBody(user);
        } catch (Exception e) {
            LOGGER.error("[회원정보 등록 API 오류]", e);

            ApiResponseHeader header = new ApiResponseHeader();
            header.setResultCode(ApiResultCode.COMMON_FAIL.getCode());
            header.setResultMessage(e.getMessage());
            apiResponse.setHeader(header);
        }

        return apiResponse;
    }

}
