/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 1.
 */
package com.realsnake.sample.service.uhdb;

import java.util.List;
import java.util.Map;

import com.realsnake.sample.model.uhdb.AptVo;
import com.realsnake.sample.model.uhdb.NfcVo;
import com.realsnake.sample.model.uhdb.UhdbDto;
import com.realsnake.sample.model.uhdb.UhdbLogVo;
import com.realsnake.sample.model.uhdb.UhdbVo;

/**
 * <pre>
 * Class Name : UhdbService.java
 * Description : Description
 * </pre>
 *
 * @author 전강욱(realsnake1975@gmail.com)
 * @since 2017. 5. 1.
 * @version 1.0
 */
public interface UhdbService {

    List<AptVo> findAptList(AptVo param) throws Exception;

    List<UhdbVo> findUhdbList(UhdbVo param) throws Exception;

    void modifyUhdbLog(UhdbLogVo param) throws Exception;

    List<UhdbLogVo> findUhdbLogList(UhdbDto param) throws Exception;

    List<Map<String, Object>> findAptUhdbUserList(Integer param) throws Exception;

    String openBox(UhdbLogVo param);

    String initBox(UhdbLogVo param);

    void modifyUhdbGonginIp(UhdbVo param) throws Exception;

    String findUhdbUserPassword(UhdbLogVo param) throws Exception;

    void modifyUhdbUserPassword(UhdbLogVo param) throws Exception;

    void regNfc(NfcVo param) throws Exception;

    void removeNfc(NfcVo param) throws Exception;

    List<UhdbLogVo> findUhdbLogList4Mobile(UhdbDto param) throws Exception;

    void sendAlarm4LongBox(UhdbDto param) throws Exception;

}
