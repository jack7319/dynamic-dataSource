package com.bizideal.mn.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author: liulq
 * @Date: 2018/3/10 12:50
 * @Description: 动态数据源切换实现
 * @version: 1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    // 我们自行组装的数据源，包括主数据源和自定义数据源
    // Map<Object, Object> targetDataSources;

    // 默认数据源
    // Object defaultTargetDataSource;

    // 由targetDataSources读取而来，内容其实是一样的,key-dataSource
    // Map<Object, DataSource> resolvedDataSources;

    // 和resolvedDefaultDataSource相同
    // private DataSource resolvedDefaultDataSource;

    /**
     * {@link #AbstractRoutingDataSource {@link #determineTargetDataSource()}}
     * 由key决定数据源，Key在进入方法前已经通过DataSourceContextHolder改变过
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceType = DataSourceContextHolder.getDataSourceType();
        return dataSourceType;
    }
}
