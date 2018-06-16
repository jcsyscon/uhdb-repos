package com.realsnake.sample.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.collect.Lists;
import com.realsnake.sample.util.SessionAttrs;

public class LoginChecker extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginChecker.class);

    private static final List<String> filterUrlsStartWith =
            Lists.newArrayList("/api/public", "/api/error", "/api/health", "/mobile/health", "/mobile/public", "/web/health", "/web/public", "/web/sample", "/favicon.ico");

    @SuppressWarnings("unused")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        LOGGER.debug("<<login checker. req url>> {}", url);

        if (!isLoginChecked(request)) {
            boolean invalidSession = true;
            for (String filterUrl : filterUrlsStartWith) {
                if (url.startsWith(filterUrl)) {
                    invalidSession = false;
                    break;
                }
            }

            // 일단 세션체크는 제거
            // if (invalidSession) {
            // RequestDispatcher dispatcher = request.getRequestDispatcher("/api/public/no-login");
            // dispatcher.forward(request, response);
            // return false;
            // }
        }

        return super.preHandle(request, response, handler);
    }

    // @Override
    // public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    // super.postHandle(request, response, handler, modelAndView);
    // }

    private boolean isLoginChecked(HttpServletRequest request) {
        Long userId = SessionAttrs.getUserId(request.getSession());
        return userId != null && userId != 0l;
    }

}
