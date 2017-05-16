/**
 * Copyright (c) 2016 realsnake1975@gmail.com
 *
 * 2016. 10. 13.
 */
package com.realsnake.sample.model.common;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.model.user.LoginUser;
import com.realsnake.sample.util.PagingHelper;

/**
 * <pre>
 * Class Name : CommonDto.java
 * Description : 공통 Dto
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2016. 10. 13.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2016. 10. 13.
 * @version 1.0
 */
@Alias(value = "CommonDto")
public class CommonDto implements Serializable {

    /** SID */
    private static final long serialVersionUID = 4026539935212986947L;

    /** 페이징 */
    private Paging pagingHelper;
    /** 로그인 사용자 */
    private LoginUser loginUser;
    /** 파일업로드(풀파일경로). 모든 파일업로드에서 사용하며 이름까지 동일하다. */
    private String[] uploadedFiles;

    private List<AddressVo> addressList;
    private List<AttachFileVo> attachFileList;

    public Paging getPagingHelper() {
        if (pagingHelper == null) {
            pagingHelper = new PagingHelper();
            pagingHelper.setStartNum(0);
            pagingHelper.setEndNum(CommonConstants.DEFAULT_PAGE_SIZE);
        }

        return pagingHelper;
    }

    public void setPagingHelper(Paging pagingHelper) {
        this.pagingHelper = pagingHelper;
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    public String[] getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(String[] uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public List<AddressVo> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressVo> addressList) {
        this.addressList = addressList;
    }

    public List<AttachFileVo> getAttachFileList() {
        return attachFileList;
    }

    public void setAttachFileList(List<AttachFileVo> attachFileList) {
        this.attachFileList = attachFileList;
    }

}
