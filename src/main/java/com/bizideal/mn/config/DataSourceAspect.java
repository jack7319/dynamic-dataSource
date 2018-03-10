package com.bizideal.mn.config;

import com.bizideal.mn.annotation.TargetDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author: liulq
 * @Date: 2018/3/10 13:02
 * @Description:
 * @version: 1.0
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        String name = targetDataSource.name();
        logger.debug("切换数据源，{}", name);
        DataSourceContextHolder.setDataSourceType(name);
    }

    @After("@annotation(targetDataSource)")
    public void clearDataSourceType(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        String name = targetDataSource.name();
        logger.debug("清除数据源，{}", name);
        DataSourceContextHolder.clearDataSourceType();
    }
}
