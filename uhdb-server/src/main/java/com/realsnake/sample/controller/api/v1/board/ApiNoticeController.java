package com.realsnake.sample.controller.api.v1.board;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realsnake.sample.constants.ApiResultCode;
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.board.BoardDto;
import com.realsnake.sample.model.board.NoticeVo;
import com.realsnake.sample.model.common.api.ApiResponse;
import com.realsnake.sample.service.board.NoticeService;
import com.realsnake.sample.util.MobilePagingHelper;

@RestController("ApiV1NoticeController")
@RequestMapping(value = "/api/v1/board/notice")
public class ApiNoticeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiNoticeController.class);

    @Autowired
    private NoticeService noticeService;

    @GetMapping(value = "/list")
    public ApiResponse<?> getNoticeList(MobilePagingHelper mobilePagingHelper, BoardDto param) throws CommonApiException {
        LOGGER.debug("<<mobilePagingHelper.toString()>>, {}", mobilePagingHelper.toString());

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiNoticeController.getNoticeList>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            List<NoticeVo> noticeList = this.noticeService.findNoticeList4Mobile(param);

            ApiResponse<List<NoticeVo>> apiResponse = new ApiResponse<>(mobilePagingHelper.getNextPageToken(), mobilePagingHelper.getPageSize());
            apiResponse.setBody(noticeList);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

    @GetMapping(value = "/{seq}")
    public ApiResponse<?> getNotice(@PathVariable("seq") Integer seq) throws CommonApiException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOGGER.debug("<<ApiNoticeController.getNotice>> 인증 실패");
                throw new CommonApiException(ApiResultCode.NOTFOUND_USER);
            }

            NoticeVo notice = this.noticeService.findNotice(seq);

            ApiResponse<NoticeVo> apiResponse = new ApiResponse<>();
            apiResponse.setBody(notice);

            return apiResponse;
        } catch (Exception e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

}
