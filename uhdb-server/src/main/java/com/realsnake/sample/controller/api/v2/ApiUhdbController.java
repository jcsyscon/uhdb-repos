package com.realsnake.sample.controller.api.v2;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.realsnake.sample.constants.ApiResultCode;
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.common.api.ApiResponse;
import com.realsnake.sample.model.uhdb.UhdbDto;
import com.realsnake.sample.model.uhdb.UhdbLogVo;
import com.realsnake.sample.model.uhdb.UhdbVo;
import com.realsnake.sample.service.uhdb.UhdbService;
import com.realsnake.sample.util.MobilePagingHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="무인택배보관함", description="무인택배보관함 관련 API들")
@RestController("ApiV2UhdbController")
@RequestMapping(value = "/api/v2/uhdb")
public class ApiUhdbController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUhdbController.class);

    @Autowired
    private UhdbService uhdbService;
    
    /**
     * 무인택배함 조회
     *
     * @param uhdbNo TODO: 형식: 아파트아이디-아파트위치
     * @return
     * @throws CommonApiException
     */
    @ApiOperation(value = "무인택배함 조회", response = ApiResponse.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "uhdbNo", value = "무인택배함번호(아파트아이디-아파트위치)", required = true, dataType = "string", paramType = "path", defaultValue = "")
    })
    @PostMapping(value = "/{uhdbNo}")
    @Deprecated
    public ApiResponse<?> getUhdb(@PathVariable("uhdbNo") String uhdbNo) throws CommonApiException {
        try {            
            String[] temps = uhdbNo.split("-", -1);
            String aptId = temps[0];
            String aptPosi = temps[1];

            UhdbVo param = new UhdbVo();
            param.setAptId(aptId);
            param.setAptPosi(aptPosi);

            List<UhdbVo> uhdbList = this.uhdbService.findUhdbList(param);
            UhdbVo uhdb = null;
            if (uhdbList != null && !uhdbList.isEmpty()) {
                uhdb = uhdbList.get(0);
            }

            ApiResponse<UhdbVo> apiResponse = new ApiResponse<>();
            apiResponse.setBody(uhdb);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 무인택배함 보관함 열기(앱)
     *
     * @param uhdbNo
     * @param boxNo
     * @param password
     * @return
     * @throws CommonApiException
     */
    @ApiOperation(value = "무인택배함 보관함 열기(앱)", response = ApiResponse.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "uhdbNo", value = "무인택배함번호(아파트아이디-아파트위치)", required = true, dataType = "string", paramType = "query", defaultValue = "")
        , @ApiImplicitParam(name = "boxNo", value = "무인택배함 박스번호", required = true, dataType = "string", paramType = "query", defaultValue = "")
        , @ApiImplicitParam(name = "password", value = "비밀번호", required = true, dataType = "string", paramType = "query", defaultValue = "")
        , @ApiImplicitParam(name = "tbcode", value = "tb코드", required = true, dataType = "string", paramType = "query", defaultValue = "")
    })
    @PostMapping(value = "/open")
    public ApiResponse<?> openBox(String uhdbNo, String boxNo, String password, @RequestParam(value = "tbcode", required = false, defaultValue = StringUtils.EMPTY) String tbcode) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<무인택배함 보관함 열기(/uhdb/open)>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            String[] temps = uhdbNo.split("-", -1);
            String aptId = temps[0];
            String aptPosi = temps[1];
            
            UhdbLogVo param = new UhdbLogVo();
            param.setAptId(aptId);
            param.setAptPosi(aptPosi);
            param.setBoxNo(boxNo);
            param.setPswd(password);
            param.setTbcode(tbcode);
            String result = this.uhdbService.openBox(param);

            ApiResponse<String> apiResponse = new ApiResponse<>();
            apiResponse.setBody(result);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 무인택배함 보관함 초기화(앱에서 보관함 열기 중 닫기/확인/고객센터 버튼 클릭 시)
     *
     * @param uhdbNo
     * @param boxNo
     * @return
     * @throws CommonApiException
     */
    @ApiOperation(value = "무인택배함 보관함 초기화(앱에서 보관함 열기 중 닫기/확인/고객센터 버튼 클릭 시)", response = ApiResponse.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "uhdbNo", value = "무인택배함번호(아파트아이디-아파트위치)", required = true, dataType = "string", paramType = "query", defaultValue = "")
        , @ApiImplicitParam(name = "boxNo", value = "무인택배함 박스번호", required = true, dataType = "string", paramType = "query", defaultValue = "")
    })
    @PostMapping(value = "/init")
    public ApiResponse<?> initBox(String uhdbNo, String boxNo) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<무인택배함 보관함 초기화(/uhdb/init)>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            String[] temps = uhdbNo.split("-", -1);
            String aptId = temps[0];
            String aptPosi = temps[1];
            
            UhdbLogVo param = new UhdbLogVo();
            param.setAptId(aptId);
            param.setAptPosi(aptPosi);
            param.setBoxNo(boxNo);
            String result = this.uhdbService.initBox(param);

            ApiResponse<String> apiResponse = new ApiResponse<>();
            apiResponse.setBody(result);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    /**
     * 무인택배함 보관함 사용내역 조회(앱)
     *
     * @param gubun past / now
     * @param userSeq
     * @param mobilePagingHelper
     * @param param
     * @return
     * @throws CommonApiException
     */
    @ApiOperation(value = "무인택배함 보관함 사용내역 조회(앱)", response = ApiResponse.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "gubun", value = "구분(past/now)", required = true, dataType = "string", paramType = "path", defaultValue = "")
        , @ApiImplicitParam(name = "userSeq", value = "회원일련번호", required = true, dataType = "int", paramType = "path", defaultValue = "")
    })
    @PostMapping(value = "/log/{gubun}/{userSeq}")
    public ApiResponse<?> searchUhdbLogList(@PathVariable("gubun") String gubun, @PathVariable("userSeq") Integer userSeq, MobilePagingHelper mobilePagingHelper, UhdbDto param) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                LOGGER.debug("<<무인택배함 사용내역 조회(/log/{}/{})>> 인증 실패", gubun, userSeq);
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            param.setGubun(gubun);
            param.setUserSeq(userSeq);

            List<UhdbLogVo> uhdbLogList = this.uhdbService.findUhdbLogList4Mobile(param);

            ApiResponse<List<UhdbLogVo>> apiResponse = new ApiResponse<>(mobilePagingHelper.getNextPageToken(), mobilePagingHelper.getPageSize());
            apiResponse.setBody(uhdbLogList);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

}
