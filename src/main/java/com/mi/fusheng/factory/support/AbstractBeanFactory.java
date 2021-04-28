package com.mi.fusheng.factory.support;

import com.mi.fusheng.factory.BeanFactory;
import com.mi.fusheng.ioc.BeanDefinition;
import com.mi.fusheng.registry.support.DefaultSingletonBeanRegistry;

/**
 * 该方法对BeanFactory的方法进行了实现，但只是定义了getBean的步骤，而细节部分需要交给子类实现
 * */
public abstract class AbstractBeanFactory  extends DefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String name) {
        //先从一级缓存中获取单例Bean到实例

        Object bean = getSington(name);

        if (bean != null) {
            return bean;
        }

        //懒汉式,等getBean的时候,并且singletonObjects没有实例到时候，才会去创建实例
        //当缓存中没有找到Bean实例，则需要创建Bean，然后将该Bean放入一级缓存中
        //要创建Bean，需要知道该Bean到信息（信息配置到XML中）

        //根据beanName去BeanDefinitions中获取对应的Bean信息
        BeanDefinition beanDefinition = getBeanDefinition(name);

        if (beanDefinition == null) {
            return null;
        }

        //根据Bean的信息，来判断该bean是单例还是多例（原型）bean
        if (beanDefinition.isSingleton()) {
            //根据Bean到新学期创建Bean的对象
            bean = doCreateBean(beanDefinition);
            //将创建的Bean对象，存入到singletonObjects
            addSingleton(name, bean);
        } else if (beanDefinition.isPrototype()) {
            //根据Bean到信息去创建Bean的对象
            bean = doCreateBean(beanDefinition);
        } else {
            //TODO
        }

        return bean;
    }

    /**
     * 使用抽象模板方法，将具体创建Bean实例的功能延迟到子类去实现
     * */
    protected abstract Object doCreateBean(BeanDefinition beanDefinition);

    /**
     * 使用抽象模板方法，将获取BeanDefinition的功能延迟到子类去实现
     * */
    protected abstract BeanDefinition getBeanDefinition(String name);
}
