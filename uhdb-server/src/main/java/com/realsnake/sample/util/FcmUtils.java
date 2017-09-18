/**
 *
 */
package com.realsnake.sample.util;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realsnake.sample.model.fcm.FcmReqForm;

/**
 * <pre>
 * Class Name : EmailSender.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2016. 8. 25.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2016. 8. 25.
 * @version 1.0
 */
@Component
public class FcmUtils {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${fcm.serverKey}")
    private String fcmServerKey;

    @Autowired
    private RestTemplate restTemplate;

    // 사용 포트: 5228, 5229, 5230

    /** POST 방식 */
    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    public static final String FCM_CONTENT_TYPE = "Content-Type";

    enum FcmContentType {
        JSON("application/json;charset=UTF-8"), TEXT("application/x-www-form-urlencoded;charset=UTF-8");

        private String value;

        FcmContentType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static final String FCM_HEADER_KEY = "Authorization";
    public static final String FCM_HEADER_VALUE = "key=%s";

    /* @formatter:off */
    /**
    // 요청 JSON
    {
        "data": {
            "message": {
                "title": "푸시알림 제목입니다."
                , "body": "푸시알림 내용입니다."
                , "adUrl": null
            }
        }
        , "to": "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1..."
    }

    // 응답 JSON 샘플
    {
        "multicast_id": 108
        , "success": 1
        , "failure": 0
        , "canonical_ids": 0
        , "results": [
            { "message_id": "1:08" }
        ]
    }

    // 광고
    {
        "ad": {
            "title": ""
            , "copy": ""
            , "imageUrl": ""
            , "link": ""
            , "tel": ""
        }
    }
    */
    /* @formatter:on */

    // private HttpHeaders headers = new HttpHeaders();

    private ObjectMapper mapper = new ObjectMapper();

    @Async
    public CompletableFuture<String> send(FcmReqForm fcmReqForm) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(FCM_HEADER_KEY, String.format(FCM_HEADER_VALUE, this.fcmServerKey));
        headers.add(FCM_CONTENT_TYPE, FcmContentType.JSON.getValue());

        HttpEntity<String> entity = null;
        ResponseEntity<String> responseEntity = null;

        try {
            String fcmReqStr = this.mapper.writeValueAsString(fcmReqForm);
            logger.debug("<<FCM메시지>> {}", fcmReqStr);

            entity = new HttpEntity<>(fcmReqStr, headers);
            responseEntity = this.restTemplate.exchange(FCM_URL, HttpMethod.POST, entity, String.class);

            return CompletableFuture.completedFuture(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            logger.error("<<FCM 오류 발생>> {}", e.getMessage());

            return CompletableFuture.completedFuture(null);
        }
    }

}
