/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 14.
 */
package com.realsnake.sample.service.user;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.mapper.user.UserMapper;
import com.realsnake.sample.model.common.Sort;
import com.realsnake.sample.model.user.UserDto;
import com.realsnake.sample.model.user.UserVo;
import com.realsnake.sample.util.PasswordHash;
import com.realsnake.sample.util.crypto.BlockCipherUtils;

/**
 * <pre>
 * Class Name : UserServiceImpl.java
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
@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regUser(UserVo param) throws Exception {
        // 사용자가 입력한 비밀번호를 해싱(단방향 암호화) 처리
        String passwordHash = PasswordHash.createHash(param.getPassword());
        param.setPassword(passwordHash);

        // 사용자의 이름/이메일 등을 암호화
        // String secretKey = BlockCipherUtils.generateSecretKey(passwordHash);
        String secretKey = BlockCipherUtils.generateSecretKey(CommonConstants.DEFAULT_AUTH_KEY);
        param.setName(BlockCipherUtils.encrypt(secretKey, param.getName()));
        param.setMobile(BlockCipherUtils.encrypt(secretKey, param.getMobile()));

        // TODO: 일단 익명권한 부여, 이메일 인증 후 ROLL_USER로 업데이트
        param.setAuthorities(AuthorityUtils.createAuthorityList(CommonConstants.RollType.ROLL_ANONYMOUS.getValue()));

        this.userMapper.insertUser(param);
        this.userMapper.insertUserAuthority(param);
    }

    @Override
    @Transactional(readOnly = true)
    public UserVo findUser(UserVo param) throws Exception {
        UserVo user = this.userMapper.selectUser(param);

        if (user == null) {
            return null;
        }

        user.setAuthorities(this.userMapper.selectUserAuthNameList(user.getSeq()));

        logger.debug("<<사용자 정보>> {}", user);
        logger.debug("<<사용자 이름>> {}", user.getDecName());
        logger.debug("<<사용자 핸드폰번호>> {}", user.getDecMobile());

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserVo findUser(String username) throws Exception {
        UserVo param = new UserVo();
        param.setUsername(username);
        return this.findUser(param);
    }

    @Override
    @Transactional(readOnly = true)
    public UserVo findUser(Integer seq) throws Exception {
        UserVo param = new UserVo();
        param.setSeq(seq);
        return this.findUser(param);
    }

    @Override
    @Transactional(readOnly = true)
    public String checkDoubleUser(String type, String param) throws Exception {
        if (!("username".equalsIgnoreCase(type) || "mobile".equalsIgnoreCase(type))) {
            return "UNKNOWN";
        }

        UserVo user = new UserVo();

        if ("username".equalsIgnoreCase(type)) {
            user.setUsername(param);
        } else if ("mobile".equalsIgnoreCase(type)) { // 핸드폰번호는 사용자의 비밀번호를 키로 암호화되어 있기 때문에 중복검사 불가
            String secretKey = BlockCipherUtils.generateSecretKey(CommonConstants.DEFAULT_AUTH_KEY);
            user.setMobile(BlockCipherUtils.encrypt(secretKey, param));
        }

        if (this.userMapper.selectUser(user) == null) {
            return "NONEXIST";
        } else {
            return "EXIST";
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserVo> findUserList(UserDto param) throws Exception {
        // TODO: 페이징이 필요할 시 페이징 처리
        param.getPagingHelper().setEndNum(Integer.MAX_VALUE);
        Sort sort = new Sort();
        sort.setColumn("seq");
        sort.setAscOrDesc(CommonConstants.SortType.DESC.getValue());
        List<Sort> sortList = new ArrayList<Sort>();
        sortList.add(sort);
        param.getPagingHelper().setSortList(sortList);
        // TODO: 페이징이 필요할 시 페이징 처리

        param.getPagingHelper().setTotalRecordCount(this.userMapper.selectUserListCount(param));
        return this.userMapper.selectUserList(param);
    }

}
