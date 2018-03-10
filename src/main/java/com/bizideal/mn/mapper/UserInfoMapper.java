package com.bizideal.mn.mapper;

import com.bizideal.mn.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * @author: liulq
 * @Date: 2018/3/10 13:39
 * @Description:
 * @version: 1.0
 */
public interface UserInfoMapper {

    @Select("SELECT * FROM user_info WHERE id = #{id}")
    UserInfo selectById(int id);

    @Insert("INSERT INTO user_info(name) VALUES (#{name})")
    int addUserInfo(UserInfo userInfo);
}
