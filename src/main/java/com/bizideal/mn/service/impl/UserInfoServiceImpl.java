package com.bizideal.mn.service.impl;

import com.bizideal.mn.annotation.TargetDataSource;
import com.bizideal.mn.entity.UserInfo;
import com.bizideal.mn.mapper.UserInfoMapper;
import com.bizideal.mn.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: liulq
 * @Date: 2018/3/10 13:43
 * @Description:
 * @version: 1.0
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    @TargetDataSource(name = "ds1")
    public UserInfo selectById(int id) {
        return userInfoMapper.selectById(id);
    }

    @Override
    @TargetDataSource(name = "ds2")
    public int addUserInfo(String name) {
        return userInfoMapper.addUserInfo(new UserInfo().setName(name));
    }
}
