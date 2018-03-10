package com.bizideal.mn.annotation;

import java.lang.annotation.*;

/**
 * @author: liulq
 * @Date: 2018/3/10 13:25
 * @Description:
 * @version: 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    String name();
}
