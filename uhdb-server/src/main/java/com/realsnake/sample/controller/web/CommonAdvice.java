/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 6.
 */
package com.realsnake.sample.controller.web;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.realsnake.sample.model.common.CommonDto;
import com.realsnake.sample.model.user.LoginUser;
import com.realsnake.sample.util.MobilePagingHelper;
import com.realsnake.sample.util.PagingHelper;

/**
 * <pre>
 * Class Name : CommonAdvice.java
 * Description : Description
 * </pre>
 *
 * @author home
 * @since 2017. 5. 6.
 * @version 1.0
 */
@Aspect
@Component
public class CommonAdvice {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(public * com.realsnake.sample.controller..*Controller.*(..))")
    public void executeBeforeMethod(JoinPoint joinPoint) throws Throwable {
        // String className = joinPoint.getTarget().getClass().getName();
        // String methodName = joinPoint.getSignature().getName();
        // logger.debug("<<클래스명: {}, 메소드명: {}>>", className, methodName);
        // logger.debug("<<joinPoint>> {}", joinPoint);

        Object params[] = joinPoint.getArgs();

        MobilePagingHelper mobilePagingHelper = null;
        PagingHelper pagingHelper = null;
        CommonDto commonDto = null;

        for (Object param : params) {
            if (param == null) {
                continue;
            }

            if (MobilePagingHelper.class.isAssignableFrom(param.getClass())) {
                mobilePagingHelper = (MobilePagingHelper) param;
            }

            if (PagingHelper.class.isAssignableFrom(param.getClass())) {
                pagingHelper = (PagingHelper) param;
            }

            if (CommonDto.class.isAssignableFrom(param.getClass())) {
                commonDto = (CommonDto) param;
            }
        }

        if (commonDto != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // if (authentication != null) {
            if (authentication != null && !"anonymousUser".equalsIgnoreCase(authentication.getPrincipal().toString())) {
                LoginUser loginUser = (LoginUser) authentication.getPrincipal();
                commonDto.setLoginUser(loginUser);
            }

            if (pagingHelper != null) {
                commonDto.setPagingHelper(pagingHelper);
            }

            if (mobilePagingHelper != null) {
                commonDto.setMobilePagingHelper(mobilePagingHelper);
            }
        }
    }

}
