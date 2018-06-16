/**
 *
 */
package com.realsnake.sample.mapper.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.realsnake.sample.model.board.BoardDto;
import com.realsnake.sample.model.board.NoticeVo;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Mapper class mapped db-table called notice
 */
@Mapper
public interface NoticeMapper {

    int insertNotice(NoticeVo param);

    int updateNotice(NoticeVo param);

    int deleteNotice(NoticeVo param);

    NoticeVo selectNotice(Integer param);

    int selectNoticeListCount(BoardDto param);

    List<NoticeVo> selectNoticeList(BoardDto param);

    int selectNoticeListCount4Mobile(BoardDto param);

    List<NoticeVo> selectNoticeList4Mobile(BoardDto param);

}
