package com.realsnake.sample.controller.api.v1.user;

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
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.common.api.ApiResponse;
import com.realsnake.sample.model.user.UserVo;
import com.realsnake.sample.service.user.UserService;
import com.realsnake.sample.util.MobilePagingHelper;

@RestController("ApiV1UserController")
@RequestMapping(value = "/api/v1/user")
public class ApiUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserController.class);

    @Autowired
    private UserService userService;

    @GetMapping(value = "/login")
    public ApiResponse<?> login(MobilePagingHelper mobilePagingHelper) throws CommonApiException {
        LOGGER.debug("<<mobilePagingHelper.toString()>>, {}", mobilePagingHelper.toString());

        ApiResponse<MobilePagingHelper> apiResponse = new ApiResponse<>();
        apiResponse.setBody(mobilePagingHelper);

        return apiResponse;
    }

    @GetMapping(value = "/{seq}")
    public ApiResponse<?> gerUser(@PathVariable("seq") Integer seq) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<</auth/refresh>> 인증 실패");
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
