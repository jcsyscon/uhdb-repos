package com.realsnake.sample.controller.common;

import java.net.URI;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.realsnake.sample.constants.CommonConstants;

@Controller
@RequestMapping(value = "/oauth")
public class OauthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OauthController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Resource(name = "facebookClient")
    private AuthorizationCodeResourceDetails facebookClient;

    @Deprecated
    @Resource(name = "facebookRestTemplate")
    private OAuth2RestTemplate facebookRestTemplate;

    @Deprecated
    @Resource(name = "facebookTokenService")
    private ResourceServerTokenServices facebookTokenService;

    /** 로그인 화면 호출 URL */
    static final String CONNECT_URL = "%s?client_id=%s&redirect_uri=%s&scope=public_profile,email"; // scope=public_profile,email,user_friends
    /** 접큰토큰 요청 URL */
    static final String ACCESS_TOKEN_REQ_URL = CONNECT_URL + "&client_secret=%s&code=%s";
    /** 개인정보 요청 URL */
    static final String PROFILE_REQ_URL = "%s?access_token=%s";

    @GetMapping(value = "/connect/{type}")
    public String connect(@PathVariable("type") String type) throws Exception {
        // TODO: 로그인 여부 체크
        LOGGER.debug("<<type>> {}", type);

        AuthorizationCodeResourceDetails client = this.getClient(type);
        String connectUrl = String.format(CONNECT_URL, client.getUserAuthorizationUri(), client.getClientId(), client.getPreEstablishedRedirectUri());
        LOGGER.debug("<<connectUrl>> {}", connectUrl);

        return "redirect:" + connectUrl;
    }

    @GetMapping(value = "/callback/{type}")
    public void callback(HttpServletRequest request, HttpServletResponse response, @PathVariable("type") String type) throws Exception {
        LOGGER.debug("<<type>> {}", type);

        String code = request.getParameter("code");
        String error = request.getParameter("error"); // 로그인을 취소한 경우

        if (StringUtils.isNotEmpty(code)) { // 로그인 성공 - 액세스 토큰 요청 및 로그인 완료 처리
            LOGGER.debug("<<code>> {}", code);

            AuthorizationCodeResourceDetails client = this.getClient(type);

            /* @formatter:off */
            String accessTokenReqUrl = String.format(ACCESS_TOKEN_REQ_URL
                    , client.getAccessTokenUri()
                    , client.getClientId()
                    , "http://localhost:8080/oauth/callback/" + type
                    , client.getClientSecret()
                    , code
            );
            LOGGER.debug("<<accessTokenReqUrl>> {}", accessTokenReqUrl);
            /* @formatter:on */

            AccessToken accessToken = this.restTemplate.getForObject(new URI(accessTokenReqUrl), AccessToken.class);
            LOGGER.debug("<<accessToken>> {}", accessToken.toString());



            /* @formatter:off */
            /**
            try {
                OAuth2AccessToken oauth2AccessToken = this.getRestTemplate(type).getAccessToken();
                LOGGER.debug("<<oauth2AccessToken>> {}", oauth2AccessToken.toString());
                OAuth2Authentication auth = this.getTokenService(type).loadAuthentication(oauth2AccessToken.getValue());
                System.out.println(auth.getUserAuthentication().getDetails());
            } catch (Exception e) {
                e.printStackTrace();
            }
            */
            /* @formatter:on */

            // TODO: 접근토큰 확인 후
        } else { // 로그인 실패
            LOGGER.debug("<<error>> {}", error);
        }
    }

    private AuthorizationCodeResourceDetails getClient(String type) {
        if (CommonConstants.SocialType.FACEBOOK.getValue().equals(type)) {
            return this.facebookClient;
        }

        return null;
    }

    @Deprecated
    private OAuth2RestTemplate getRestTemplate(String type) {
        if (CommonConstants.SocialType.FACEBOOK.getValue().equals(type)) {
            return this.facebookRestTemplate;
        }

        return null;
    }

    @Deprecated
    private ResourceServerTokenServices getTokenService(String type) {
        if (CommonConstants.SocialType.FACEBOOK.getValue().equals(type)) {
            return this.facebookTokenService;
        }

        return null;
    }

    // {
    // "access_token":"EAAU4cOBvhoYBABZAdsCct9l7jHrbDrpkUpAnf6rCVJZCBDoZCr1baUm2EhO4dDdTer22noNMFPsBAfBASsiZB8wPPXptpxBpOrcckvgdZA4QIlfc4VxZCvHXyRtaJl02SZB1pfCjawDxitetHclS2oejZBeLatmpk2YZD"
    // ,"token_type":"bearer"
    // ,"expires_in":5121307
    // }
    public static class AccessToken {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("expires_in")
        private Integer expiresIn;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public Integer getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
        }

        @Override
        public String toString() {
            return "AccessToken [accessToken=" + accessToken + ", tokenType=" + tokenType + ", expiresIn=" + expiresIn + "]";
        }
    }

}
