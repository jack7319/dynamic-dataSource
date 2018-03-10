package com.bizideal.mn.config;

/**
 * @author: liulq
 * @Date: 2018/3/10 11:55
 * @Description: 多线程下，记录下每个线程设置的数据源名称
 * @version: 1.0
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> local = new ThreadLocal<String>();

    public static void setDataSourceType(String dataSourceType) {
        local.set(dataSourceType);
    }

    public static String getDataSourceType() {
        return local.get();
    }

    public static void clearDataSourceType() {
        local.remove();
    }
}
