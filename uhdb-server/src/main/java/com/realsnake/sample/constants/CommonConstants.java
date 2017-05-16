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

}
