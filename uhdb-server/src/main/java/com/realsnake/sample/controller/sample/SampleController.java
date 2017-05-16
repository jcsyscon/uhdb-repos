package com.realsnake.sample.controller.sample;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.realsnake.sample.model.fcm.Content;
import com.realsnake.sample.model.fcm.Data;
import com.realsnake.sample.model.fcm.FcmReqForm;
import com.realsnake.sample.model.sample.SampleDto;
import com.realsnake.sample.model.sample.SampleVo;
import com.realsnake.sample.service.common.CommonService;
import com.realsnake.sample.service.sample.SampleService;
import com.realsnake.sample.util.EmailSender;
import com.realsnake.sample.util.PagingHelper;

@Controller
@RequestMapping(value = "/sample")
public class SampleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/reg")
    public String regSample(Model model, PagingHelper pagingHelper) throws Exception {
        SampleVo sample = new SampleVo();
        sample.setName("전강욱");

        this.sampleService.regSample(sample);
        LOGGER.debug("<<smaple>> {}", sample.toString());

        model.addAttribute("message", "OK");
        return "health";
    }

    @RequestMapping(value = "/list")
    public String findSampleList(Model model, PagingHelper pagingHelper, SampleDto param) throws Exception {
        LOGGER.debug("<<pagingHelper.toString()>>, {}", pagingHelper.toString());

        param.setPagingHelper(pagingHelper);

        List<SampleVo> sampleList = this.sampleService.findSampleList(param);

        if (sampleList == null || sampleList.isEmpty()) {
            LOGGER.debug("<<샘플 목록이 비어있습니다.>>");
        } else {
            for (SampleVo sample : sampleList) {
                LOGGER.debug("<<sample>> {}", sample.toString());
            }
        }

        model.addAttribute("message", "OK");
        return "health";
    }

    @GetMapping(value = "/redis")
    public String sampleRedis(Model model) throws Exception {
        // this.valueOperations.set("key", "12345678");
        // LOGGER.debug("<<key>> {}", this.valueOperations.get("key"));
        // this.valueOperations.set("name", "전강욱");
        // LOGGER.debug("<<name>> {}", this.valueOperations.get("name"));

        this.hashOperations.put("test:map", "name", "전강욱");
        this.hashOperations.put("test:map", "age", "43");
        // Map<String, String> testMap = this.hashOperations.entries("test:map");
        // LOGGER.debug("<<name>> {}", testMap.get("name"));
        // LOGGER.debug("<<age>> {}", testMap.get("age"));

        this.listOperations.rightPush("test:list", "12");
        this.listOperations.rightPush("test:list", "34");
        this.listOperations.rightPush("test:list", "56");
        this.listOperations.rightPush("test:list", "78");

        // String listValue = this.listOperations.leftPop("test:list");
        // while (listValue != null) {
        // LOGGER.debug("<<listValue>> {}", listValue);
        // listValue = listOperations.leftPop("test:list");
        // }

        return "index";
    }

    @GetMapping(value = "/redis/session")
    public String sampleRedisSession(HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession();
        session.setAttribute("userId", "realsnanke");
        LOGGER.debug("<<userId>> {}", session.getAttribute("userId"));

        return "index";
    }

    @GetMapping(value = "/email")
    public String sampleEmail(HttpServletRequest request, Model model) throws Exception {
        String[] attachFileNames = {"C:\\Users\\A\\Desktop\\이미지\\8680206301.jpg"};
        this.emailSender.sendTextWithAttach("neverchangepwd@jahasmart.com", "realsnake@jahasmart.com", "메일 제목입니다.", "메일내용입니다. 하하하하하하하하하", attachFileNames);

        return "index";
    }

    @GetMapping(value = "/fcm")
    @ResponseBody
    public FcmReqForm sampleFcm(HttpServletRequest request, Model model) throws Exception {
        Content content = new Content("제목입니다.", "내용입니다.");
        return new FcmReqForm(new Data(content), "FCM 토큰");
    }

    @GetMapping(value = "/juso/new/{gubun}")
    @ResponseBody
    public String sampleJuso(@PathVariable("gubun") String gubun) throws Exception {
        try {
            // gubun: road, jibun, build
            this.commonService.regJuso(gubun);
            return "OK";
        } catch (Exception e) {
            return "NOK";
        }
    }

}
