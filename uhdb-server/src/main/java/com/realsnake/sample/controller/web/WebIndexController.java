package com.realsnake.sample.controller.web;

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

import com.realsnake.sample.model.user.UserDto;
import com.realsnake.sample.model.user.UserVo;
import com.realsnake.sample.service.user.UserService;
import com.realsnake.sample.util.PagingHelper;

@Controller
@RequestMapping(value = "/")
public class WebIndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebIndexController.class);

    @Autowired
    private UserService userService;

    @RequestMapping
    public String index() throws Exception {
        // return "index";
        return "redirect:/main";
    }

    @GetMapping(value = "/login")
    public String moveLoginPage() throws Exception {
        return "login";
    }

    @GetMapping(value = "/main")
    public String moveMainPage(Model model, PagingHelper pagingHelper, UserDto param) throws Exception {
        LOGGER.debug("<<UserDto>> {}", param.toString());

        List<UserVo> userList = this.userService.findUserList(param);
        model.addAttribute("userList", userList);

        // return "main";
        return "user/list";
    }

    /**
     * 회원가입 페이지 이동
     *
     * @param gubun user / admin
     * @return
     * @throws Exception
     */
    @GetMapping(value = "{gubun}/join")
    public String moveJoinPage(Model model, @PathVariable("gubun") String gubun) throws Exception {
        model.addAttribute("gubun", gubun);
        return gubun + "/join";
    }

    @PostMapping(value = "{gubun}/join")
    public String joinProcessing(@PathVariable("gubun") String gubun, UserVo param) throws Exception {
        this.userService.regUser(param);
        return "redirect:/login";
    }

}
