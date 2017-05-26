/**
 *
 */
package com.realsnake.sample.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.GrantedAuthority;

import com.realsnake.sample.model.user.UserDto;
import com.realsnake.sample.model.user.UserFcmVo;
import com.realsnake.sample.model.user.UserUhdbVo;
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

    /**
     * user 데이타를 수정한다.
     *
     * @param param
     */
    void updateUser(UserVo param);

    /**
     * user 데이타를 수정한다. - 비밀번호 수정
     *
     * @param param
     */
    void updateUserPassword(UserVo param);

    /**
     * user 데이타를 수정한다. - 탈퇴
     *
     * @param param
     */
    void secedeUser(UserVo param);

    /**
     * user_uhdb에 데이타를 입력한다.
     *
     * @param param
     */
    void insertUserUhdb(UserUhdbVo param);

    /**
     * user_uhdb 데이타를 삭제한다(del_yn=Y).
     *
     * @param param
     */
    void deleteUserUhdb(UserUhdbVo param);

    /**
     * user_uhdb 데이타를 조회한다.
     *
     * @param param
     */
    UserUhdbVo selectUserUhdb(UserUhdbVo param);

    /**
     * user_uhdb 데이타를 조회한다.
     *
     * @param param
     */
    List<UserUhdbVo> selectUserUhdbList(UserUhdbVo param);

    /**
     * user_fcm에 데이타를 입력한다.
     *
     * @param param
     */
    void insertUserFcm(UserFcmVo param);

    /**
     * user_fcm 데이타를 삭제한다(del_yn=Y).
     *
     * @param param
     */
    void deleteUserFcm(UserFcmVo param);

    /**
     * user_fcm 데이타를 조회한다.
     *
     * @param param
     */
    UserFcmVo selectUserFcm(UserFcmVo param);

}
