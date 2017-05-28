/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 1.
 */
package com.realsnake.sample.service.uhdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.mapper.uhdb.UhdbMapper;
import com.realsnake.sample.mapper.user.UserMapper;
import com.realsnake.sample.model.ad.AdVo;
import com.realsnake.sample.model.common.SendVo;
import com.realsnake.sample.model.common.Sort;
import com.realsnake.sample.model.fcm.Data;
import com.realsnake.sample.model.fcm.FcmReqForm;
import com.realsnake.sample.model.fcm.Message;
import com.realsnake.sample.model.uhdb.AptVo;
import com.realsnake.sample.model.uhdb.UhdbDto;
import com.realsnake.sample.model.uhdb.UhdbLogVo;
import com.realsnake.sample.model.uhdb.UhdbVo;
import com.realsnake.sample.model.user.UserDto;
import com.realsnake.sample.model.user.UserFcmVo;
import com.realsnake.sample.model.user.UserUhdbVo;
import com.realsnake.sample.model.user.UserVo;
import com.realsnake.sample.service.ad.AdService;
import com.realsnake.sample.service.common.CommonService;
import com.realsnake.sample.util.FcmUtils;
import com.realsnake.sample.util.JdbcUtils;
import com.realsnake.sample.util.SmsUtils;

/**
 * <pre>
 * Class Name : UhdbServiceImpl.java
 * Description : Description
 * </pre>
 *
 * @author 전강욱(realsnake1975@gmail.com)
 * @since 2017. 5. 1.
 * @version 1.0
 */
@Service
public class UhdbServiceImpl implements UhdbService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UhdbMapper uhdbMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private FcmUtils fcmUtils;

    @Autowired
    private AdService adService;

    @Autowired
    private CommonService commonService;

    private static final String AD_URL = "/ad/%s";

    @Override
    @Transactional(readOnly = true)
    public List<AptVo> findAptList(AptVo param) throws Exception {
        return this.uhdbMapper.selectAptList(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UhdbVo> findUhdbList(UhdbVo param) throws Exception {
        return this.uhdbMapper.selectUhdbList(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyUhdbLog(UhdbLogVo param) throws Exception {
        if (StringUtils.isEmpty(param.getAptId())) {
            throw new Exception("아파트 아이디는 필수입니다!");
        }
        if (StringUtils.isEmpty(param.getAptPosi())) {
            throw new Exception("택배함 아이디는 필수입니다!");
        }
        if (StringUtils.isEmpty(param.getBoxNo())) {
            throw new Exception("보관함 번호는 필수입니다!");
        }

        String title = StringUtils.EMPTY;
        String body = StringUtils.EMPTY;
        String uhdbName = StringUtils.EMPTY;

        /** 10: 택배보관(택배기사), 20: 택배수령(고객), 30: 택배발송요청(고객), 40: 택배반품요청(고객), 50: 택배수령(택배기사) */
        if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_10.getCode())) {
            param.setUseYn("Y");
            param.setStDt(new Date());
            param.setEnDt(null);
            param.setAmt((double) 0);

            // 택배함 조회
            UhdbVo uhdb = new UhdbVo();
            uhdb.setAptId(param.getAptId());
            uhdb.setAptPosi(param.getAptPosi());
            List<UhdbVo> uhdbList = this.uhdbMapper.selectUhdbList(uhdb);

            if (uhdbList != null && !uhdbList.isEmpty()) {
                uhdbName = uhdbList.get(0).getAptPosiNm();
            }

            title = CommonConstants.SafeFuncType.SAFE_FUNC_10.getTitle();
            body = String.format(CommonConstants.SafeFuncType.SAFE_FUNC_10.getBody(), uhdbName, param.getBoxNo(), param.getPswd());
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_20.getCode())) {
            param.setUseYn("N");
            param.setStDt(null);
            param.setEnDt(new Date());
            param.setDong(null);
            param.setHo(null);
            param.setAmtGb(null);
            param.setAmt((double) 0);
            param.setTaekbaeHandphone(null);
            param.setTaekbaePswd(null);
            param.setTaekbae(null);
            param.setHandphone(null);
            param.setPswd(null);
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_30.getCode())) {
            param.setUseYn("Y");
            param.setStDt(new Date());
            param.setEnDt(null);
            param.setAmt((double) 0);
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_40.getCode())) {
            param.setUseYn("Y");
            param.setStDt(new Date());
            param.setEnDt(null);
            param.setAmt((double) 0);
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_50.getCode())) {
            param.setUseYn("N");
            param.setStDt(null);
            param.setEnDt(new Date());
            param.setDong(null);
            param.setHo(null);
            param.setAmtGb(null);
            param.setAmt((double) 0);
            param.setTaekbaeHandphone(null);
            param.setTaekbaePswd(null);
            param.setTaekbae(null);
            param.setHandphone(null);
            param.setPswd(null);

            // 택배함 조회
            UhdbVo uhdb = new UhdbVo();
            uhdb.setAptId(param.getAptId());
            uhdb.setAptPosi(param.getAptPosi());
            List<UhdbVo> uhdbList = this.uhdbMapper.selectUhdbList(uhdb);

            if (uhdbList != null && !uhdbList.isEmpty()) {
                uhdbName = uhdbList.get(0).getAptPosiNm();
            }

            title = CommonConstants.SafeFuncType.SAFE_FUNC_50.getTitle();
            body = String.format(CommonConstants.SafeFuncType.SAFE_FUNC_50.getBody(), uhdbName, param.getBoxNo());
        }

        this.uhdbMapper.updateUhdbLog(param);
        logger.info("<<무인택배함 로그 저장>> {}", param.toString());

        if (StringUtils.isEmpty(body)) {
            logger.info("<<무인택배함API {}, SMS / PUSH 미발송>>", param.getSafeFunc());
            return;
        }

        try {
            // 1. 핸드폰번호로 사용자 찾기
            UserUhdbVo userUhdb = new UserUhdbVo();
            userUhdb.setMobile(param.getHandphone());
            List<UserUhdbVo> userUhdbList = this.userMapper.selectUserUhdbList(userUhdb);

            if (userUhdbList == null || userUhdbList.isEmpty()) {
                // 2. 핸드폰번호에 해당하는 사용자가 없다면 아파트아이디, 택배함 위치, 동, 호로 사용자 찾기
                userUhdb = new UserUhdbVo();
                userUhdb.setAptId(param.getAptId());
                userUhdb.setUhdbId(param.getAptPosi());
                userUhdb.setDong(param.getDong());
                userUhdb.setHo(param.getHo());
                userUhdbList = this.userMapper.selectUserUhdbList(userUhdb);
            }

            if (userUhdbList == null || userUhdbList.isEmpty()) {
                // 3. 핸드폰번호로도 아파트아이디, 택배함 위치, 동, 호로도 사용자를 찾을 수 없다면 SMS 발송
                CompletableFuture<String> result = this.smsUtils.send(param.getHandphone(), body);

                // 발송 로그 저장
                SendVo send = new SendVo();
                send.setGubun(CommonConstants.SendType.SMS_UHDB.getValue());
                send.setMobile(param.getHandphone());
                send.setSendMessage(body);
                send.setResultMessage(result.get());
                this.commonService.regSendLog(send);

                logger.info("<<SMS 발송>> {}", send.toString());
            } else {
                List<Integer> userSeqList = new ArrayList<>();

                for (UserUhdbVo uu : userUhdbList) {
                    userSeqList.add(uu.getUserSeq());
                }

                UserDto userDto = new UserDto();
                userDto.setUserSeqList(userSeqList);

                List<UserVo> userList = this.userMapper.selectUserList(userDto);

                if (userList == null || userList.isEmpty()) {
                    // 정말 이상한 경우이다. 나올 수 없는 경우이다.
                } else {
                    // 4. FCM 발송
                    for (UserVo user : userList) {
                        UserFcmVo userFcm = new UserFcmVo();
                        userFcm.setUserSeq(user.getSeq());

                        // 광고 조회
                        String adUrl = null;
                        AdVo ad = this.adService.findRandomAd(null);
                        if (ad != null) {
                            adUrl = String.format(AD_URL, ad.getSeq());
                        }

                        UserFcmVo fcm = this.userMapper.selectUserFcm(userFcm);

                        Message message = new Message(title, body, adUrl);
                        FcmReqForm fcmReqForm = new FcmReqForm(new Data(message), fcm.getFcmToken());

                        CompletableFuture<String> result = this.fcmUtils.send(fcmReqForm);

                        // 발송 로그 저장
                        SendVo send = new SendVo();
                        send.setGubun(CommonConstants.SendType.PUSH_UHDB.getValue());
                        send.setMobile(user.getMobile());
                        send.setFcmToken(fcm.getFcmToken());
                        send.setSendMessage(fcmReqForm.toString());
                        send.setResultMessage(result.get());
                        this.commonService.regSendLog(send);

                        logger.info("<<PUSH 발송>> {}", send.toString());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("SMS 및 FCM 발송 중 오류", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UhdbLogVo> findUhdbLogList(UhdbDto param) throws Exception {
        if (param.getPagingHelper().getSortList() == null || param.getPagingHelper().getSortList().isEmpty()) {
            Sort sort = new Sort();
            sort.setColumn("seq");
            sort.setAscOrDesc(CommonConstants.SortType.DESC.getValue());

            List<Sort> sortList = new ArrayList<>();
            sortList.add(sort);

            param.getPagingHelper().setSortList(sortList);
        }

        param.getPagingHelper().setTotalCount(this.uhdbMapper.selectUhdbLogListCount(param));

        return this.uhdbMapper.selectUhdbLogList(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> findAptUhdbUserList(Integer param) throws Exception {
        return this.uhdbMapper.selectAptUhdbUserList(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String openBox(UhdbLogVo param) {
        try {
            // 1. 무인택배함 사용기록 조회
            UhdbLogVo uhdbLog = this.uhdbMapper.selectUhdbLog(param);

            if (uhdbLog != null) {
                return "USER NOT FOUND";
            }

            // 2. 아파트아이디와 무인택배함아이디로 무인택배함의 공인아이피 조회
            UhdbVo uv = new UhdbVo();
            uv.setAptId(param.getAptId());
            uv.setAptPosi(param.getAptPosi());
            List<UhdbVo> uhdbList = this.uhdbMapper.selectUhdbList(uv);

            if (uhdbList == null || uhdbList.isEmpty()) {
                // 무인택배함 부재
                return "UHDB NOT FOUND";
            }

            String gonginIp = uhdbList.get(0).getGonginIp();

            if (StringUtils.isEmpty(gonginIp)) {
                return "PUBLIC IP NOT FOUND";
            }

            // 3. 락커 오픈 실행(DB 업데이트)
            try {
                JdbcUtils ju = new JdbcUtils();
                ju.openBox(gonginIp, param.getAptId(), param.getAptPosi(), param.getBoxNo());
                logger.info("<<{} {} 무인택배함 {} 번 보관함이 열렸습니다.>>", param.getAptId(), param.getAptPosi(), param.getBoxNo());
            } catch (Exception e) {
                logger.error("<<무인택배함 보관함 열기 실패>>", e);
                return e.getMessage();
            }

            return "OK";
        } catch (Exception e) {
            logger.error("<<무인택배함 보관함 열기 실패>>", e);
            return e.getMessage();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyUhdbGonginIp(UhdbVo param) throws Exception {
        try {
            this.uhdbMapper.updateUhdbGonginIp(param);
            logger.info("<<무인택배함 공인아이피 수정>> {}", param.toString());
        } catch (Exception e) {
            logger.error("<<무인택배함 공인아이피 수정 실패>>", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String findUhdbUserPassword(UhdbLogVo param) throws Exception {
        return this.uhdbMapper.selectUhdbUserPassword(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyUhdbUserPassword(UhdbLogVo param) throws Exception {
        try {
            this.uhdbMapper.updateUhdbUserPassword(param);
            logger.info("<<무인택배함 세대 비밀번호 수정>> {}", param.toString());
        } catch (Exception e) {
            logger.error("<<무인택배함 세대 비밀번호 수정 실패>>", e);
        }
    }

}
