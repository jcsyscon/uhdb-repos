/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 1. 23.
 */
package com.realsnake.sample.service.sample;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.realsnake.sample.mapper.sample.SampleMapper;
import com.realsnake.sample.model.sample.SampleDto;
import com.realsnake.sample.model.sample.SampleVo;

/**
 * <pre>
 * Class Name : SampleServiceImpl.java
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
@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    private SampleMapper sampleMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int regSample(SampleVo param) throws Exception {
        this.sampleMapper.insertSample(param);
        return param.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SampleVo> findSampleList(SampleDto param) throws Exception {
        return this.sampleMapper.selectSampleList(param);
    }

}
