/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 22.
 */
package com.realsnake.sample.config.sec;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.realsnake.sample.model.user.LoginUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * <pre>
 * Class Name : JwtTokenUtil.java
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
@Component
public class JwtTokenUtil {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // https://jwt.io/
    // iss: 토큰을 발급한 발급자(Issuer)
    // sub: Claim의 주제(Subject)로 토큰이 갖는 문맥을 의미한다.
    // aud: 이 토큰을 사용할 수신자(Audience)
    // exp: 만료시간(Expiration Time)은 만료시간이 지난 토큰은 거절해야 한다.
    // nbf: Not Before의 의미로 이 시간 이전에는 토큰을 처리하지 않아야 함을 의미한다.
    // iat: 토큰이 발급된 시간(Issued At)
    // jti: JWT ID로 토큰에 대한 식별자이다.

    private static final String CLAIM_KEY_ISSUER = "iss";
    @SuppressWarnings("unused")
    private static final String CLAIM_KEY_SUBJECT = "sub";
    private static final String CLAIM_KEY_AUDIENCE = "aud";
    @SuppressWarnings("unused")
    private static final String CLAIM_KEY_EXPIRATIONTIME = "exp";
    private static final String CLAIM_KEY_ISSUEDAT = "iat";
    private static final String CLAIM_KEY_JWTID = "jti";
    private static final String CLAIM_KEY_DEVICE = "dev";

    private static final String CLAIM_VALUE_ISSUER = "realsnake.com";

    private static final String DEVICE_UNKNOWN = "unknown";
    private static final String DEVICE_WEB = "web";
    private static final String DEVICE_MOBILE = "mobile";
    private static final String DEVICE_TABLET = "tablet";

    private static String SECRET = "!@#123qwe";
    private static Long EXPIRATION = 31536000000L; // 1년(토큰 만료 기한은 1년이며 1년이 지난 토큰은 폐기되어 해석 못함)

    @Value("${jwt.token.header}")
    private String jwtTokenHeader;

    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            logger.error("<<JWT 오류>>", e);
        }
        return username;
    }

    public Date getIssuedAtDateFromToken(String token) {
        Date created = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_ISSUEDAT));
        } catch (Exception e) {
            logger.error("<<JWT 오류>>", e);
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            logger.error("<<JWT 오류>>", e);
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();

            // for (String key : claims.keySet()) {
            // logger.debug("<<Claims>> key: {}, value: {}", key, claims.get(key));
            // }
        } catch (Exception e) {
            logger.error("<<JWT 오류>>", e);
        }
        return claims;
    }

    private Date generateExpirationDate() {
        Date expDate = new Date();
        expDate.setTime(System.currentTimeMillis() + EXPIRATION);
        return expDate;
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private String generateDevice(Device device) {
        String audience = DEVICE_UNKNOWN;
        if (device.isNormal()) {
            audience = DEVICE_WEB;
        } else if (device.isTablet()) {
            audience = DEVICE_TABLET;
        } else if (device.isMobile()) {
            audience = DEVICE_MOBILE;
        }
        return audience;
    }

    @Deprecated
    private Boolean ignoreTokenExpiration(String token) {
        String audience = getDeviceFromToken(token);
        return (DEVICE_TABLET.equals(audience) || DEVICE_MOBILE.equals(audience));
    }

    @Deprecated
    private String getDeviceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_DEVICE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    public String generateToken(LoginUser loginUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_ISSUER, CLAIM_VALUE_ISSUER);
        claims.put(CLAIM_KEY_AUDIENCE, loginUser.getUsername());
        claims.put(CLAIM_KEY_ISSUEDAT, new Date());
        claims.put("seq", loginUser.getSeq());
        return this.generateToken(claims);
    }

    public String generateToken(LoginUser loginUser, String jwtId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_ISSUER, CLAIM_VALUE_ISSUER);
        claims.put(CLAIM_KEY_AUDIENCE, loginUser.getUsername());
        claims.put(CLAIM_KEY_ISSUEDAT, new Date());
        claims.put(CLAIM_KEY_JWTID, jwtId);
        claims.put("seq", loginUser.getSeq());
        return this.generateToken(claims);
    }

    public String generateToken(LoginUser loginUser, Device device) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_ISSUER, CLAIM_VALUE_ISSUER);
        claims.put(CLAIM_KEY_AUDIENCE, loginUser.getUsername());
        claims.put(CLAIM_KEY_ISSUEDAT, new Date());
        claims.put(CLAIM_KEY_DEVICE, this.generateDevice(device));
        claims.put("seq", loginUser.getSeq());
        return this.generateToken(claims);
    }

    /**
     * 핸드폰번호와 Session id로 JWT 생성
     *
     * @param mobileNo
     * @param sessionId
     * @Param userSeq
     * @return
     */
    public String generateToken(String mobileNo, String sessionId, Integer userSeq) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_ISSUER, CLAIM_VALUE_ISSUER);
        claims.put(CLAIM_KEY_AUDIENCE, mobileNo);
        claims.put(CLAIM_KEY_ISSUEDAT, new Date());
        claims.put(CLAIM_KEY_JWTID, sessionId);
        claims.put("seq", userSeq);
        return this.generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        logger.debug(claims.toString());

        return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate()).signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_ISSUEDAT, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        LoginUser user = (LoginUser) userDetails;
        final String username = this.getUsernameFromToken(token);
        // final Date created = getCreatedDateFromToken(token);
        // final Date expiration = getExpirationDateFromToken(token);
        // return (username.equals(user.getUsername()) && !isTokenExpired(token) && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
        return (username.equals(user.getUsername()) && !this.isTokenExpired(token));
    }

}
