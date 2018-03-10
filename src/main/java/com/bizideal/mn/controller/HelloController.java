package com.bizideal.mn.controller;

import com.bizideal.mn.entity.UserInfo;
import com.bizideal.mn.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liulq
 * @Date: 2018/3/10 15:56
 * @Description:
 * @version: 1.0
 */
@RestController
@RequestMapping("/")
public class HelloController {

    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/set/{name}")
    public int add(@PathVariable String name) {
        int i = userInfoService.addUserInfo(name);
        return i;
    }

    @GetMapping("/get/{id}")
    public UserInfo getById(@PathVariable int id) {
        return userInfoService.selectById(id);
    }
}
