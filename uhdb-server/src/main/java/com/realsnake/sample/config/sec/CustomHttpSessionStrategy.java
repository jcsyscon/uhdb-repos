/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 22.
 */
package com.realsnake.sample.config.sec;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.accept.ContentNegotiationStrategy;

/**
 * <pre>
 * Class Name : CustomHttpSessionStrategy.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 22.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 22.
 * @version 1.0
 */
public class CustomHttpSessionStrategy implements HttpSessionStrategy {
    private HttpSessionStrategy browser;

    private HttpSessionStrategy api;

    private RequestMatcher browserMatcher;

    @Autowired
    public CustomHttpSessionStrategy(ContentNegotiationStrategy contentNegotiationStrategy) {
        // HeaderHttpSessionStrategy : 쿠키의 세션을 사용하는 대신 header에 x-auth-token 값 등을 사용, 폼로그인-쿠키 저장 방식을 사용하지 못함
        this(new CookieHttpSessionStrategy(), new HeaderHttpSessionStrategy());
        MediaTypeRequestMatcher matcher = new MediaTypeRequestMatcher(contentNegotiationStrategy, Arrays.asList(MediaType.TEXT_HTML));
        matcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));

        RequestHeaderRequestMatcher javascript = new RequestHeaderRequestMatcher("X-Requested-With");

        this.browserMatcher = new OrRequestMatcher(Arrays.asList(matcher, javascript));
    }

    public CustomHttpSessionStrategy(HttpSessionStrategy browser, HttpSessionStrategy api) {
        this.browser = browser;
        this.api = api;
    }

    @Override
    public String getRequestedSessionId(HttpServletRequest request) {
        return getStrategy(request).getRequestedSessionId(request);
    }

    @Override
    public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
        getStrategy(request).onNewSession(session, request, response);
    }

    @Override
    public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
        getStrategy(request).onInvalidateSession(request, response);
    }

    private HttpSessionStrategy getStrategy(HttpServletRequest request) {
        return this.browserMatcher.matches(request) ? this.browser : this.api;
    }
}
