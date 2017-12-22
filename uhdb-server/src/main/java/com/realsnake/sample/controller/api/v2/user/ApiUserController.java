package com.realsnake.sample.controller.api.v2.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
import com.realsnake.sample.util.PasswordHash;
import com.realsnake.sample.util.RandomKeys;
import com.realsnake.sample.util.SmsUtils;

@RestController("ApiV2UserController")
@RequestMapping(value = "/api/v2/user")
public class ApiUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserController.class);

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
     * @param mobileNo 핸드폰번호
     * @return
     */
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
     * 모바일 인증번호 발송요청
     *
     * @param mobileNo 핸드폰번호
     * @return
     */
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
     * 무인택배함 찾기(NONEXIST/EXIST)
     *
     * @param uhdbNo 무인택배함번호
     * @return
     */
    @GetMapping(value = "/search/uhdb/{uhdbNo}")
    public ApiResponse<?> searchUhdb(String uhdbNo) {
        ApiResponse<String> apiResponse = new ApiResponse<>();

        try {
            UhdbVo uhdbParam = new UhdbVo();
            uhdbParam.setAptId(uhdbNo);

            List<UhdbVo> uhdbList = this.uhdbService.findUhdbList(uhdbParam);
            if (uhdbList == null || uhdbList.isEmpty()) {
                apiResponse.setBody("NONEXIST");
            } else {
                apiResponse.setBody("EXIST");
            }
        } catch (Exception e) {
            LOGGER.error("[무인택배함 찾기 API 오류]", e);

            ApiResponseHeader header = new ApiResponseHeader();
            header.setResultCode(ApiResultCode.COMMON_FAIL.getCode());
            header.setResultMessage(e.getMessage());
            apiResponse.setHeader(header);
        }

        return apiResponse;
    }

    /**
     * 회원정보 등록
     *
     * @param mobileNo 핸드폰번호
     * @param uhdbNo 무인택배함번호
     * @param fcmToken FCM토큰
     * @param appVersion 앱버전
     * @param deviceType 디바이스유형(android/ios/none)
     * @return
     */
    @PostMapping(value = "/join")
    public ApiResponse<?> regUser(String mobileNo, String uhdbNo, String fcmToken, String appVersion, String deviceType) {
        ApiResponse<UserVo> apiResponse = new ApiResponse<>();

        try {
            UserVo userParam = new UserVo();
            userParam.setUsername(mobileNo); // 암호화안함
            userParam.setName(mobileNo); // 이름 핸드폰번호로, 아래 서비스 단에서 암호화
            userParam.setPassword(mobileNo); // 핸드폰번호를 해싱 처리, 아래 서비스 단에서 해싱 처리
            userParam.setMobile(mobileNo); // 아래 서비스 단에서 암호화

            UserUhdbVo userUhdbParam = new UserUhdbVo();
            userUhdbParam.setAptId(uhdbNo);
            // userUhdbParam.setUhdbId(uhdbNo);
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

    /**
     * 회원 무인택배함 목록조회
     *
     * @param userSeq 회원 일련번호
     * @return
     */
    @GetMapping(value = "/{userSeq}/uhdb/list")
    public ApiResponse<?> getAptUhdbUserList(@PathVariable("userSeq") Integer userSeq) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.getUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.AUTH_FAIL);
            }

            List<Map<String, Object>> mapList = this.uhdbService.findAptUhdbUserList(userSeq);

            ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();
            apiResponse.setBody(mapList);
            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 회원정보 조회
     *
     * @param userSeq 회원 일련번호
     * @return
     * @throws CommonApiException
     */
    @GetMapping(value = "/{userSeq}")
    public ApiResponse<?> gerUser(@PathVariable("userSeq") Integer userSeq) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.getUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.AUTH_FAIL);
            }

            UserVo user = this.userService.findUser(userSeq);

            List<Map<String, Object>> mapList = this.uhdbService.findAptUhdbUserList(userSeq);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("user", user);
            responseMap.put("aptList", mapList);

            ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
            apiResponse.setBody(responseMap);
            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 회원 정보 수정
     *
     * @param userSeq 회원 일련번호
     * @param mobileNo 핸드폰번호
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/modify/{userSeq}")
    public ApiResponse<?> modifyUser(@PathVariable("userSeq") Integer userSeq, String mobileNo) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.getUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.AUTH_FAIL);
            }

            UserVo userParam = new UserVo();
            userParam.setSeq(userSeq);
            userParam.setUsername(mobileNo); // 암호화 안함
            userParam.setName(mobileNo); // 이름 핸드폰번호로, 아래 서비스 단에서 암호화
            userParam.setPassword(PasswordHash.createHash(mobileNo)); // 핸드폰번호를 해싱 처리
            userParam.setMobile(mobileNo); // 아래 서비스 단에서 암호화
            this.userService.modifyUser(userParam);

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(user);
            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 회원 탈퇴
     *
     * @param userSeq 회원 일련번호
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/secede/{userSeq}")
    public ApiResponse<?> secedeUser(@PathVariable("userSeq") Integer userSeq) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.getUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.AUTH_FAIL);
            }

            UserVo userParam = new UserVo();
            userParam.setSeq(userSeq);

            this.userService.secedeUser(userParam);

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(user);
            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 알림설정(수신여부) 수정
     *
     * @param userSeq 회원 일련번호
     * @param String alarmRecYn 알람 수신 여부(Y/N)
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/modify/{userSeq}/alarm-rec-yn")
    public ApiResponse<?> modifyAlarmRecYn(@PathVariable("userSeq") Integer userSeq, String alarmRecYn) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.getUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.AUTH_FAIL);
            }

            UserVo userParam = new UserVo();
            userParam.setSeq(userSeq);
            userParam.setAlarmRecYn(alarmRecYn);

            this.userService.modifyUserAlarmRecYn(userParam);

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(user);
            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 회원 FCM토큰/앱버전/디바이스유형 수정
     *
     * @param userSeq 회원 일련번호
     * @param fcmToken FCM토큰
     * @param appVersion 앱버전
     * @param deviceType 디바이스유형(android/ios/none)
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/modify/{userSeq}/fcm-token")
    public ApiResponse<?> modifyUserFcm(@PathVariable("userSeq") Integer userSeq, String fcmToken, String appVersion, String deviceType) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.getUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.AUTH_FAIL);
            }

            UserFcmVo userFcmParam = new UserFcmVo();
            userFcmParam.setUserSeq(userSeq);
            userFcmParam.setFcmToken(fcmToken);
            userFcmParam.setAppVersion(appVersion);
            userFcmParam.setDeviceType(deviceType);

            this.userService.modifyUserFcm(userFcmParam);

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(user);
            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 회원 택배함 정보 수정
     *
     * @param userSeq
     * @param userUhdb
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/modify/{userSeq}/uhdb")
    public ApiResponse<?> modifyUserUhdb(@PathVariable("userSeq") Integer userSeq, UserUhdbVo userUhdb) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.getUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.AUTH_FAIL);
            }

            this.userService.modifyUserUhdb(userUhdb);

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(user);
            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

}
