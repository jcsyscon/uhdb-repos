/**
 *
 */
package com.realsnake.sample.model.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.util.crypto.BlockCipherUtils;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Domain class mapped db-table called user
 */
@Alias(value = "UserVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = 1811096374737521107L;

    /** 일련번호 */
    private Integer seq;
    /** 아이디 */
    private String username;
    /** 비밀번호 */
    @JsonIgnore
    private String password;
    /** 이름 */
    @JsonIgnore
    private String name;
    /** 핸드폰번호 */
    @JsonIgnore
    private String mobile;
    /** 이메일 */
    @JsonIgnore
    private String email;
    /** 생년월일 */
    private String birthDate;
    /** 성별 */
    private String sex;
    /** 알람수신여부 */
    private String alarmRecYn = "Y";
    /** 등록일시 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDate;
    /** 탈퇴여부 */
    private String secedeYn = "N";
    /** 탈퇴일시 */
    private Date secedeDate;
    /** 사용자권한 */
    private Collection<? extends GrantedAuthority> authorities;

    /** 관리에 의한 탈퇴여부 */
    private String secedeYnByAdmin = "N";
    
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getSex() {
        return sex;
    }

    public String getAlarmRecYn() {
        return alarmRecYn;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAlarmRecYn(String alarmRecYn) {
        this.alarmRecYn = alarmRecYn;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getSecedeYn() {
        return secedeYn;
    }

    public void setSecedeYn(String secedeYn) {
        this.secedeYn = secedeYn;
    }

    public Date getSecedeDate() {
        return secedeDate;
    }

    public void setSecedeDate(Date secedeDate) {
        this.secedeDate = secedeDate;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getSecedeYnByAdmin() {
		return secedeYnByAdmin;
	}

	public void setSecedeYnByAdmin(String secedeYnByAdmin) {
		this.secedeYnByAdmin = secedeYnByAdmin;
	}

	/**
     * 복호화한 사용자 이름
     *
     * @return
     */
    public String getDecName() {
        try {
            // String secretKey = BlockCipherUtils.generateSecretKey(password);
            String secretKey = BlockCipherUtils.generateSecretKey(CommonConstants.DEFAULT_AUTH_KEY);
            return BlockCipherUtils.decrypt(secretKey, name);
        } catch (Exception e) {
            // System.out.printf("<<사용자 이름 복호화 중 오류>> %s", e.getMessage());
            return null;
        }
    }

    /**
     * 복호화한 사용자 핸드폰번호
     *
     * @return
     */
    public String getDecMobile() {
        try {
            // String secretKey = BlockCipherUtils.generateSecretKey(password);
            String secretKey = BlockCipherUtils.generateSecretKey(CommonConstants.DEFAULT_AUTH_KEY);
            return BlockCipherUtils.decrypt(secretKey, mobile);
        } catch (Exception e) {
            // System.out.printf("<<사용자 이메일 복호화 중 오류>> %s", e.getMessage());
            return null;
        }
    }

    /**
     * 복호화한 사용자 이메일
     *
     * @return
     */
    public String getDecEmail() {
        try {
            String secretKey = BlockCipherUtils.generateSecretKey(password);
            return BlockCipherUtils.decrypt(secretKey, email);
        } catch (Exception e) {
            // System.out.printf("<<사용자 이메일 복호화 중 오류>> %s", e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}
