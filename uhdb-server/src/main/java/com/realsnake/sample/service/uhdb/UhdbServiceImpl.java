/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 1.
 */
package com.realsnake.sample.service.uhdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.mapper.uhdb.UhdbMapper;
import com.realsnake.sample.model.common.Sort;
import com.realsnake.sample.model.uhdb.AptVo;
import com.realsnake.sample.model.uhdb.UhdbDto;
import com.realsnake.sample.model.uhdb.UhdbLogVo;
import com.realsnake.sample.model.uhdb.UhdbVo;

/**
 * <pre>
 * Class Name : UhdbServiceImpl.java
 * Description : Description
 * </pre>
 *
 * @author 전강욱(realsnake1975@gmail.com)
 * @since 2017. 5. 1.
 * @version 1.0
 */
@Service
public class UhdbServiceImpl implements UhdbService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UhdbMapper uhdbMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AptVo> findAptList(AptVo param) throws Exception {
        return this.uhdbMapper.selectAptList(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UhdbVo> findUhdbList(UhdbVo param) throws Exception {
        return this.uhdbMapper.selectUhdbList(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyUhdbLog(UhdbLogVo param) throws Exception {
        if (StringUtils.isEmpty(param.getAptId())) {
            throw new Exception("아파트 아이디는 필수입니다!");
        }
        if (StringUtils.isEmpty(param.getAptPosi())) {
            throw new Exception("택배함 아이디는 필수입니다!");
        }
        if (StringUtils.isEmpty(param.getBoxNo())) {
            throw new Exception("락커 번호는 필수입니다!");
        }

        if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_10.getCode())) {
            param.setUseYn("Y");
            param.setStDt(new Date());
            param.setEnDt(null);
            param.setAmt((double) 0);
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_20.getCode())) {
            param.setUseYn("N");
            param.setStDt(null);
            param.setEnDt(new Date());
            param.setDong(null);
            param.setHo(null);
            param.setAmtGb(null);
            param.setAmt((double) 0);
            param.setTaekbaeHandphone(null);
            param.setTaekbaePswd(null);
            param.setTaekbae(null);
            param.setHandphone(null);
            param.setPswd(null);
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_30.getCode())) {
            param.setUseYn("Y");
            param.setStDt(new Date());
            param.setEnDt(null);
            param.setAmt((double) 0);
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_40.getCode())) {
            param.setUseYn("Y");
            param.setStDt(new Date());
            param.setEnDt(null);
            param.setAmt((double) 0);
        } else if (param.getSafeFunc().equals(CommonConstants.SafeFuncType.SAFE_FUNC_50.getCode())) {
            param.setUseYn("N");
            param.setStDt(null);
            param.setEnDt(new Date());
            param.setDong(null);
            param.setHo(null);
            param.setAmtGb(null);
            param.setAmt((double) 0);
            param.setTaekbaeHandphone(null);
            param.setTaekbaePswd(null);
            param.setTaekbae(null);
            param.setHandphone(null);
            param.setPswd(null);
        }

        this.uhdbMapper.updateUhdbLog(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UhdbLogVo> findUhdbLogList(UhdbDto param) throws Exception {
        if (param.getPagingHelper().getSortList() == null || param.getPagingHelper().getSortList().isEmpty()) {
            Sort sort = new Sort();
            sort.setColumn("seq");
            sort.setAscOrDesc(CommonConstants.SortType.DESC.getValue());

            List<Sort> sortList = new ArrayList<>();
            sortList.add(sort);

            param.getPagingHelper().setSortList(sortList);
        }

        param.getPagingHelper().setTotalCount(this.uhdbMapper.selectUhdbLogListCount(param));

        return this.uhdbMapper.selectUhdbLogList(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> findAptUhdbUserList(Integer param) throws Exception {
        return this.uhdbMapper.selectAptUhdbUserList(param);
    }

}
