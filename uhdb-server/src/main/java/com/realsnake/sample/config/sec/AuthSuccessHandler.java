/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 21.
 */
package com.realsnake.sample.config.sec;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.realsnake.sample.model.user.LoginUser;

/**
 * <pre>
 * Class Name : AuthSuccessHandler.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 21.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 21.
 * @version 1.0
 */
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String uri = request.getRequestURI();

        response.setStatus(HttpServletResponse.SC_OK);

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        if (uri.startsWith("/api")) {
            logger.debug("<<API REST 로그인 성공, LoginUser>> {}", loginUser.toString());
        } else {
            logger.debug("<<웹 로그인 성공, LoginUser>> {}", loginUser.toString());

            response.sendRedirect("/main");
        }
    }

}
