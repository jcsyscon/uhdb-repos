/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 1. 23.
 */
package com.realsnake.sample.service.sample;

import java.util.List;

import com.realsnake.sample.model.sample.SampleDto;
import com.realsnake.sample.model.sample.SampleVo;

/**
 * <pre>
 * Class Name : SampleService.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 1. 23.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 1. 23.
 * @version 1.0
 */
public interface SampleService {

    int regSample(SampleVo param) throws Exception;

    List<SampleVo> findSampleList(SampleDto param) throws Exception;

}
