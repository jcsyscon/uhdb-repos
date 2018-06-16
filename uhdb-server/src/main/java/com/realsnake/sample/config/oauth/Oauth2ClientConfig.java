/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 24.
 */
package com.realsnake.sample.config.oauth;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * <pre>
 * Class Name : Oauth2ClientConfig.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 24.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 24.
 * @version 1.0
 */
@Configuration
@EnableOAuth2Client
public class Oauth2ClientConfig {

    @Bean(name = "facebookClient")
    @ConfigurationProperties("facebook.client")
    public AuthorizationCodeResourceDetails facebookClient() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean(name = "facebookResource")
    @ConfigurationProperties("facebook.resource")
    public ResourceServerProperties facebookResource() {
        return new ResourceServerProperties();
    }

    /* @formatter:off */
/**
    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private UserDetailsService authService;

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean(name = "oauthFilter")
    public Filter oauthFilter() {
        List<Filter> filters = new ArrayList<>();
        CompositeFilter filter = new CompositeFilter();

        // 페이스북 필터
        // /login/facebook으로 접속하면 제일 먼저 페이스북 필터가 적용된다.
        OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
        facebookFilter.setRestTemplate(new OAuth2RestTemplate(this.facebookClient(), this.oauth2ClientContext));
        facebookFilter.setTokenServices(new UserInfoTokenServices(this.facebookResource().getUserInfoUri(), this.facebookClient().getClientId()));
        facebookFilter.setAuthenticationSuccessHandler(new OAuth2SuccessHandler("facebook", this.authService));
        // facebookFilter.setAuthenticationFailureHandler(failureHandler);

        filters.add(facebookFilter);
        filter.setFilters(filters);

        return filter;
    }
*/
    /* @formatter:on */


    @Resource
    @Qualifier("accessTokenRequest")
    private AccessTokenRequest accessTokenRequest;

    @Bean(name = "facebookRestTemplate")
    public OAuth2RestTemplate facebookRestTemplate() {
        return new OAuth2RestTemplate(this.facebookClient(), new DefaultOAuth2ClientContext(this.accessTokenRequest));
    }

    @Bean(name = "facebookTokenService")
    public ResourceServerTokenServices facebookTokenService() {
        UserInfoTokenServices tokenService = new UserInfoTokenServices(this.facebookResource().getUserInfoUri(), this.facebookClient().getClientId());
        tokenService.setRestTemplate(this.facebookRestTemplate());
        return tokenService;
    }

}
