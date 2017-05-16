/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 1.
 */
package com.realsnake.sample.service.board;

import java.util.List;

import com.realsnake.sample.model.board.BoardDto;
import com.realsnake.sample.model.board.NoticeVo;

/**
 * <pre>
 * Class Name : NoticeService.java
 * Description : Description
 * </pre>
 *
 * @author 전강욱(realsnake1975@gmail.com)
 * @since 2017. 5. 1.
 * @version 1.0
 */
public interface NoticeService {

    void regNotice(BoardDto param, NoticeVo notice) throws Exception;

    void modifyNotice(BoardDto param, NoticeVo notice) throws Exception;

    NoticeVo findNotice(Integer param) throws Exception;

    List<NoticeVo> findNoticeList(BoardDto param) throws Exception;

}
