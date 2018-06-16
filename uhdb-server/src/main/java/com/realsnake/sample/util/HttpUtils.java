/**
 *
 */
package com.realsnake.sample.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.realsnake.sample.constants.CommonConstants;

/**
 * @author 전강욱(realsnake1975@gmail.com)
 * @description
 *
 */
@Component
public class HttpUtils {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @author 전강욱(realsnake1975@gmail.com)
     * @description URL 호출
     *
     * @param reqUrl
     * @param httpMethod
     * @param params
     * @param headers
     */
    public HttpEntity connectPost(String reqUrl, Map<String, String> headers, Map<String, String> params, File targetFile) throws Exception {
        HttpClient httpClient = null;
        HttpEntity httpEntity = null;

        try {
            httpClient = HttpClientBuilder.create().build();

            HttpPost httpPost = new HttpPost(reqUrl);

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }

            ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }

            httpPost.setEntity(new UrlEncodedFormEntity(postParams, CommonConstants.DEFAULT_ENCODING));
            logger.debug("<<Request URL>> {}", reqUrl);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int httpStatus = httpResponse.getStatusLine().getStatusCode();
            logger.info("<<HTTP 응답({})>>", httpStatus);

            if (HttpStatus.SC_OK == httpStatus) {
                httpEntity = httpResponse.getEntity();
            }
        } catch (Exception e) {
            logger.error("<<HTTP Connection 에러>>", e);
            throw e;
        }

        return httpEntity;
    }

    /**
     * @author 전강욱(realsnake1975@gmail.com)
     * @description 텍스트를 음성 파일로 저장
     *
     * @param text
     * @param targetFile
     */
    @Async
    public Future<String> saveMp3OfVocalware(String text, File targetFile) {
        String connUrl = "http://www.vocalware.com/tts/gen.php";

        Map<String, String> params = new ConcurrentHashMap<String, String>();
        params.put("EID", "3");
        params.put("LID", "13");
        params.put("VID", "1");
        params.put("TXT", text);
        params.put("EXT", "mp3");
        params.put("FX_TYPE", "");
        params.put("FX_LEVEL", "");
        params.put("ACC", "5847458");
        params.put("API", "2495595");
        params.put("SESSION", "");
        params.put("HTTP_ERR", "");

        StringBuilder sb = new StringBuilder();
        // sb.append(params.get("EID")).append(params.get("LID")).append(params.get("VID")).append(URLEncoder.encode(params.get("TXT"), Constants.DEFAULT_ENCODING));
        sb.append(params.get("EID")).append(params.get("LID")).append(params.get("VID")).append(params.get("TXT"));
        sb.append(params.get("EXT")).append(params.get("FX_TYPE")).append(params.get("FX_LEVEL")).append(params.get("ACC")).append(params.get("API")).append(params.get("SESSION"))
                .append(params.get("HTTP_ERR"));
        sb.append("dad907f5d97d0adc6c1ed65d185999c9"); // SECRET

        // logger.debutg(URLEncoder.encode(params.get("TXT"), Constants.DEFAULT_ENCODING));
        String cs = DigestUtils.md5Hex(sb.toString());
        // logger.debug(cs);
        params.put("CS", cs);

        // this.connectPost(connUrl, null, params, new File("C:\\temp", "tts-1.mp3"));

        HttpEntity httpEntity = null;

        try {
            httpEntity = this.connectPost(connUrl, null, params, targetFile);
            FileUtils.writeByteArrayToFile(targetFile, EntityUtils.toByteArray(httpEntity));
            logger.info("<<Vocalware 연동 및 오디오 파일[{}] 저장 성공>>", targetFile.getCanonicalFile());
        } catch (Exception e) {
            logger.error("<<Vocalware mp3파일 저장 중 에러>>", e);
        }

        return new AsyncResult<String>("SUCCESS");
    }

    private static final String VOICE_MIJIN = "mijin";
    private static final String VOICE_JINHO = "jinho";

    /**
     * @author 전강욱(realsnake1975@gmail.com)
     * @description 텍스트를 음성 파일로 저장
     *
     * @param clientId
     * @param clientSecret
     * @param maleOrFemale
     * @param text
     * @param targetFile
     */
    @Async
    public Future<String> saveMp3OfNaver(String clientId, String clientSecret, String maleOrFemale, String text, File targetFile) {
        String connUrl = "https://openapi.naver.com/v1/voice/tts.bin";
        Map<String, String> headers = new ConcurrentHashMap<String, String>();
        headers.put("X-Naver-Client-Id", clientId);
        headers.put("X-Naver-Client-Secret", clientSecret);
        Map<String, String> params = new ConcurrentHashMap<String, String>();
        params.put("speaker", ("female".equals(maleOrFemale)) ? VOICE_MIJIN : VOICE_JINHO); // mijin:미진(한국어, 여성), jinho:진호(한국어, 남성)
        params.put("speed", "0"); // -5 ~ 5 사이 정수로 -5면 0.5배 빠른, 5면 0.5배 느린, 0이면 정상 속도의 목소리로 합성
        params.put("text", text);

        HttpEntity httpEntity = null;

        try {
            httpEntity = this.connectPost(connUrl, headers, params, targetFile);
            if (httpEntity != null) {
                FileUtils.writeByteArrayToFile(targetFile, EntityUtils.toByteArray(httpEntity));
                logger.info("<<Naver 연동 및 오디오 파일[{}] 저장 성공>>", targetFile.getCanonicalFile());
            }
        } catch (Exception e) {
            logger.error("<<Naver mp3파일 저장 중 에러>>", e);
        }

        return new AsyncResult<String>("SUCCESS");
    }

    // public static void main(String[] args) throws Exception {
    // HttpUtils hu = new HttpUtils();
    // String clientId = "m48ceQLVHlCHbMWzUnyH";
    // String clientSecret = "pR3_a5Nngd";
    // String text = "안녕하세요? 대한민국의 신바람 박사 전강욱입니다. 이히~ 조아~조아~조아~";
    //
    // hu.saveMp3OfNaver(clientId, clientSecret, "female", text, new File("C:\\temp", "tts-2.mp3"));
    // }

}
