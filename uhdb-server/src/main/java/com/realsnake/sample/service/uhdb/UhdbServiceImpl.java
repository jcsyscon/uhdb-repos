/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 1.
 */
package com.realsnake.sample.service.uhdb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.mapper.uhdb.UhdbMapper;
import com.realsnake.sample.mapper.user.UserMapper;
import com.realsnake.sample.model.common.SendVo;
import com.realsnake.sample.model.common.Sort;
import com.realsnake.sample.model.fcm.Data;
import com.realsnake.sample.model.fcm.FcmReqForm;
import com.realsnake.sample.model.fcm.Message;
import com.realsnake.sample.model.uhdb.AptVo;
import com.realsnake.sample.model.uhdb.NfcVo;
import com.realsnake.sample.model.uhdb.UhdbDto;
import com.realsnake.sample.model.uhdb.UhdbLogVo;
import com.realsnake.sample.model.uhdb.UhdbVo;
import com.realsnake.sample.model.user.UserDto;
import com.realsnake.sample.model.user.UserFcmVo;
import com.realsnake.sample.model.user.UserUhdbVo;
import com.realsnake.sample.model.user.UserVo;
import com.realsnake.sample.service.common.CommonService;
import com.realsnake.sample.util.FcmUtils;
import com.realsnake.sample.util.JdbcUtils;
import com.realsnake.sample.util.SmsUtils;
import com.realsnake.sample.util.crypto.BlockCipherUtils;

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
    private CommonService commonService;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    private static final String PUSH_AD_URL = "/api/v1/ad/gubun/push";

    private ObjectMapper mapper = new ObjectMapper();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);

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

    private String removeZero(String param) {
        try {
            return StringUtils.remove(param, "0");
        } catch (Exception e) {
            return param;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyUhdbLog(UhdbLogVo param) throws Exception {
        logger.info("<<무인택배함 로그>> {}", param.toString());

        if (StringUtils.isEmpty(param.getAptId())) {
            throw new Exception("아파트 아이디는 필수입니다!");
        }
        if (StringUtils.isEmpty(param.getAptPosi())) {
            throw new Exception("택배함 아이디는 필수입니다!");
        }
        if (StringUtils.isEmpty(param.getBoxNo())) {
            throw new Exception("보관함 번호는 필수입니다!");
        }

        String title4User = StringUtils.EMPTY;
        String body4User = StringUtils.EMPTY;
        String userMobile = StringUtils.EMPTY;

        String body4Tb = StringUtils.EMPTY; // 택배기사용 문구
        String tbMobile = StringUtils.EMPTY; // 택배기사 핸드폰번호

        String aptsName = StringUtils.EMPTY;
        String aptPosiName = StringUtils.EMPTY;
        String nowYmd = StringUtils.EMPTY;

        String aptId = param.getAptId();
        String dong = param.getDong();
        String ho = param.getHo();

        try {
            AptVo aptParam = new AptVo();
            aptParam.setAptId(param.getAptId());
            List<AptVo> aptList = this.uhdbMapper.selectAptList(aptParam);
            aptsName = aptList.get(0).getAptsNm();

            UhdbVo uhdbParam = new UhdbVo();
            uhdbParam.setAptId(param.getAptId());
            uhdbParam.setAptPosi(param.getAptPosi());
            List<UhdbVo> uhdbList = this.uhdbMapper.selectUhdbList(uhdbParam);
            aptPosiName = uhdbList.get(0).getAptPosiNm();

            nowYmd = sdf.format(new Date());
        } catch (Exception e) {
            logger.error("<<modifyUhdbLog 오류>>", e);
        }

        /** 10: 택배보관(택배기사), 20: 택배수령(고객), 30: 택배발송요청(고객), 40: 택배수령(택배기사), 50: 택배반품요청(고객) */
        if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_10.getCode())) { // 택배보관(택배기사)
            Date nowDate = new Date();
            userMobile = param.getHandphone();
            title4User = CommonConstants.SafeFuncType.SAFE_FUNC_10.getTitle();
            body4User = String.format(CommonConstants.SafeFuncType.SAFE_FUNC_10.getBody(), aptPosiName, this.removeZero(param.getBoxNo()), param.getPswd(), param.getDong(), param.getHo(),
                    param.getTaekbae());

            tbMobile = param.getTaekbaeHandphone();
            // 택배기사 발송 문자
            // 아파트명 택배함설치장소명 인증번호:taekbae_PSWD 물품교체/보관함 열리지 않았을때 사용
            // - tb_ap0101.aptSnm tb_ap0102.aptPosiNM 인증번호:tb_bx0201.taekbae_pswd 물품교체/보관함이 열리지 않았을 때 사용
            body4Tb = "%s %s 인증번호:%s 물품교체/보관함 열리지 않았을때 사용<고객용비번아님>";
            body4Tb = String.format(body4Tb, aptsName, aptPosiName, param.getTaekbaePswd());

            param.setUseYn("Y");
            param.setStDt(nowDate);
            param.setEnDt(null);
            if (StringUtils.isEmpty(param.getAmtGb())) {
                param.setAmtGb(null);
            }
            if (param.getAmt() == null) {
                param.setAmt((double) 0);
            }

            // <!-- 택배기사에게 하루에 한번만 문자 보내도록 체크
            String checkTb4SmsKey = param.getTaekbae() + "|" + param.getTaekbaeHandphone();
            String checkTb4SmsValue = CommonConstants.SHORT_DATE_SDF.format(nowDate);

            String redisValue = this.valueOperations.get(checkTb4SmsKey);

            if (checkTb4SmsValue.equals(redisValue)) {
                logger.debug("<<택배기사에게 오늘 날짜로 safeFunc 10번 관련 안내 문자를 한번 발송했기 때문에 더 이상 발송하지 않는다>>");
                body4Tb = StringUtils.EMPTY;
            } else {
                this.valueOperations.set(checkTb4SmsKey, checkTb4SmsValue);
            }
            // 택배기사에게 하루에 한번만 문자 보내도록 체크 -->
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_20.getCode())) { // 택배수령(고객)
            param.setSafeFunc(null);
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
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_30.getCode())) { // 택배발송요청(고객)
            tbMobile = param.getTaekbaeHandphone();
            // 택배기사 발송 문자
            // 아파트명 택배함설치장소명 2017.07.08 1201동 1301호 001번함 택배보관 PW:12345(taekbae_pswd)
            // - tb_ap0101.aptSnm tb_ap0102.aptPosiNM 현재날자 tb_bx0201.dong동 tb_bx0201.ho호 tb_bx0201.boxno번함 택배보관 PW:tb_bx0201. taekbae_pswd
            body4Tb = "%s %s %s %s동 %s호 %s번함 택배보관, PW:%s";
            body4Tb = String.format(body4Tb, aptsName, aptPosiName, nowYmd, param.getDong(), param.getHo(), this.removeZero(param.getBoxNo()), param.getTaekbaePswd());

            param.setUseYn("Y");
            param.setStDt(new Date());
            param.setEnDt(null);
            if (StringUtils.isEmpty(param.getAmtGb())) {
                param.setAmtGb(null);
            }
            if (param.getAmt() == null) {
                param.setAmt((double) 0);
            }
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_40.getCode())) { // 택배수령(택배기사)
            UhdbLogVo uhdbLog = this.uhdbMapper.selectUhdbLog(param);

            userMobile = uhdbLog.getHandphone();
            title4User = CommonConstants.SafeFuncType.SAFE_FUNC_40.getTitle();
            // 고객 푸시 발송
            // 아파트명 택배함설치장소명 2017.07.08 1201동 1301호 001번 보관함에 물품 배송이 되었습니다.
            // - tb_ap0101.aptSnm tb_ap0102.aptPosiNM 현재날자 tb_bx0201.dong동 tb_bx0201.ho호 tb_bx0201.boxno번 보관함에 물품 배송이 되었습니다.

            body4User = "%s %s %s %s동 %s호 %s번 보관함 물품이 배송되었습니다.";
            body4User = String.format(body4User, aptsName, aptPosiName, nowYmd, uhdbLog.getDong(), uhdbLog.getHo(), this.removeZero(param.getBoxNo()), uhdbLog.getTaekbaePswd());

            param.setSafeFunc(null);
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
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_50.getCode())) { // 택배반품요청(고객)
            tbMobile = param.getTaekbaeHandphone();
            // 택배기사 발송 문자
            // 아파트명 택배함설치장소명 2017.07.08 1201동 1301호 001번함 반품택배보관 PW:12345(taekbae_pswd)
            // - tb_ap0101.aptSnm tb_ap0102.aptPosiNM 현재날자 tb_bx0201.dong동 tb_bx0201.ho호 tb_bx0201.boxno번함 택배보관 PW:tb_bx0201. taekbae_pswd
            body4Tb = "%s %s %s %s동 %s호 %s번함 반품택배보관, PW:%s";
            body4Tb = String.format(body4Tb, aptsName, aptPosiName, nowYmd, param.getDong(), param.getHo(), this.removeZero(param.getBoxNo()), param.getTaekbaePswd());

            param.setUseYn("Y");
            param.setStDt(new Date());
            param.setEnDt(null);
            if (StringUtils.isEmpty(param.getAmtGb())) {
                param.setAmtGb(null);
            }
            if (param.getAmt() == null) {
                param.setAmt((double) 0);
            }
        }

        this.uhdbMapper.updateUhdbLog(param);
        // logger.info("<<무인택배함 로그 저장 완료>>");

        if ("SAFE_INS".equalsIgnoreCase(param.getUserId())) {
            logger.debug("<<보관함 데이터 초기화>>");
            this.initBox(param);
            return;
        }

        if (StringUtils.isNotEmpty(body4Tb)) {
            try {
                // 택배기사용 문자 발송
                CompletableFuture<String> result = this.smsUtils.send(tbMobile, body4Tb);

                // 발송 로그 저장
                SendVo send = new SendVo();
                send.setGubun(CommonConstants.SendType.SMS_UHDB.getValue());
                send.setMobile(tbMobile);
                send.setSendMessage(body4Tb);
                send.setResultMessage(result.get());
                this.commonService.regSendLog(send);

                logger.info("<<무인택배함 로그 API, 택배기사 SMS 발송>> {}", send.toString());
            } catch (Exception e) {
                logger.error("<<무인택배함 로그 API, 택배기사 문자발송 중 오류>>", e);
            }
        }

        if (StringUtils.isEmpty(body4User)) {
            logger.info("<<무인택배함 로그 API {}, 사용자에게 SMS / PUSH 미발송>>", param.getSafeFunc());
            return;
        }

        String secretKey = null;

        try {
            // 1. 핸드폰번호로 사용자 찾기
            UserUhdbVo userUhdbParam = new UserUhdbVo();

            String mobileNumber = userMobile.substring(0, 3) + "-" + userMobile.substring(3, 7) + "-" + userMobile.substring(7, 11);
            secretKey = BlockCipherUtils.generateSecretKey(CommonConstants.DEFAULT_AUTH_KEY);
            String encMobileNumber = BlockCipherUtils.encrypt(secretKey, mobileNumber);

            userUhdbParam.setMobile(encMobileNumber);
            List<UserUhdbVo> userUhdbList = this.userMapper.selectUserUhdbList(userUhdbParam);

            /* @formatter:off */
            /**
            if (userUhdbList == null || userUhdbList.isEmpty()) {
                // 2. 핸드폰번호에 해당하는 사용자가 없다면 아파트아이디, 택배함 위치, 동, 호로 사용자 찾기
                // -> 20170810, 핸드폰번호에 해당하는 회원가입자가 없다면 해당 번호로 문자만 발송으로 변경
                userUhdb = new UserUhdbVo();
                userUhdb.setAptId(param.getAptId());
                userUhdb.setUhdbId(param.getAptPosi());
                userUhdb.setDong(param.getDong());
                userUhdb.setHo(param.getHo());
                userUhdbList = this.userMapper.selectUserUhdbList(userUhdb);
            }
            */
            /* @formatter:on */

            /* @formatter:off */
            if (userUhdbList == null || userUhdbList.isEmpty()) {
                // 2. 핸드폰번호로 사용자를 찾을 수 없다면(회원미가입자) 택배+앱설치권유 SMS 발송
                CompletableFuture<String> result = this.smsUtils.send(userMobile, body4User);

                // 발송 로그 저장
                SendVo send = new SendVo();
                send.setGubun(CommonConstants.SendType.SMS_UHDB.getValue());
                send.setMobile(userMobile);
                send.setSendMessage(body4User);
                send.setResultMessage(result.get());
                this.commonService.regSendLog(send);

                logger.info("<<무인택배함 로그 API, 사용자 SMS 발송, 회원미가입자>> {}", send.toString());

                // <!-- 택배요 설치 문자 발송
                String applyText = "택배요 앱을 다운,설치하세요 https://play.google.com/store/apps/details?id=kr.co.tbyo.and";

                CompletableFuture<String> applyResult = this.smsUtils.send(userMobile, applyText);

                // 발송 로그 저장
                send = new SendVo();
                send.setGubun(CommonConstants.SendType.SMS_APPLY.getValue());
                send.setMobile(userMobile);
                send.setSendMessage(applyText);
                send.setResultMessage(applyResult.get());
                this.commonService.regSendLog(send);

                logger.info("<<무인택배함 로그 API, 앱설치권유 SMS 발송>> {}", send.toString());
                // 택배요 설치 문자 발송 -->

                // logger.info("<<무인택배함 로그 API, 회원미가입자에게 SMS 발송해야하지만 현재 SMS 발송은 주석처리되어 있으므로 SMS 발송하지 않고 종료>>");
                return;
            }
            else {
            	userUhdbParam.setAptId(aptId);
            	userUhdbParam.setDong(dong);
            	userUhdbParam.setHo(ho);
            	
            	UserUhdbVo userUhdb = this.userMapper.selectMemberYn(userUhdbParam);

                if ( userUhdb == null ) {
                    // 2. 핸드폰번호로 사용자를 찾았는데 아파트아이디/동/호가 틀리다면 택배 SMS 발송
                    CompletableFuture<String> result = this.smsUtils.send(userMobile, body4User);

                    // 발송 로그 저장
                    SendVo send = new SendVo();
                    send.setGubun(CommonConstants.SendType.SMS_UHDB.getValue());
                    send.setMobile(userMobile);
                    send.setSendMessage(body4User);
                    send.setResultMessage(result.get());
                    this.commonService.regSendLog(send);

                    logger.info("<<무인택배함 로그 API, 사용자 SMS 발송, 핸드폰번호로 사용자를 찾았는데 아파트아이디/동/호가 다른 경우>> {}", send.toString());
                    return;
                }
            }
            /* @formatter:on */

            List<Integer> userSeqList = new ArrayList<>();

            for (UserUhdbVo uu : userUhdbList) {
                userSeqList.add(uu.getUserSeq());
            }

            UserDto userDto = new UserDto();
            userDto.setUserSeqList(userSeqList);

            List<UserVo> userList = this.userMapper.selectUserList(userDto);

            if (userList == null || userList.isEmpty()) {
                // 정말 이상한 경우이다. 나올 수 없는 경우이다.
                return;
            }

            // 4. FCM 발송
            for (UserVo user : userList) {
                UserFcmVo userFcm = new UserFcmVo();
                userFcm.setUserSeq(user.getSeq());

                UserFcmVo fcm = this.userMapper.selectUserFcm(userFcm);
                if (fcm == null) {
                    continue;
                }

                Message message = new Message(title4User, body4User, PUSH_AD_URL);
                FcmReqForm fcmReqForm = new FcmReqForm(new Data(message), fcm.getFcmToken());

                CompletableFuture<String> result = this.fcmUtils.send(fcmReqForm);

                // 발송 로그 저장
                SendVo send = new SendVo();
                send.setGubun(CommonConstants.SendType.PUSH_UHDB.getValue());
                send.setMobile(BlockCipherUtils.decrypt(secretKey, user.getMobile()));
                send.setFcmToken(fcm.getFcmToken());
                send.setSendMessage(this.mapper.writeValueAsString(fcmReqForm));
                send.setResultMessage(result.get());
                this.commonService.regSendLog(send);

                logger.info("<<무인택배함 로그 API, 사용자 PUSH 발송>> {}", send.toString());
            }
        } catch (Exception e) {
            logger.error("<<SMS 및 FCM 발송 중 오류>>", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UhdbLogVo> findUhdbLogList(UhdbDto param) throws Exception {
        if (param.getPagingHelper().getSortList() == null || param.getPagingHelper().getSortList().isEmpty()) {
            Sort sort = new Sort();
            sort.setColumn("upddt");
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
            // 0. 인증코드 조회
            UhdbLogVo ul = this.uhdbMapper.selectAuthcode(param);

            if (ul == null || StringUtils.isBlank(ul.getTbcode())) {
                return "AUTHCODE NOT FOUND";
            }
            if (!ul.getTbcode().equals(param.getTbcode())) {
                logger.info("<<AUTHCODE NOT MATCH>>");
                return "AUTHCODE NOT MATCH";
            }

            // 1. 무인택배함 사용기록 조회
            UhdbLogVo uhdbLog = this.uhdbMapper.selectUhdbLog(param);

            if (uhdbLog == null) {
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

            // logger.debug("<<무인택배함 정보>> {}", uhdbList.get(0).toString());
            String gonginIp = uhdbList.get(0).getGonginIp();
            String portFw = StringUtils.defaultIfEmpty(uhdbList.get(0).getPortFw(), "3306");

            if (StringUtils.isEmpty(gonginIp)) {
                return "PUBLIC IP NOT FOUND";
            }

            // 3. 락커 오픈 실행(DB 업데이트)
            int updateCount = 0;

            try {
                JdbcUtils ju = new JdbcUtils();
                updateCount = ju.openBox(gonginIp, portFw, param.getAptId(), param.getAptPosi(), param.getBoxNo());
            } catch (Exception e) {
                logger.error("<<무인택배함 보관함 열기 실패>>", e);
                return e.getMessage();
            }

            if (updateCount > 0) {
                logger.info("<<{} {} 무인택배함 {} 번 보관함이 열렸습니다.>>", param.getAptId(), param.getAptPosi(), param.getBoxNo());
                return "OK";
            } else {
                return "NOK";
            }
        } catch (Exception e) {
            logger.error("<<무인택배함 보관함 열기 실패>>", e);
            return e.getMessage();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String initBox(UhdbLogVo param) {
        try {
            // 1. 아파트아이디와 무인택배함아이디로 무인택배함의 공인아이피 조회
            UhdbVo uv = new UhdbVo();
            uv.setAptId(param.getAptId());
            uv.setAptPosi(param.getAptPosi());
            List<UhdbVo> uhdbList = this.uhdbMapper.selectUhdbList(uv);

            if (uhdbList == null || uhdbList.isEmpty()) {
                // 무인택배함 부재
                return "UHDB NOT FOUND";
            }

            // logger.debug("<<무인택배함 정보>> {}", uhdbList.get(0).toString());
            String gonginIp = uhdbList.get(0).getGonginIp();
            String portFw = StringUtils.defaultIfEmpty(uhdbList.get(0).getPortFw(), "3306");

            if (StringUtils.isEmpty(gonginIp)) {
                return "PUBLIC IP NOT FOUND";
            }

            // 2. 무인택배함 보관함 로그 업데이트
            UhdbLogVo uhdbLogParam = new UhdbLogVo();
            uhdbLogParam.setAptId(param.getAptId());
            uhdbLogParam.setAptPosi(param.getAptPosi());
            uhdbLogParam.setBoxNo(param.getBoxNo());
            uhdbLogParam.setSafeFunc(null);
            uhdbLogParam.setUseYn("N");
            uhdbLogParam.setStDt(null);
            uhdbLogParam.setEnDt(new Date());
            uhdbLogParam.setDong(null);
            uhdbLogParam.setHo(null);
            uhdbLogParam.setAmtGb(null);
            uhdbLogParam.setAmt((double) 0);
            uhdbLogParam.setTaekbaeHandphone(null);
            uhdbLogParam.setTaekbaePswd(null);
            uhdbLogParam.setTaekbae(null);
            uhdbLogParam.setHandphone(null);
            uhdbLogParam.setPswd(null);
            uhdbLogParam.setUserId("SAFEAUTO");

            this.uhdbMapper.updateUhdbLog(uhdbLogParam);

            // 3. 락커 초기화 실행(DB 업데이트)
            int updateCount = 0;

            try {
                JdbcUtils ju = new JdbcUtils();
                updateCount = ju.initBox(gonginIp, portFw, param.getAptId(), param.getAptPosi(), param.getBoxNo());
            } catch (Exception e) {
                logger.error("<<무인택배함 보관함 초기화 실패>>", e);
                return e.getMessage();
            }

            if (updateCount > 0) {
                logger.info("<<{} {} 무인택배함 {} 번 보관함이 초기화되었습니다.>>", param.getAptId(), param.getAptPosi(), param.getBoxNo());
                return "OK";
            } else {
                return "NOK";
            }
        } catch (Exception e) {
            logger.error("<<무인택배함 보관함 초기화 실패>>", e);
            return e.getMessage();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyUhdbGonginIp(UhdbVo param) throws Exception {
        this.uhdbMapper.updateUhdbGonginIp(param);
        logger.info("<<무인택배함 공인아이피 수정>> {}", param.toString());
    }

    @Override
    @Transactional(readOnly = true)
    public String findUhdbUserPassword(UhdbLogVo param) throws Exception {
        return this.uhdbMapper.selectUhdbUserPassword(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyUhdbUserPassword(UhdbLogVo param) throws Exception {
        this.uhdbMapper.updateUhdbUserPassword(param);
        logger.info("<<무인택배함 세대 비밀번호 수정>> {}", param.toString());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regNfc(NfcVo param) throws Exception {
        this.uhdbMapper.insertNfc(param);
        logger.info("<<무인택배함 NFC 등록>> {}", param.toString());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeNfc(NfcVo param) throws Exception {
        this.uhdbMapper.deleteNfc(param);
        logger.info("<<무인택배함 NFC 삭제>> {}", param.toString());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UhdbLogVo> findUhdbLogList4Mobile(UhdbDto param) throws Exception {
        logger.info("<<무인택배함 사용내역 조회>> {}", param.toString());

        // <!-- 20170810, 아파트아이디, 택배함위치, 전화번호 조회로 번경
        /* @formatter:on */
        List<Map<String, Object>> userUhdbList = this.uhdbMapper.selectAptUhdbUserList(param.getUserSeq());

        if (userUhdbList == null || userUhdbList.isEmpty()) {
            return null;
        }

        Map<String, Object> temp = userUhdbList.get(0);
        String aptId = (String) temp.get("aptId");
        String aptPosi = (String) temp.get("aptPosi");
        // String dong = (String) temp.get("dong");
        // String ho = (String) temp.get("ho");

        param.setAptId(aptId);
        param.setAptPosi(aptPosi);
        // param.setDong(dong);
        // param.setHo(ho);
        /* @formatter:off */
        // 20170810, 아파트아이디, 택배함위치, 전화번호 조회로 번경 -->

        UserVo userParam = new UserVo();
        userParam.setSeq(param.getUserSeq());
        UserVo user = this.userMapper.selectUser(userParam);
        param.setHandphone(StringUtils.remove(user.getDecMobile(), "-"));

        if (param.getMobilePagingHelper().getSortList() == null || param.getMobilePagingHelper().getSortList().isEmpty()) {
            Sort sort = new Sort();
            sort.setColumn("upddt");
            sort.setAscOrDesc(CommonConstants.SortType.DESC.getValue());

            List<Sort> sortList = new ArrayList<>();
            sortList.add(sort);

            param.getMobilePagingHelper().setSortList(sortList);
        }

        List<UhdbLogVo> uhdbLogList = null;

        if ("now".equalsIgnoreCase(param.getGubun())) {
            param.getMobilePagingHelper().setTotalCount(this.uhdbMapper.selectUhdbLogListCount4Mobile(param));
            uhdbLogList = this.uhdbMapper.selectUhdbLogList4Mobile(param);
        } else {
            param.getMobilePagingHelper().setTotalCount(this.uhdbMapper.selectPastUhdbLogListCount4Mobile(param));
            uhdbLogList = this.uhdbMapper.selectPastUhdbLogList4Mobile(param);
        }

        int nextPageToken = 0;

        if (uhdbLogList != null && !uhdbLogList.isEmpty()) {
            nextPageToken = uhdbLogList.get(uhdbLogList.size() - 1).getSeq();
        }

        param.getMobilePagingHelper().setNextPageToken(nextPageToken);

        return uhdbLogList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void sendAlarm4LongBox(UhdbDto param) throws Exception {
        /* @formatter:off */
        /**
        1. 장기보관 문자전송 프로세스.
        - 물품보관 된지 한지 3일째 되는 날에 고객에게 PUSH를 한번 더 보낸다.
          ex) 2017.06.05 보관 -> 2017.06.08 12:30 에 일괄로 push 진행.
           -- 택배함에서 체크해서 API를 호출할 예정.

         - api 방식으로 처리.(전강욱 책임 API 개발)
           aptid, aptposi, boxno, 전송구분(push만, push+문자)
          ※ 앱이 설치되지 않는 고객에게 문자로 전송을 하지는 않는다.
        */
        /* @formatter:on */

        // String aptsName = StringUtils.EMPTY;
        String aptPosiName = StringUtils.EMPTY;
        // String nowYmd = StringUtils.EMPTY;
        String userMobile = StringUtils.EMPTY;
        String userPassword = StringUtils.EMPTY;

        String title4Push = "택배 장기보관 알림";
        String body4Push = "%s 보관함 %s번함에 3일 이상 보관되어 있는 물품이 있으니 빨리 찾아가세요. 비밀번호:%s";
        String body4Sms = "%s 보관함 %s번함 3일 이상 보관 빨리 찾아가세요. 비밀번호:%s";

        try {
            UhdbLogVo uhdbLogParam = new UhdbLogVo();
            uhdbLogParam.setAptId(param.getAptId());
            uhdbLogParam.setAptPosi(param.getAptPosi());
            uhdbLogParam.setBoxNo(param.getBoxNo());

            UhdbLogVo uhdbLog = this.uhdbMapper.selectUhdbLog(uhdbLogParam);
            userMobile = uhdbLog.getHandphone();
            userPassword = uhdbLog.getPswd();

            // AptVo aptParam = new AptVo();
            // aptParam.setAptId(param.getAptId());
            // List<AptVo> aptList = this.uhdbMapper.selectAptList(aptParam);
            // aptsName = aptList.get(0).getAptsNm();

            UhdbVo uhdbParam = new UhdbVo();
            uhdbParam.setAptId(param.getAptId());
            uhdbParam.setAptPosi(param.getAptPosi());
            List<UhdbVo> uhdbList = this.uhdbMapper.selectUhdbList(uhdbParam);
            aptPosiName = uhdbList.get(0).getAptPosiNm();

            // nowYmd = sdf.format(new Date());

            body4Push = String.format(body4Push, aptPosiName, param.getBoxNo(), userPassword);
            body4Sms = String.format(body4Sms, aptPosiName, param.getBoxNo(), userPassword);
        } catch (Exception e) {
            logger.error("<<sendAlarm4LongBox 오류>>", e);
            throw e;
        }

        String secretKey = null;

        try {
            // 1. 핸드폰번호로 사용자 찾기
            UserUhdbVo userUhdb = new UserUhdbVo();

            String mobileNumber = userMobile.substring(0, 3) + "-" + userMobile.substring(3, 7) + "-" + userMobile.substring(7, 11);
            secretKey = BlockCipherUtils.generateSecretKey(CommonConstants.DEFAULT_AUTH_KEY);
            String encMobileNumber = BlockCipherUtils.encrypt(secretKey, mobileNumber);

            userUhdb.setMobile(encMobileNumber);
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

            List<Integer> userSeqList = new ArrayList<>();

            for (UserUhdbVo uu : userUhdbList) {
                userSeqList.add(uu.getUserSeq());
            }

            UserDto userDto = new UserDto();
            userDto.setUserSeqList(userSeqList);

            List<UserVo> userList = this.userMapper.selectUserList(userDto);

            if (userList == null || userList.isEmpty()) {
                // 정말 이상한 경우이다. 나올 수 없는 경우이다.
                return;
            }

            // 4. FCM 발송
            for (UserVo user : userList) {
                UserFcmVo userFcm = new UserFcmVo();
                userFcm.setUserSeq(user.getSeq());

                UserFcmVo fcm = this.userMapper.selectUserFcm(userFcm);
                if (fcm == null) {
                    continue;
                }

                Message message = new Message(title4Push, body4Push, PUSH_AD_URL);
                FcmReqForm fcmReqForm = new FcmReqForm(new Data(message), fcm.getFcmToken());

                CompletableFuture<String> result = this.fcmUtils.send(fcmReqForm);

                // 발송 로그 저장
                SendVo send = new SendVo();
                send.setGubun(CommonConstants.SendType.PUSH_UHDB.getValue());
                send.setMobile(BlockCipherUtils.decrypt(secretKey, user.getMobile()));
                send.setFcmToken(fcm.getFcmToken());
                send.setSendMessage(this.mapper.writeValueAsString(fcmReqForm));
                send.setResultMessage(result.get());
                this.commonService.regSendLog(send);

                logger.info("<<무인택배함 장기보관 알림 API, 사용자 PUSH 발송>> {}", send.toString());
            }
        } catch (Exception e) {
            logger.error("<<무인택배함 장기보관 알림 API, FCM 발송 중 오류>>", e);
            throw e;
        }

        if ("pushAndSms".equalsIgnoreCase(param.getGubun())) {
            // 문자 발송
            // 테스트 기간 중 SMS 발송 중지
            // 3. 핸드폰번호로도 아파트아이디, 택배함 위치, 동, 호로도 사용자를 찾을 수 없다면 SMS 발송
            CompletableFuture<String> result = this.smsUtils.send(userMobile, body4Sms);

            // 발송 로그 저장
            SendVo send = new SendVo();
            send.setGubun(CommonConstants.SendType.SMS_UHDB.getValue());
            send.setMobile(userMobile);
            send.setSendMessage(body4Sms);
            send.setResultMessage(result.get());
            this.commonService.regSendLog(send);

            logger.info("<<무인택배함 장기보관 알림 API, 사용자 SMS 발송>> {}", send.toString());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void restoreBox(UhdbLogVo param, String updateDatetime) throws Exception {
        UhdbLogVo uhdbLogParam = new UhdbLogVo();
        uhdbLogParam.setAptId(param.getAptId());
        uhdbLogParam.setAptPosi(param.getAptPosi());
        uhdbLogParam.setBoxNo(param.getBoxNo());
        if (StringUtils.isEmpty(updateDatetime)) {
            uhdbLogParam.setUpdDt(new Date());
        } else {
            try {
                uhdbLogParam.setUpdDt(CommonConstants.DEFAULT_SDF.parse(updateDatetime));
            } catch (Exception e) {
                uhdbLogParam.setUpdDt(new Date());
            }
        }
        uhdbLogParam.setUserId(param.getUserId());

        UhdbLogVo uhdbLog = this.uhdbMapper.selectUhdbLogHistory(param);

        uhdbLogParam.setSafeFunc(uhdbLog.getSafeFunc());
        uhdbLogParam.setUseYn(uhdbLog.getUseYn());
        uhdbLogParam.setStDt(uhdbLog.getStDt());
        uhdbLogParam.setEnDt(uhdbLog.getEnDt());
        uhdbLogParam.setDong(uhdbLog.getDong());
        uhdbLogParam.setHo(uhdbLog.getHo());
        uhdbLogParam.setAmtGb(uhdbLog.getAmtGb());
        uhdbLogParam.setAmt(uhdbLog.getAmt());
        uhdbLogParam.setTaekbaeHandphone(uhdbLog.getTaekbaeHandphone());
        uhdbLogParam.setTaekbaePswd(uhdbLog.getTaekbaePswd());
        uhdbLogParam.setTaekbae(uhdbLog.getTaekbae());
        uhdbLogParam.setHandphone(uhdbLog.getHandphone());
        uhdbLogParam.setPswd(uhdbLog.getPswd());

        this.uhdbMapper.updateUhdbLog(uhdbLogParam);
    }

    @Override
    public void modifyAuthcode(UhdbLogVo param) {
        this.uhdbMapper.updateAuthcode(param);
    }

    @Override
    public UserUhdbVo findMemberYn(UhdbLogVo param) throws Exception {
        // 아파트아이디와 핸드폰번호로 사용자 찾기

        String userMobile = param.getHandphone();

        UserUhdbVo userUhdbParam = new UserUhdbVo();

        String mobileNumber = userMobile.substring(0, 3) + "-" + userMobile.substring(3, 7) + "-" + userMobile.substring(7, 11);
        String secretKey = BlockCipherUtils.generateSecretKey(CommonConstants.DEFAULT_AUTH_KEY);
        String encMobileNumber = BlockCipherUtils.encrypt(secretKey, mobileNumber);

        userUhdbParam.setAptId(param.getAptId());
        userUhdbParam.setMobile(encMobileNumber);

        return this.userMapper.selectMemberYn(userUhdbParam);
    }

}
