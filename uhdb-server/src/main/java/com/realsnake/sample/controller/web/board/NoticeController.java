package com.realsnake.sample.controller.web.board;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.realsnake.sample.model.board.BoardDto;
import com.realsnake.sample.model.board.NoticeVo;
import com.realsnake.sample.service.board.NoticeService;
import com.realsnake.sample.util.PagingHelper;

@Controller
@RequestMapping(value = "/notice")
public class NoticeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public String index() throws Exception {
        // return "index";
        return "redirect:/notice/list";
    }

    @GetMapping(value = "/list")
    public String moveNoticeListPage(Model model, PagingHelper pagingHelper, BoardDto param) throws Exception {
        LOGGER.debug("<<BoardDto>> {}", param.toString());

        List<NoticeVo> noticeList = this.noticeService.findNoticeList(param);
        model.addAttribute("noticeList", noticeList);

        return "board/notice/list";
    }

    @GetMapping(value = "/reg")
    public String moveNoticeRegPage() throws Exception {
        return "board/notice/reg";
    }

    @PostMapping(value = "/reg")
    public String regNotice(BoardDto param, NoticeVo notice) throws Exception {
        this.noticeService.regNotice(param, notice);
        return "redirect:/notice/list";
    }

    @GetMapping(value = "/{seq}")
    public String moveNoticeViewPage(Model model, @PathVariable("seq") Integer seq) throws Exception {
        NoticeVo notice = this.noticeService.findNotice(seq);
        model.addAttribute("notice", notice);
        return "board/notice/view";
    }

    @PostMapping(value = "/modify/{seq}")
    public String modifyNotice(@PathVariable("seq") Integer seq, BoardDto param, NoticeVo notice) throws Exception {
        this.noticeService.modifyNotice(param, notice);
        return "redirect:/notice/list";
    }

}
