package com.mi.fusheng.registry.support;

import com.mi.fusheng.registry.SingletonBeanFactory;

import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonBeanRegistry implements SingletonBeanFactory {
    private Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSington(String name) {
        return this.singletonObjects.get(name);
    }

    @Override
    public void addSingleton(String name, Object bean) {
        this.singletonObjects.put(name, bean);
    }
}
