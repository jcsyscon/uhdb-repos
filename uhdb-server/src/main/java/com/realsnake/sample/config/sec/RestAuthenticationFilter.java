/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 22.
 */
package com.realsnake.sample.config.sec;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <pre>
 * Class Name : RestAuthenticationFilter.java
 * Description : Rest 인증 요청 필터
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
@Component
public class RestAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDetailsService authService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.token.header}")
    private String jwtTokenHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = request.getHeader(this.jwtTokenHeader);

        if (StringUtils.isNotBlank(authToken)) {
            // authToken.startsWith("Bearer ")
            // String authToken = header.substring(7);
            String username = this.jwtTokenUtil.getUsernameFromToken(authToken);

            logger.debug("<<checking authentication the user>> {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // It is not compelling necessary to load the use details from the database. You could also store the information in the token and read it from it. It's up to you.
                UserDetails userDetails = this.authService.loadUserByUsername(username);

                // For simple validation it is completely sufficient to just check the token integrity. You don't have to call the database compellingly. Again it's up to you.
                if (this.jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.debug("<<authenticated user>> {}, setting security context", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}
