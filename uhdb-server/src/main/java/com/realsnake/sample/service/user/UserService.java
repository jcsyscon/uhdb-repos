/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 14.
 */
package com.realsnake.sample.service.user;

import java.util.List;

import com.realsnake.sample.model.user.UserDto;
import com.realsnake.sample.model.user.UserVo;

/**
 * <pre>
 * Class Name : UserService.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 14.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 14.
 * @version 1.0
 */
public interface UserService {

    void regUser(UserVo param) throws Exception;

    UserVo findUser(UserVo param) throws Exception;

    UserVo findUser(String username) throws Exception;

    UserVo findUser(Integer seq) throws Exception;

    String checkDoubleUser(String type, String param) throws Exception;

    List<UserVo> findUserList(UserDto param) throws Exception;

    void modifyUser(UserVo param) throws Exception;

    void modifyUserPassword(UserVo param) throws Exception;

    void secedeUser(UserVo param) throws Exception;

}
