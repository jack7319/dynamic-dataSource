package com.bizideal.mn;

import com.bizideal.mn.entity.UserInfo;
import com.bizideal.mn.service.UserInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {


    @Autowired
    private UserInfoService userInfoService;

    @Test
    public void testS(){
        int jliulq = userInfoService.addUserInfo("jliulq");
        UserInfo userInfo = userInfoService.selectById(1);
        System.out.println(userInfo);
    }
}
