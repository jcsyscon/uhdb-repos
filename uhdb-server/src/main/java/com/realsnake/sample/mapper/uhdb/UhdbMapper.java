/**
 *
 */
package com.realsnake.sample.mapper.uhdb;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.realsnake.sample.model.uhdb.AptVo;
import com.realsnake.sample.model.uhdb.UhdbDto;
import com.realsnake.sample.model.uhdb.UhdbLogVo;
import com.realsnake.sample.model.uhdb.UhdbVo;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Mapper class mapped db-table called tb_ap0101, tb_ap0102, tb_bx0201, tb_bx0201h
 */
@Mapper
public interface UhdbMapper {

    List<AptVo> selectAptList(AptVo param);

    List<UhdbVo> selectUhdbList(UhdbVo param);

    int updateUhdbGonginIp(UhdbVo param);

    int insertUhdbLog(UhdbLogVo param);

    int updateUhdbLog(UhdbLogVo param);

    int selectUhdbLogListCount(UhdbDto param);

    List<UhdbLogVo> selectUhdbLogList(UhdbDto param);

    List<Map<String, Object>> selectAptUhdbUserList(Integer param);

    UhdbLogVo selectUhdbLog(UhdbLogVo param);

    String selectUhdbUserPassword(UhdbLogVo param);

    int updateUhdbUserPassword(UhdbLogVo param);

}
