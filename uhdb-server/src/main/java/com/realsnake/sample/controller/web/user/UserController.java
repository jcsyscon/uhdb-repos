package com.realsnake.sample.controller.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.realsnake.sample.constants.ApiResultCode;
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.common.api.ApiResponse;
import com.realsnake.sample.model.user.UserVo;
import com.realsnake.sample.service.user.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 중복체크
     *
     * @param type username / email
     * @param param
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/double-check/{type}/{param}")
    @ResponseBody
    public String doubleCheckProcessing(@PathVariable("type") String type, @PathVariable("param") String param) throws Exception {
        return this.userService.checkDoubleUser(type, param);
    }

    @GetMapping(value = "/{seq}")
    public String moveUserViewPage(Model model, @PathVariable("seq") Integer seq) throws Exception {
        UserVo user = this.userService.findUser(seq);
        model.addAttribute("user", user);
        return "user/view";
    }

    /**
     * 회원 탈퇴
     *
     * @param seq
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/secede/{seq}")
    public String secedeUserByAdmin(@PathVariable("seq") Integer seq) throws Exception {
        UserVo user = new UserVo();
        user.setSeq(seq);

        this.userService.secedeUser(user);
        
        return "redirect:/main";
    }
    
}
