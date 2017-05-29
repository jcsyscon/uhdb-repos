/**
 *
 */
package com.realsnake.sample.model.uhdb;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Domain class mapped db-table called tb_bx0201
 */
@Alias(value = "UhdbLogVo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UhdbLogVo implements Serializable {

    /** SID */
    private static final long serialVersionUID = 4998511915929320817L;

    private Date updDt;
    private String aptId;
    private String aptPosi;
    /** 락커번호 */
    private String boxNo;
    /** 10: 택배보관(택배기사), 20: 택배수령(고객), 30: 택배발송요청(고객), 40: 택배수령(택배기사), 50: 택배반품요청(고객) */
    private String safeFunc;
    /** 보관여부 */
    private String useYn;
    private Date stDt;
    private Date enDt;
    private String dong;
    private String ho;
    /** 선후불 구분 */
    private String amtGb;
    private Double amt;
    /** 택배회사 */
    private String taekbae;
    /** 택배기사 전화번호 */
    private String taekbaeHandphone;
    /** 택배기사 비밀번호 */
    private String taekbaePswd;
    /** 고객비밀번호 */
    private String handphone;
    /** 고객비밀번호 */
    private String pswd;
    // private String plcAddr;
    // private String sendStatus;

    public Date getUpdDt() {
        return updDt;
    }

    public String getAptId() {
        return aptId;
    }

    public String getAptPosi() {
        return aptPosi;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public String getSafeFunc() {
        return safeFunc;
    }

    public String getUseYn() {
        return useYn;
    }

    public Date getStDt() {
        return stDt;
    }

    public Date getEnDt() {
        return enDt;
    }

    public String getDong() {
        return dong;
    }

    public String getHo() {
        return ho;
    }

    public String getAmtGb() {
        return amtGb;
    }

    public Double getAmt() {
        return amt;
    }

    public String getTaekbae() {
        return taekbae;
    }

    public String getTaekbaeHandphone() {
        return taekbaeHandphone;
    }

    public String getTaekbaePswd() {
        return taekbaePswd;
    }

    public String getHandphone() {
        return handphone;
    }

    public String getPswd() {
        return pswd;
    }

    public void setUpdDt(Date updDt) {
        this.updDt = updDt;
    }

    public void setAptId(String aptId) {
        this.aptId = aptId;
    }

    public void setAptPosi(String aptPosi) {
        this.aptPosi = aptPosi;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public void setSafeFunc(String safeFunc) {
        this.safeFunc = safeFunc;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public void setStDt(Date stDt) {
        this.stDt = stDt;
    }

    public void setEnDt(Date enDt) {
        this.enDt = enDt;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public void setAmtGb(String amtGb) {
        this.amtGb = amtGb;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public void setTaekbae(String taekbae) {
        this.taekbae = taekbae;
    }

    public void setTaekbaeHandphone(String taekbaeHandphone) {
        this.taekbaeHandphone = taekbaeHandphone;
    }

    public void setTaekbaePswd(String taekbaePswd) {
        this.taekbaePswd = taekbaePswd;
    }

    public void setHandphone(String handphone) {
        this.handphone = handphone;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
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
