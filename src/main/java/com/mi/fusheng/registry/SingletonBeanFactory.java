package com.mi.fusheng.registry;

/**
 *
 * 针对存储单例Bean集合提供对外的操作功能
 * */
public interface SingletonBeanFactory {

    /**
     * 获取单例Bean
     * */
    Object getSington(String name);

    /**
     * 存储单例Bean
     * */
    void addSingleton(String name, Object bean);
}
