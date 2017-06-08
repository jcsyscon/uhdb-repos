/**
 *
 */
package com.realsnake.sample.mapper.common;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.realsnake.sample.model.common.AddressVo;
import com.realsnake.sample.model.common.AttachFileVo;
import com.realsnake.sample.model.common.BuildingInfoVo;
import com.realsnake.sample.model.common.CommonCodeVo;
import com.realsnake.sample.model.common.CommonDto;
import com.realsnake.sample.model.common.JibunVo;
import com.realsnake.sample.model.common.RoadNameCodeVo;
import com.realsnake.sample.model.common.SendVo;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Mapper class mapped db-table called attach_file, common_code etc...
 */
@Mapper
public interface CommonMapper {

    int insertAttachFile(AttachFileVo param);

    int deleteAttachFile(AttachFileVo param);

    List<AttachFileVo> selectAttachFileList(AttachFileVo param);

    AttachFileVo selectAttachFile(Integer attachFileSeq);

    int insertAddress(AddressVo param);

    int deleteAddress(AddressVo param);

    AddressVo selectAddress(Integer addressSeq);

    List<AddressVo> selectAddressList(AddressVo param);

    int insertCommonCode(CommonCodeVo param);

    int updateCommonCode(CommonCodeVo param);

    int deleteCommonCode(CommonCodeVo param);

    CommonCodeVo selectCommonCode(Integer param);

    int selectCommonCodeListCount(CommonDto param);

    List<CommonCodeVo> selectCommonCodeList(CommonDto param);

    int insertBuildingInfo(BuildingInfoVo param);

    int insertJibun(JibunVo param);

    int insertRoadNameCode(RoadNameCodeVo param);

    int insertSendLog(SendVo param);

    SendVo selectSendLog(String param);

    List<String> selectSidoList(String param);

    List<String> selectSiguList(String param);

}
