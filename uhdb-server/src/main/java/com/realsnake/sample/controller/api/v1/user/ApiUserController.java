package com.realsnake.sample.controller.api.v1.user;

import java.util.HashMap;
import java.util.Map;

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
import com.realsnake.sample.model.user.UserFcmVo;
import com.realsnake.sample.model.user.UserUhdbVo;
import com.realsnake.sample.model.user.UserVo;
import com.realsnake.sample.service.user.UserService;
import com.realsnake.sample.util.RandomKeys;

@RestController("ApiV1UserController")
@RequestMapping(value = "/api/v1/user")
public class ApiUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserController.class);

    @Autowired
    private UserService userService;

    /**
     * 중복체크
     *
     * @param type username / email
     * @param param
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/double-check/{type}/{param}")
    public ApiResponse<?> doubleCheckProcessing(@PathVariable("type") String type, @PathVariable("param") String param) throws Exception {
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

            // TODO: sms 발송 및 DB 저장
            Map<String, String> codeAndKey = new HashMap<String, String>();
            codeAndKey.put("code", code);
            codeAndKey.put("key", randomKey);

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
            // TODO: DB 조회 및 인증번호 검증

            ApiResponse<String> apiResponse = new ApiResponse<>();
            apiResponse.setBody("OK");


            // apiResponse.setBody("NOK");

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
            // TODO: 아파트 DB 조회

            ApiResponse<String> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(random);

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
    public ApiResponse<?> searchUhdb(String aptId, @RequestParam(required = false) String dong) throws CommonApiException {
        try {
            // TODO: 무인택배함 조회

            ApiResponse<String> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(random);

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

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            apiResponse.setBody(user);

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
     * @param seq
     * @param userUhdb
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/modify/{seq}/uhdb")
    public ApiResponse<?> modifyUserUhdb(@PathVariable("seq") Integer seq, UserUhdbVo userUhdb) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.modifyUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            userUhdb.setUserSeq(seq);
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

            this.userService.modifyUser(user);

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
     * @param seq
     * @param userFcm
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/modify/{seq}/fcm-token")
    public ApiResponse<?> modifyUserFcm(@PathVariable("seq") Integer seq, UserFcmVo userFcm) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiUserController.modifyUser>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            userFcm.setUserSeq(seq);
            this.userService.modifyUserFcm(userFcm);

            ApiResponse<UserVo> apiResponse = new ApiResponse<>();
            // apiResponse.setBody(user);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /* @formatter:off */
    /**
    https://api-sms.cloud.toast.com


        [SMS]
            /sms/v2.0/appKeys/{appKey}/sender/sms

        [Request Body]
        {
            "body": "{본문 내용}",
            "sendNo": "{발신번호}",
            "recipientList": [{
                "recipientNo": "{수신번호}"
            }],
            "userId": "발송구분자"
        }

        [응답]
        {
          "header": {
            "isSuccessful": true,
            "resultCode": 0,
            "resultMessage": "SUCCESS"
          },
          "body": {
            "data": {
              "requestId": "0-201607-424541-1",
              "statusCode": "2",
              "sendResultList" : [
                  {
                      "recipientNo" : {수신번호},
                      "resultCode" :  0,
                      "resultMessage" : "SUCCESS"
                  }
              ]
            }
          }
        }



        [LMS]
            /sms/v2.0/appKeys/{appKey}/sender/mms

        [Request Body]
        {
            "title": "{제목}",
            "body": "{본문 내용}",
            "sendNo": "{발신번호}",
            "recipientList": [{
                "recipientNo": "{수신번호}",
                "templateParameter": { }
            }],
            "userId": ""
        }

        [응답]
        {
          "header": {
            "isSuccessful": true,
            "resultCode": 0,
            "resultMessage": "SUCCESS"
          },
          "body": {
            "data": {
              "requestId": "0-201607-424541-1",
              "statusCode": "2",
              "sendResultList" : [
                  {
                      "recipientNo" : {수신번호},
                      "resultCode" :  0,
                      "resultMessage" : "SUCCESS"
                  }
              ]
            }
          }
        }
    */
    /* @formatter:on */

}
