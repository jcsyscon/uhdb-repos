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
 * Class Name : SmsUtils.java
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
public class SmsUtils {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${sms.api.key}")
    private String smsApiKey;

    @Value("${sms.api.userId}")
    private String smsApiUserId;

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

https://apis.aligo.in/

[요청]
userid          M   Char    사용자ID, ex) userid=가입아이디
key             M   Char    API Key, ex) key=zt5p0p7c6we0w7cw9zccjuagcxyh8q5x
sender          M   Char    발신자 전화번호 (최대 16bytes), ex) from=025114560
receiver            M   Char    수신자 전화번호 - 컴마(,)분기 입력으로 최대 1천명까지 동시전송가능합니다. ex) receiver=01011112222,01022223333
destination     O   Char    %고객명% 변경할 넣으면 성명 삽입 - 본문에 %고객명% 문구가 있는경우만 처리됩니다. ex) destination=01011112222|홍길동,01022223333|고길동
msg             M   Char    메시지 내용 - 메시지 내용은 UTF-8과 URL인코딩 필요 - 90Byte를 초과하는 경우 자동으로 장문(LMS)처리됩니다.
rdate               O   Char    예약일 (현재일이상), ex) rdate=20170522
rtime               O   Char    예약시간 - 현재시간보다 30분이상 커야함. ex) rtime=1625 (오후 4시 25분의 경우)
image           O   Char    첨부이미지 - 지원형식 : 900KB 이하의 JPEG, PNG, GIF 형식의 이미지파일 - 이미지가 첨부되면 자동으로 MMS 전환됩니다.
testmode_yn O   Char    테스트전송 유무 - Y : 실문자 발송하지 않음(자동취소)

[요청 응답]
result_code M   Char    결과코드
message M   Char    결과 메세지
msg_id      O   Integer 발송된 메시지에 부여되는 메시지 아이디 - 에러시에는 부여되지 않습니다.
success_cnt M   Char    성공건수
error_cnt   M   Char    실패건수
msg_type    O   Char    메시지 타입 (1. SMS, 2.LMS, 3. MMS)

[응답코드]
SUCCESS      1      전송성공(message:success)
SUCCESS      1      예약성공(message:reserved)
FAILURE         -100    서버에러
INVALID         -101    필수입력 부적합
INVALID         -102    인증 에러
INVALID         -103    발신번호 인증 에러
INVALID         -105    발송건수제한,발송시간 에러
INVALID         -109    문자 잔여횟수 부족
INVALID         -115    예약 시간 에러
INVALID         -201    전송가능 건수 부족(충전잔액부족)
INVALID         -301    이미지 입력오류
UNKNOWN -900    알려지지 않은 에러

 */
    /* @formatter:on */

    private HttpHeaders headers = new HttpHeaders();

    private ObjectMapper mapper = new ObjectMapper();

    @Async
    public CompletableFuture<String> send(FcmReqForm fcmReqForm) {
        // this.headers.add(FCM_HEADER_KEY, String.format(FCM_HEADER_VALUE, this.fcmServerKey));
        this.headers.add(FCM_CONTENT_TYPE, FcmContentType.JSON.getValue());

        HttpEntity<String> entity = null;
        ResponseEntity<String> responseEntity = null;

        try {
            String fcmReqStr = this.mapper.writeValueAsString(fcmReqForm);
            logger.debug("<<FCM메시지>> {}", fcmReqStr);

            entity = new HttpEntity<>(fcmReqStr, this.headers);
            responseEntity = this.restTemplate.exchange(FCM_URL, HttpMethod.POST, entity, String.class);

            return CompletableFuture.completedFuture(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            logger.error("<<FCM 오류 발생>> {}", e.getMessage());

            return CompletableFuture.completedFuture(null);
        }
    }

}
