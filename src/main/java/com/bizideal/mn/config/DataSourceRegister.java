package com.bizideal.mn.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: liulq
 * @Date: 2018/3/10 11:43
 * @Description: 数据源注册
 * @version: 1.0
 */
@Configuration
public class DataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceRegister.class);

    // 提供类型转换服务
    private ConversionService conversionService = new DefaultConversionService();

    // 用于存储spring.datasource.后面的其他key-value
    private PropertyValues dataSourcePropertyValues;

    // 数据源的几个键
    public static final String defaultDataSourceType = "org.apache.tomcat.jdbc.pool.DataSource";
    public static final String TYPE = "type";
    public static final String DRIVER_CLASS_NAME = "driver-class-name";
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    // 默认数据源
    private DataSource defaultDataSource;

    // 其他自定义数据源
    private Map<String, DataSource> otherDataSource = new HashMap<String, DataSource>();

    @Override
    public void setEnvironment(Environment environment) {
        initDefaultDataSource(environment);
        initOtherDataSource(environment);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, DataSource> targetDataSources = new HashMap<String, DataSource>();
        targetDataSources.put("dataSource", defaultDataSource);
        for (Map.Entry<String, DataSource> entry : otherDataSource.entrySet())
            targetDataSources.put(entry.getKey(), entry.getValue());

        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(DynamicDataSource.class);
        genericBeanDefinition.setSynthetic(true);
        MutablePropertyValues propertyValues = genericBeanDefinition.getPropertyValues();
        propertyValues.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        propertyValues.addPropertyValue("targetDataSources", targetDataSources);

        // 注册dataSource
        registry.registerBeanDefinition("dataSource", genericBeanDefinition);

        logger.info("数据源加载完成...");
    }

    /**
     * 初始化默认数据源
     *
     * @param environment
     */
    public void initDefaultDataSource(Environment environment) {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        RelaxedPropertyResolver relaxedPropertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
        hashMap.put(DRIVER_CLASS_NAME, relaxedPropertyResolver.getProperty(DRIVER_CLASS_NAME));
        hashMap.put(URL, relaxedPropertyResolver.getProperty(URL));
        hashMap.put(USERNAME, relaxedPropertyResolver.getProperty(USERNAME));
        hashMap.put(PASSWORD, relaxedPropertyResolver.getProperty(PASSWORD));

        this.defaultDataSource = buildDataSource(hashMap);

    }

    /**
     * 初始化其他自定义数据源
     *
     * @param environment
     */
    public void initOtherDataSource(Environment environment) {
        Map<String, String> hashMap = new HashMap<String, String>();
        RelaxedPropertyResolver relaxedPropertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
        String names = relaxedPropertyResolver.getProperty("names");
        if (null == names) {
            return;
        }
        // 对多个自定义数据源分别加载
        for (String dataSourceName : StringUtils.split(names, ",")) {
            Map<String, Object> subProperties = relaxedPropertyResolver.getSubProperties(dataSourceName + ".");
            DataSource dataSource = buildDataSource(subProperties);
            otherDataSource.put(dataSourceName, dataSource);
            dataBinder(dataSource, environment);
        }
    }

    /**
     * 创建数据源
     *
     * @param hashMap 包含数据库连接配置的map
     * @return
     */
    private DataSource buildDataSource(Map<String, Object> hashMap) {
        try {
            String type = (String) hashMap.get(TYPE);
            if (StringUtils.isBlank(type)) {
                type = defaultDataSourceType;
            }
            Class<? extends DataSource> dataSourceType = (Class<? extends DataSource>) Class.forName(type);
            String driveClassName = (String) hashMap.get(DRIVER_CLASS_NAME);
            String url = (String) hashMap.get(URL);
            String username = (String) hashMap.get(USERNAME);
            String password = (String) hashMap.get(PASSWORD);
            DataSourceBuilder build = DataSourceBuilder.create().type(dataSourceType).driverClassName(driveClassName).url(url).username(username).password(password);
            return build.build();
        } catch (ClassNotFoundException e) {
            logger.error("加载数据源出错..");
        }
        return null;
    }

    /**
     * 给数据源绑定更多数据。即给自定义数据源加上配置，即除type/driver-class-name/url/username/password外的配置，例如最大连接数
     *
     * @param dataSource
     * @param environment
     */
    private void dataBinder(DataSource dataSource, Environment environment) {
        RelaxedDataBinder relaxedDataBinder = new RelaxedDataBinder(dataSource);
        relaxedDataBinder.setConversionService(conversionService);
        relaxedDataBinder.setIgnoreNestedProperties(false);
        relaxedDataBinder.setIgnoreInvalidFields(false);
        // 忽略未知字段
        relaxedDataBinder.setIgnoreUnknownFields(true);

        if (null == dataSourcePropertyValues) {
            Map<String, Object> rpr = new RelaxedPropertyResolver(environment, "spring.datasource").getSubProperties(".");
            // 这里如果不对map进行一次转换，那么rpr.remove(TYPE);会直接报错。因为返回的是Collections.unmodifiableMap(subProperties);
            HashMap<String, Object> hashMap = new HashMap<String, Object>(rpr);
            hashMap.remove(TYPE);
            hashMap.remove(DRIVER_CLASS_NAME);
            hashMap.remove(URL);
            hashMap.remove(USERNAME);
            hashMap.remove(PASSWORD);
            dataSourcePropertyValues = new MutablePropertyValues(hashMap);
        }

        relaxedDataBinder.bind(dataSourcePropertyValues);
    }
}
