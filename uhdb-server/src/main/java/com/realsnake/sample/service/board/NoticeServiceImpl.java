/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 1.
 */
package com.realsnake.sample.service.board;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.mapper.board.NoticeMapper;
import com.realsnake.sample.model.board.BoardDto;
import com.realsnake.sample.model.board.NoticeVo;
import com.realsnake.sample.model.common.Sort;
import com.realsnake.sample.service.common.CommonService;

/**
 * <pre>
 * Class Name : NoticeServiceImpl.java
 * Description : Description
 * </pre>
 *
 * @author 전강욱(realsnake1975@gmail.com)
 * @since 2017. 5. 1.
 * @version 1.0
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private CommonService commonService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regNotice(BoardDto param, NoticeVo notice) throws Exception {
        notice.setRegUserSeq(param.getLoginUser().getSeq());
        this.noticeMapper.insertNotice(notice);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyNotice(BoardDto param, NoticeVo notice) throws Exception {
        if ("Y".equalsIgnoreCase(notice.getDelYn())) {
            notice.setDelUserSeq(param.getLoginUser().getSeq());
            this.noticeMapper.deleteNotice(notice);
        } else {
            notice.setModUserSeq(param.getLoginUser().getSeq());
            this.noticeMapper.updateNotice(notice);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeVo findNotice(Integer param) throws Exception {
        return this.noticeMapper.selectNotice(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoticeVo> findNoticeList(BoardDto param) throws Exception {
        // TODO: 페이징이 필요할 시 페이징 처리
        param.getPagingHelper().setEndNum(Integer.MAX_VALUE);
        Sort sort = new Sort();
        sort.setColumn("seq");
        sort.setAscOrDesc(CommonConstants.SortType.DESC.getValue());
        List<Sort> sortList = new ArrayList<Sort>();
        sortList.add(sort);
        param.getPagingHelper().setSortList(sortList);
        // TODO: 페이징이 필요할 시 페이징 처리

        param.getPagingHelper().setTotalCount(this.noticeMapper.selectNoticeListCount(param));
        return this.noticeMapper.selectNoticeList(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoticeVo> findNoticeList4Mobile(BoardDto param) throws Exception {
        if (param.getMobilePagingHelper().getSortList() == null || param.getMobilePagingHelper().getSortList().isEmpty()) {
            Sort sort = new Sort();
            sort.setColumn("seq");
            sort.setAscOrDesc(CommonConstants.SortType.DESC.getValue());

            List<Sort> sortList = new ArrayList<>();
            sortList.add(sort);

            param.getMobilePagingHelper().setSortList(sortList);
        }

        param.getMobilePagingHelper().setTotalCount(this.noticeMapper.selectNoticeListCount4Mobile(param));
        List<NoticeVo> noticeList = this.noticeMapper.selectNoticeList4Mobile(param);

        int nextPageToken = 0;

        if (noticeList != null && !noticeList.isEmpty()) {
            nextPageToken = noticeList.get(noticeList.size() - 1).getSeq();
        }

        param.getMobilePagingHelper().setNextPageToken(nextPageToken);

        return noticeList;
    }

}
