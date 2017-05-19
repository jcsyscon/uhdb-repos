/**
 *
 */
package com.realsnake.sample.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.GrantedAuthority;

import com.realsnake.sample.model.user.UserDto;
import com.realsnake.sample.model.user.UserVo;

/**
 * @author 전강욱(realsnake1975@gmail.com) <br />
 *         This Mapper class mapped db-table called user, user_authority
 */
@Mapper
public interface UserMapper {

    /**
     * user에 데이타를 입력한다.
     *
     * @param param
     */
    void insertUser(UserVo param);

    /**
     * user를 조회한다.
     *
     * @param param
     * @return
     */
    UserVo selectUser(UserVo param);

    /**
     * user 레코드 수를 검색조건에 맞게 조회한다.
     *
     * @param param
     * @return
     */
    int selectUserListCount(UserDto param);

    /**
     * user 목록을 검색조건에 맞게 조회한다.
     *
     * @param param
     * @return
     */
    List<UserVo> selectUserList(UserDto param);

    /**
     * user_authority에 데이타를 입력한다.
     *
     * @param param
     */
    void insertUserAuthority(UserVo param);

    /**
     * user 권한을 조회한다.
     *
     * @param param
     * @return
     */
    List<GrantedAuthority> selectUserAuthNameList(int seq);

}