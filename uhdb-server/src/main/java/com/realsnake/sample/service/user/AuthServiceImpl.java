/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 20.
 */
package com.realsnake.sample.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.realsnake.sample.model.user.LoginUser;
import com.realsnake.sample.model.user.UserVo;

/**
 * <pre>
 * Class Name : AuthServiceImpl.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 20.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 20.
 * @version 1.0
 */
@Service("authService")
public class AuthServiceImpl implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    /**
     * 스프링 시큐리티 로그인에서 사용
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVo param = new UserVo();
        param.setUsername(username);

        try {
            UserVo user = this.userService.findUser(param);

            if (user == null) {
                throw new UsernameNotFoundException("User[" + username + "] not found.");
            }

            return new LoginUser(username, user.getPassword(), user.getAuthorities(), user.getSeq());
        } catch (Exception e) {
            logger.error("<<로그인 중 오류>> {}", e.getMessage());
        }

        return null;
    }

}
