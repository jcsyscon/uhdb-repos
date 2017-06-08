package com.realsnake.sample.controller.api.v1.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.common.SendVo;
import com.realsnake.sample.model.common.api.ApiResponse;
import com.realsnake.sample.model.uhdb.AptVo;
import com.realsnake.sample.model.uhdb.UhdbVo;
import com.realsnake.sample.model.user.UserFcmVo;
import com.realsnake.sample.model.user.UserUhdbVo;
import com.realsnake.sample.model.user.UserVo;
import com.realsnake.sample.service.common.CommonService;
import com.realsnake.sample.service.uhdb.UhdbService;
import com.realsnake.sample.service.user.UserService;
import com.realsnake.sample.util.RandomKeys;
import com.realsnake.sample.util.SmsUtils;

@RestController("ApiV1UserController")
@RequestMapping(value = "/api/v1/user")
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
     * 중복체크
     *
     * @param type username / email
     * @param param
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/double-check/{type}/{param}")
    public ApiResponse<?> doubleCheckProcessing(@PathVariable("type") String type, @PathVariable("param") String param) throws CommonApiException {
        try {
            String result = this.userService.checkDoubleUser(type, param);

            ApiResponse<String> apiResponse = new ApiResponse<>();
            apiResponse.setBody(result);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 모바일 인증 번호 발송 요청
     *
     * @param mobileNumber
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/mobile-auth-num")
    public ApiResponse<?> getMobileAuthNum(String mobileNumber) throws CommonApiException {
        try {
            String code = String.format("%06d", (int) (Math.random() * 1000000));
            String randomKey = RandomKeys.make(32);

            Map<String, String> codeAndKey = new HashMap<String, String>();
            codeAndKey.put("code", code);
            codeAndKey.put("key", randomKey);

            // SMS 발송
            String message = String.format("모바일 인증 번호는 [%s]입니다.", code);
            CompletableFuture<String> result = this.smsUtils.send(mobileNumber, message);
            // 발송 로그 저장
            SendVo send = new SendVo();
            send.setGubun(CommonConstants.SendType.SMS_AUTH_NUMBER.getValue());
            send.setMobile(mobileNumber);
            send.setSendMessage(message);
            send.setResultMessage(result.get());
            send.setEtc(randomKey);
            this.commonService.regSendLog(send);

            ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>();
            apiResponse.setBody(codeAndKey);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 모바일 인증 번호 검증
     *
     * @param mobileNumber
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/mobile-auth-num/check")
    public ApiResponse<?> checkMobileAuthNum(String code, String key) throws CommonApiException {
        try {
            ApiResponse<String> apiResponse = new ApiResponse<>();

            if (this.commonService.compareAuthMobileCode(code, key)) {
                apiResponse.setBody("OK");
            } else {
                apiResponse.setBody("NOK");
            }

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 아파트 검색
     *
     * @param aptName
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/search/apt")
    public ApiResponse<?> searchApt(String aptName) throws CommonApiException {
        try {
            AptVo apt = new AptVo();
            apt.setAptNm(aptName);

            List<AptVo> aptList = this.uhdbService.findAptList(apt);

            ApiResponse<List<AptVo>> apiResponse = new ApiResponse<>();
            apiResponse.setBody(aptList);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 사용자-택배함 목록
     *
     * @param aptName
     * @return
     * @throws CommonApiException
     */
    @GetMapping(value = "{seq}/uhdb/list")
    public ApiResponse<?> getAptUhdbUserList(@PathVariable("seq") Integer seq) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.getUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            List<Map<String, Object>> mapList = this.uhdbService.findAptUhdbUserList(seq);

            ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();
            apiResponse.setBody(mapList);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 택배함 찾기
     *
     * @param aptId
     * @param dong
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/search/uhdb")
    public ApiResponse<?> searchUhdb(String aptId, @RequestParam(required = false, defaultValue = StringUtils.EMPTY) String dong) throws CommonApiException {
        try {
            UhdbVo uhdb = new UhdbVo();
            uhdb.setAptId(aptId);
            uhdb.setAptPosiNm(dong);

            List<UhdbVo> uhdbList = this.uhdbService.findUhdbList(uhdb);

            ApiResponse<List<UhdbVo>> apiResponse = new ApiResponse<>();
            apiResponse.setBody(uhdbList);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 회원 정보 조회
     *
     * @param seq
     * @return
     * @throws CommonApiException
     */
    @GetMapping(value = "/{seq}")
    public ApiResponse<?> gerUser(@PathVariable("seq") Integer seq) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.getUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            UserVo user = this.userService.findUser(seq);

            List<Map<String, Object>> mapList = this.uhdbService.findAptUhdbUserList(seq);

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
     * 회원 정보 등록
     *
     * @param user
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/join")
    public ApiResponse<?> regUser(UserVo user, UserUhdbVo userUhdb, UserFcmVo userFcm) throws CommonApiException {
        try {
            this.userService.regUserFromMobile(user, userUhdb, userFcm);

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(user);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 회원 정보 수정
     *
     * @param seq
     * @param user
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/modify/{seq}")
    public ApiResponse<?> modifyUser(@PathVariable("seq") Integer seq, UserVo user) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.modifyUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            this.userService.modifyUser(user);

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(user);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 회원 정보 수정 - 택배함 수정
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
                LOGGER.debug("<<ApiUserController.modifyUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            this.userService.modifyUserUhdb(userUhdb);

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(user);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 회원 비밀번호 수정
     *
     * @param seq
     * @param user
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/modify/{seq}/password")
    public ApiResponse<?> modifyUserPassword(@PathVariable("seq") Integer seq, UserVo user) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.modifyUserPassword>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            this.userService.modifyUserPassword(user);

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
     * @param seq
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/secede/{seq}")
    public ApiResponse<?> secedeUser(@PathVariable("seq") Integer seq) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.secedeUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            UserVo user = new UserVo();
            user.setSeq(seq);

            this.userService.secedeUser(user);

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
     * @param seq
     * @param alarmRecYn
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/modify/{seq}/alarm-rec-yn")
    public ApiResponse<?> modifyAlarmRecYn(@PathVariable("seq") Integer seq, String alarmRecYn) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.modifyAlarmRecYn>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            UserVo user = new UserVo();
            user.setSeq(seq);
            user.setAlarmRecYn(alarmRecYn);

            this.userService.modifyUserAlarmRecYn(user);

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(user);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 회원 정보 수정 - FCM토큰 수정
     *
     * @param userSeq
     * @param userFcm
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/modify/{userSeq}/fcm-token")
    public ApiResponse<?> modifyUserFcm(@PathVariable("userSeq") Integer userSeq, UserFcmVo userFcm) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.modifyUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            this.userService.modifyUserFcm(userFcm);

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(user);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

}
