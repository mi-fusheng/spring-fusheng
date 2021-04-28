package com.mi.fusheng.factory;

/**
 *
 * Bean工厂的顶级接口，该接口按照接口隔离原则，只保留最根本的bean的获取操作
 * */
public interface BeanFactory {

    /**
     * 根据bean的name从Bean工厂中获取指定名称的Bean实例
     *
     * */
    Object getBean(String name);
}
