package com.mi.fusheng.reader;


import com.mi.fusheng.registry.BeanDefinitionRegistry;

import java.io.InputStream;

/**
 * 专门用来解析XML中的BeanDefinition信息
 * */
public class XmlBeanDefinitionReader {

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void registerBeanDefinition(InputStream inputStream){
    }
}
