/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 1. 23.
 */
package com.realsnake.sample.service.common;

import java.util.List;

import com.realsnake.sample.model.common.AddressVo;
import com.realsnake.sample.model.common.AttachFileVo;
import com.realsnake.sample.model.common.BuildingInfoVo;
import com.realsnake.sample.model.common.CommonCodeVo;
import com.realsnake.sample.model.common.CommonDto;
import com.realsnake.sample.model.common.JibunVo;
import com.realsnake.sample.model.common.RoadNameCodeVo;
import com.realsnake.sample.model.common.SendVo;

/**
 * <pre>
 * Class Name : CommonService.java
 * Description : Description
 * </pre>
 *
 * @author 전강욱(realsnake1975@gmail.com)
 * @since 2017. 1. 23.
 * @version 1.0
 */
public interface CommonService {

    // void saveAttachFile(AttachFileVo param) throws Exception;

    void removeAttachFile(Integer attachFileSeq, CommonDto param) throws Exception;

    List<AttachFileVo> findAttachFileList(AttachFileVo param) throws Exception;

    AttachFileVo findAttachFile(Integer attachFileSeq) throws Exception;

    List<AttachFileVo> moveUploadedFile(String[] uploadedFiles, Integer groupSeq, String gubun) throws Exception;

    void regAddress(AddressVo param) throws Exception;

    void modifyAddress(AddressVo newAddress) throws Exception;

    void regCommonCode(CommonCodeVo param) throws Exception;

    void modifyCommonCode(CommonCodeVo param) throws Exception;

    void removeCommonCode(CommonCodeVo param) throws Exception;

    CommonCodeVo findCommonCode(String param) throws Exception;

    int findCommonCodeListCount(CommonDto param) throws Exception;

    List<CommonCodeVo> findCommonCodeList(CommonDto param) throws Exception;

    // <!-- www.juso.go.kr의 주소 데이터 처리
    void regBuildingInfo(BuildingInfoVo param) throws Exception;

    void regJibun(JibunVo param) throws Exception;

    void regRoadNameCode(RoadNameCodeVo param) throws Exception;

    void regJuso(String filterParam) throws Exception;
    // www.juso.go.kr의 주소 데이터 처리 -->

    void regSendLog(SendVo param);

    Boolean compareAuthMobileCode(String code, String key);

}
