package com.mi.fusheng.registry;


import com.mi.fusheng.ioc.BeanDefinition;

/**
 * 针对存储单例BeanDefinition集合提供对外的操作功能
 * */
public interface BeanDefinitionRegistry {

    /**
     * 获取BeanDefinition
     * */
    BeanDefinition getBeanDefinition(String name);

    /**
     * 存储BeanDefinition
     * */
    void registerBeanDefinition(String name, BeanDefinition beanDefinition);
}
