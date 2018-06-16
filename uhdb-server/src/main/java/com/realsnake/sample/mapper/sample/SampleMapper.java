/**
 *
 */
package com.realsnake.sample.mapper.sample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.realsnake.sample.model.sample.SampleDto;
import com.realsnake.sample.model.sample.SampleVo;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Mapper class mapped db-table called sample
 */
@Mapper
public interface SampleMapper {

    /**
     * sample에 데이타를 입력한다.
     */
    public void insertSample(SampleVo param);

    /**
     * sample의 목록을 검색조건에 맞게 조회한다.
     */
    public List<SampleVo> selectSampleList(SampleDto param);

}
