package com.mi.fusheng.factory.support;

import com.mi.fusheng.ioc.BeanDefinition;
import com.mi.fusheng.ioc.PropertyValue;
import com.mi.fusheng.ioc.RuntimeBeanReference;
import com.mi.fusheng.ioc.TypedStringValue;
import com.mi.fusheng.utils.ReflectUtils;

import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    protected Object doCreateBean(BeanDefinition beanDefinition) {
        Class<?> clazzType = beanDefinition.getClazzType();

        if (clazzType == null) {
            return null;
        }

        Object bean = createInstanceBean(clazzType);

        if (bean == null) {
            return null;
        }

        populateBean(bean, beanDefinition);

        initMethod(bean, beanDefinition);

        return bean;
    }

    private Object createInstanceBean(Class<?> clazzType) {

        //TODO 通过实例工厂方式去创建Bean实例，比如通过factory-bean标签属性指的FactoryBean工厂去创建；
        //TODO 通过静态工厂方法方式去创建Bean实例，比如通过factory-method标签属性指的静态方法去创建实例；
        //构造方法去创建Bean实例（反射，此处只针对无参构造进行操作）
        Object bean = ReflectUtils.createObject(clazzType);

        return bean;
    }


    private void populateBean(Object bean, BeanDefinition beanDefinition) {
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();

        if (propertyValues != null && !propertyValues.isEmpty()) {
            for (PropertyValue pv : propertyValues) {
                String name = pv.getName();
                Object value = pv.getValue();

                Object valueToUse = null;

                if (value instanceof TypedStringValue) {
                    TypedStringValue typedStringValue = (TypedStringValue) value;
                    String stringValue = typedStringValue.getValue();
                    Class<?> targetType = typedStringValue.getTargetType();

                    //TODO 通过策略模式去优化
                    //typedStringValue.getTypeHandler();
                    if (targetType == Integer.class) {
                        valueToUse = Integer.parseInt(stringValue);

                    } else if (targetType == String.class) {
                        valueToUse = stringValue;
                    }


                } else if (value instanceof RuntimeBeanReference) {
                    RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) value;
                    String ref = runtimeBeanReference.getRef();
                    valueToUse = getBean(ref);

                }

                //通过反射去给Bean实例设置指定name的值
                ReflectUtils.setProperty(bean, name, valueToUse);
            }
        }
    }

    private void initMethod(Object bean, BeanDefinition beanDefinition) {
        // 通过调用Bean标签指定的初始化方法，比如通过init-method标签属性指定到方法

        String initMethod = beanDefinition.getInitMethod();

        if (initMethod == null) {
            return;
        }

        ReflectUtils.invokeInitMethod(bean, initMethod);
    }
}
