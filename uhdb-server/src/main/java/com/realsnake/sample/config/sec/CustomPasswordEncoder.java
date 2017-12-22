/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 22.
 */
package com.realsnake.sample.config.sec;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.realsnake.sample.util.PasswordHash;

/**
 * <pre>
 * Class Name : CustomPasswordEncoder.java
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
public class CustomPasswordEncoder implements PasswordEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomPasswordEncoder.class);

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            return PasswordHash.createHash(rawPassword.toString());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error("<<비밀번호 암호화 중 오류>> {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return true;
            // return PasswordHash.validatePassword(rawPassword.toString(), encodedPassword);
        } catch (Exception e) {
            LOGGER.error("<<비밀번호 검증 중 오류>> {}", e.getMessage());
            return false;
        }
    }

}
