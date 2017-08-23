/**
 * Copyright (c) 2016 realsnake1975@gmail.com
 *
 * 2016. 10. 21.
 */
package com.realsnake.sample.constants;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * <pre>
 * Class Name : CommonConstants.java
 * Description : 공통 상수
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2016. 10. 21.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2016. 10. 21.
 * @version 1.0
 */
public interface CommonConstants {

    /** 기본 페이지 사이즈 */
    static final int DEFAULT_PAGE_SIZE = 20;
    /** 기본 페이지 블록 사이즈 */
    static final int DEFAULT_PAGE_BLOCK_SIZE = 10;
    /** 기본 문자 인코딩: UTF-8 */
    static final String DEFAULT_ENCODING = "UTF-8";
    /** 기본 날짜형식: yyyyMMddHHmmss */
    static final String DEFAULT_DATE_FORMAT = "yyyyMMddHHmmss";
    /** 짧은 날짜형식: yyyyMMdd */
    static final String SHORT_DATE_FORMAT = "yyyyMMdd";
    /** 연월 날짜형식: yyyyMM */
    static final String YYYYMM_FORMAT = "yyyyMM";
    /** 기본 버퍼 사이즈: 4096 */
    static final int DEFAULT_BUFFER_SIZE = 4096;

    /** 기본 SimpleDateFormat */
    static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    /** 짧은 SimpleDateFormat */
    static final SimpleDateFormat SHORT_DATE_SDF = new SimpleDateFormat(SHORT_DATE_FORMAT, Locale.KOREA);
    /** 연월 SimpleDateFormat */
    static final SimpleDateFormat YYYYMM_SDF = new SimpleDateFormat(YYYYMM_FORMAT, Locale.KOREA);

    /** 블록암호화에 사용할 비밀키를 만들기 위한 기본 인증키 */
    static final String DEFAULT_AUTH_KEY = "!@#$jcsyscon1234";

    /** 정렬 유형: ASC / DESC */
    enum SortType {
        ASC("ASC"), DESC("DESC");

        private String value;

        SortType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** 성별 */
    enum Gender {
        MALE("male"), FEMALE("female"), MALE_ABBR("M"), FEMALE_ABBR("F");

        private String value;

        Gender(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** 사용자 폰 유형 */
    enum UserDeviceType {
        ANDROID("android"), IOS("ios"), NONE("none");

        private String value;

        UserDeviceType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** 권한 유형 */
    enum RollType {
        ROLL_ANONYMOUS("ROLL_ANONYMOUS"), ROLL_USER("ROLL_USER"), ROLL_ADMIN("ROLL_ADMIN"), ROLL_SUPERADMIN("ROLL_SUPERADMIN");

        private String value;

        RollType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** 인증 유형 */
    enum SocialType {
        FACEBOOK("facebook"), GOOGLEPLUS("googleplus"), NAVER("naver"), KAKAO("kakao");

        private String value;

        SocialType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** 첨부파일 저장 유형 */
    enum AttachFileFolderType {
        NOTICE("notice"), AD("ad"), SHOP("shop"), SPONSOR("sponsor");

        private String value;

        AttachFileFolderType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** 광고 이미지 유형 - 시작(start)/종료(end)/배너(banner)/팝업(popup)/푸시(push) */
    enum AdImageType {
        START("start"), END("end"), BANNER("banner"), POPUP("popup"), PUSH("push");

        private String value;

        AdImageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** 주소 유형 */
    enum AddressType {
        USER("user"), SHOP("shop"), SPONSOR("sponsor");

        private String value;

        AddressType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** 푸시/SMS 발송 유형 - 인증번호발송: sms-auth-number, 무인택배함정보: sms-uhdb, 가입권유: sms-apply, 무인택배함정보: push-uhdb, 푸시광고: push-ad */
    enum SendType {
        SMS_AUTH_NUMBER("sms-auth-number"), SMS_UHDB("sms-uhdb"), SMS_APPLY("sms-apply"), SMS_REQUEST("sms-request"), PUSH_UHDB("push-uhdb"), PUSH_AD("push-ad");

        private String value;

        SendType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    enum SafeFuncType {
        /** 10: 택배보관(택배기사), 20: 택배수령(고객), 30: 택배발송요청(고객), 40: 택배수령(택배기사), 50: 택배반품요청(고객) */

        /* @formatter:off */
        // SAFE_FUNC_10("10", "택배보관(택배기사)", "택배보관", "%s택배함 %s번 비밀번호:%s %s동 %s호 %s")
        // 아파트명 동 호 번함 택배도착! 으로 변경
        SAFE_FUNC_10("10", "택배보관(택배기사)", "택배보관", "%s %s동 %s호 %s번함 택배도착!")
        , SAFE_FUNC_20("20", "택배수령(고객)", "", "")
        , SAFE_FUNC_30("30", "택배발송요청(고객)", "", "")
        , SAFE_FUNC_40("40", "택배수령(택배기사)", "택배발송", "%s %s번에 보관한 택배가 발송되었습니다.")
        , SAFE_FUNC_50("50", "택배반품요청(고객)", "", "")
        ;
        /* @formatter:on */

        SafeFuncType(String code, String value, String title, String body) {
            this.code = code;
            this.value = value;
            this.title = title;
            this.body = body;
        }

        private String code;

        private String value;

        private String title;

        private String body;

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }

        // @Override
        // public String toString() {
        // return String.format("Code:%s, Value:%s", getCode(), getValue());
        // }
    }

}
