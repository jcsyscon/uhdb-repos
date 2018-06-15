/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 24.
 */
package com.realsnake.sample.config.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * <pre>
 * Class Name : OAuth2SuccessHandler.java
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
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @SuppressWarnings("unused")
    private String type;

    @SuppressWarnings("unused")
    private UserDetailsService authService;

    public OAuth2SuccessHandler(String type, UserDetailsService authService) {
        this.type = type;
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("=================================================================================================");
        System.out.println("=================================================================================================");
        System.out.println("=================================================================================================");
    }

}
