package com.mi.fusheng.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static void setProperty(Object bean, String name, Object valueToUse) {
        try {
            Class<?> clazz = bean.getClass();
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);

            field.set(bean, valueToUse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void invokeInitMethod(Object bean, String initMethod) {
        try {
            Class<?> clazz = bean.getClass();
            Method method = clazz.getDeclaredMethod(initMethod);
            method.invoke(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object createObject(Class<?> clazz, Object... args) {
        try {
            // TODO 可以根据输入参数获取指定构造参数的构造方法
            Constructor<?> constructor = clazz.getConstructor();
            // 默认调用无参构造进行对象的创建
            return constructor.newInstance(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
