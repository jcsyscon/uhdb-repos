package com.realsnake.sample.controller.api.v1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realsnake.sample.config.sec.JwtToken;
import com.realsnake.sample.config.sec.JwtTokenUtil;
import com.realsnake.sample.constants.ApiResultCode;
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.common.api.ApiResponse;
import com.realsnake.sample.model.token.AuthRequestToken;
import com.realsnake.sample.model.token.AuthToken;
import com.realsnake.sample.model.user.LoginUser;

@RestController("ApiV1AuthController")
@RequestMapping(value = "/api/v1")
public class ApiAuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAuthController.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${jwt.token.header}")
    private String jwtTokenHeader;

    @PostMapping(value = "/login")
    public ApiResponse<?> loginProcessing(HttpSession session, AuthRequestToken param) throws CommonApiException {
        LOGGER.debug("<<x-auth-token, AuthRequestToken.toString()>>, {}", param.toString());

        try {
            ApiResponse<AuthToken> apiResponse = new ApiResponse<>();

            UsernamePasswordAuthenticationToken requestToken = new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword());
            Authentication authentication = this.authenticationManager.authenticate(requestToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

            AuthToken authToken = new AuthToken(session.getId(), loginUser.getSeq(), loginUser.getUsername());
            apiResponse.setBody(authToken);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.NOTFOUND_USER, e);
        }
    }

    /**
     * 인증 토큰(x-auth-token), Access 토큰(JWT, x-access-token) 발급요청 <br />
     *
     * @param session
     * @param device
     * @param param
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthToken(HttpSession session, Device device, AuthRequestToken param) throws CommonApiException {
        LOGGER.debug("<</auth, AuthRequestToken.toString()>>, {}", param.toString());

        try {
            UsernamePasswordAuthenticationToken requestToken = new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword());
            Authentication authentication = this.authenticationManager.authenticate(requestToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload password post-security so we can generate token
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            final String accessToken = this.jwtTokenUtil.generateToken(loginUser, session.getId());
            LOGGER.debug("<<accessToken>> {}", accessToken);

            return ResponseEntity.ok(new JwtToken(accessToken));
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.NOTFOUND_USER, e);
        }
    }

    /**
     * Access 토큰(JWT, x-access-token) 갱신 요청<br />
     * RestAuthenticationFilter에서 먼저 토큰 인증 과정을 거치게 됨
     *
     * @param request
     * @return
     * @throws CommonApiException
     */
    @PostMapping(value = "/auth/refresh")
    public ResponseEntity<?> refreshAuthToken(HttpServletRequest request) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<</auth/refresh>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            String accessToken = request.getHeader(this.jwtTokenHeader);
            String username = authentication.getName(); // this.jwtTokenUtil.getUsernameFromToken(accessToken);
            LOGGER.debug("<</auth/refresh>> username: {}", username);

            if (this.jwtTokenUtil.canTokenBeRefreshed(accessToken)) {
                final String refreshToken = this.jwtTokenUtil.refreshToken(accessToken);
                // TODO: accessToken 폐기
                return ResponseEntity.ok(new JwtToken(refreshToken));
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.NOTFOUND_USER, e);
        }
    }

}
