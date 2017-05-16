/**
 *
 */
package com.realsnake.sample.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;

    // 사용 포트: 5228, 5229, 5230

    /** POST 방식 */
    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    public static final String FCM_CONTENT_TYPE = "Content-Type";

    enum FcmContentType {
        JSON("application/json"), TEXT("application/x-www-form-urlencoded;charset=UTF-8");

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
            "content": {
                "title": "5x1"
                , "text": "This is a Firebase Cloud Messaging Topic Message!"
            }
            , "etc": {
                "ad_url": ""
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
            , "content": ""
            , "image_url": ""
            , "link": ""
            , "tel": ""
        }
    }
    */
    /* @formatter:on */

}
