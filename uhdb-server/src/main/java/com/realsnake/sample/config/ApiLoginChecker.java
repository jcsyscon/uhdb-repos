package com.realsnake.sample.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.realsnake.sample.constants.ApiResultCode;
import com.realsnake.sample.exception.CommonApiException;

public class ApiLoginChecker extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLoginChecker.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        if (!uri.startsWith("/api")) {
            return super.preHandle(request, response, handler);
        }

        LOGGER.debug("<<API login checker. 요청 uri>> {}", uri);

        if (uri.equals("/api/v1/auth") || uri.equals("/api/v1/mobile-auth-num")) {
            //
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<API 로그인 체크>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }
        }

        return super.preHandle(request, response, handler);
    }

    // @Override
    // public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    // super.postHandle(request, response, handler, modelAndView);
    // }

}
