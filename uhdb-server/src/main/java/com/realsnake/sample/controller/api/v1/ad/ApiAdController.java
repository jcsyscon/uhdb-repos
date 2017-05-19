package com.realsnake.sample.controller.api.v1.ad;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realsnake.sample.config.sec.JwtToken;
import com.realsnake.sample.config.sec.JwtTokenUtil;
import com.realsnake.sample.constants.ApiResultCode;
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.common.api.ApiResponse;
import com.realsnake.sample.util.MobilePagingHelper;

@RestController("ApiV1AdController")
@RequestMapping(value = "/api/v1/ad")
public class ApiAdController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAdController.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.token.header}")
    private String jwtTokenHeader;

    @GetMapping(value = "/login")
    public ApiResponse<?> login(MobilePagingHelper mobilePagingHelper) throws CommonApiException {
        LOGGER.debug("<<mobilePagingHelper.toString()>>, {}", mobilePagingHelper.toString());

        ApiResponse<MobilePagingHelper> apiResponse = new ApiResponse<>();
        apiResponse.setBody(mobilePagingHelper);

        return apiResponse;
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
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

}
