package com.bizideal.mn.service;

import com.bizideal.mn.entity.UserInfo;

/**
 * @author: liulq
 * @Date: 2018/3/10 13:42
 * @Description:
 * @version: 1.0
 */
public interface UserInfoService {

    UserInfo selectById(int id);

    int addUserInfo(String name);
}
