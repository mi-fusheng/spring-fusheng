package com.mi.fusheng.test;

import com.mi.fusheng.ioc.BeanDefinition;
import com.mi.fusheng.ioc.PropertyValue;
import com.mi.fusheng.ioc.RuntimeBeanReference;
import com.mi.fusheng.ioc.TypedStringValue;
import com.mi.fusheng.po.User;
import com.mi.fusheng.service.UserService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSpringV2 {

   //实现思路（面向过程）
   //采取Map集合中存储单例

   //key:BeanName
   //value:Bean实例对象
   private Map<String, Object> singletonObjects = new HashMap<String, Object>();

   //key:BeanName
   //value:BeanDefinition对象
   private Map<String, BeanDefinition> beanDefinitions = new HashMap<String, BeanDefinition>();

   @Test
   public void test() {
      // 从XML中加载配置信息，先完成BeanDefinition的注册
      registerBeanDefinitions();
      // 创建UserServiceImpl对象
      UserService userService = (UserService) getBean("userService");

      Map<String, Object> param = new HashMap<>();
      param.put("username", "王五");
      List<User> users = userService.queryUsers(param);
      System.out.println(users);
   }

   public void registerBeanDefinitions() {
      // XML解析，将BeanDefinition注册到beanDefinitions集合中
      String location = "beans.xml";
      // 获取流对象
      InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(location);
      // 创建文档对象
      Document document = createDocument(inputStream);

      //按照Spring定义的标签去解析Document
      praseBeanDefinitions(document.getRootElement());
   }

   public void praseBeanDefinitions(Element rootElement) {
      // 获取<bean>和自定义标签（比如mvc:interceptors）
      List<Element> elements = rootElement.elements();

      for(Element element : elements) {
         //获取标签名称
         String name = element.getName();
         if(name.equals("bean")) {
            parseDefaultElement(element);
         }
         else {
            parseCustomElement(element);
         }
      }

   }

   private void parseCustomElement(Element rootElement) {
      //TODO
   }

   public void parseDefaultElement(Element beanElement) {
      try {

         if(beanElement == null) {
            return;
         }

         String id = beanElement.attributeValue("id");
         String name = beanElement.attributeValue("name");

         String clazzName = beanElement.attributeValue("class");
         if(clazzName == null || "".equals(clazzName)) {
            return;
         }

         String initMethod = beanElement.attributeValue("init-method");
         String scope = beanElement.attributeValue("scope");

         scope = scope == null || "".equals(scope) ? "singleton" : scope;

         String beanName = id == null ? name : id;


         Class<?> clazzType = Class.forName(clazzName);

         beanName = beanName == null ? clazzType.getSimpleName() : beanName;

         //创建beanDefinition对象
         //TODO 可以使用构建者模式优化
         BeanDefinition beanDefinition = new BeanDefinition(clazzName, beanName);

         beanDefinition.setInitMethod(initMethod);
         beanDefinition.setScope(scope);

         //获取property子标签集合
         List<Element> propertyElements = beanElement.elements();

         for(Element propertyElement : propertyElements) {
            prasePropertyElement(beanDefinition, propertyElement);
         }

         //注册BeanDefinition信息
         this.beanDefinitions.put(beanName, beanDefinition);

      }
      catch(ClassNotFoundException e) {
         e.printStackTrace();
      }

   }

   private void prasePropertyElement(BeanDefinition beanDefinition, Element propertyElement) {

      if(propertyElement == null) {
         return;
      }

      String name = propertyElement.attributeValue("name");
      String value = propertyElement.attributeValue("value");
      String ref = propertyElement.attributeValue("ref");

      // 如果value和ref都有值，则返回
      if(value != null && !value.equals("") && ref != null && !ref.equals("")) {
         return;
      }

      PropertyValue pv = null;

      if(value != null && !"".equals(value)) {
         //因为Spring配置文件中的value是String类型，而对象中的属性是各种各样的，所以需要存储类型
         TypedStringValue typedStringValue = new TypedStringValue(value);
         Class<?> targetType = getTypeByFieldName(beanDefinition.getClazzName(), name);
         typedStringValue.setTargetType(targetType);

         pv = new PropertyValue(name, typedStringValue);
         beanDefinition.addPropertyValue(pv);
      }
      else if(ref != null && !ref.equals("")) {
         RuntimeBeanReference reference = new RuntimeBeanReference(ref);
         pv = new PropertyValue(name, reference);
         beanDefinition.addPropertyValue(pv);
      }
      else {
         return;
      }
   }

   private Class<?> getTypeByFieldName(String clazzName, String name) {

      try {
         Class<?> clazz = Class.forName(clazzName);
         Field field = clazz.getDeclaredField(name);
         return field.getType();
      }
      catch(Exception e) {
         e.printStackTrace();
      }

      return null;
   }

   private Document createDocument(InputStream inputStream) {
      Document document = null;
      try {
         SAXReader reader = new SAXReader();
         document = reader.read(inputStream);
         return document;
      }
      catch(DocumentException e) {
         e.printStackTrace();
      }
      return null;
   }

   @Before
   public void init() {
      //饿汉式
      //singletonObjects.put(key,value);
      //XML解析，将BeanDefinition注册到beanDefinitions集合中
   }

   private Object getBean(String beanName) {
      //先从一级缓存中获取单例Bean到实例

      Object sinleTonObject = singletonObjects.get(beanName);

      if(sinleTonObject != null) {
         return sinleTonObject;
      }

      //懒汉式,等getBean的时候,并且singletonObjects没有实例到时候，才会去创建实例
      //当缓存中没有找到Bean实例，则需要创建Bean，然后将该Bean放入一级缓存中
      //要创建Bean，需要知道该Bean到信息（信息配置到XML中）

      //根据beanName去BeanDefinitions中获取对应的Bean信息
      BeanDefinition beanDefinition = beanDefinitions.get(beanName);

      if(beanDefinition == null) {
         return null;
      }

      //根据Bean的信息，来判断该bean是单例还是多例（原型）bean
      if(beanDefinition.isSingleton()) {
         //根据Bean到新学期创建Bean的对象
         sinleTonObject = crateBean(beanDefinition);
         //将创建的Bean对象，存入到singletonObjects
         singletonObjects.put(beanName, sinleTonObject);
      }
      else if(beanDefinition.isPrototype()) {
         //根据Bean到信息去创建Bean的对象
         sinleTonObject = crateBean(beanDefinition);
      }
      else {
         //TODO
      }

      return sinleTonObject;
   }

   private Object crateBean(BeanDefinition beanDefinition) {
      Class<?> clazzType = beanDefinition.getClazzType();

      if(clazzType == null) {
         return null;
      }

      Object bean = createInstanceBean(clazzType);

      if(bean == null) {
         return null;
      }

      populateBean(bean, beanDefinition);

      initMethod(bean, beanDefinition);

      return bean;
   }

   private void initMethod(Object bean, BeanDefinition beanDefinition) {
      //TODO 判断Bean是否实现了Aware接口
      //TODO  判断是否实现了InitilizingBean接口，如果实现，则直接调用该Bean的afterPropertiesSet方法去初始化

      // 通过调用Bean标签指定的初始化方法，比如通过init-method标签属性指定到方法

      String initMethod = beanDefinition.getInitMethod();

      if(initMethod == null) {
         return;
      }

      invokeInitMethod(bean, initMethod);

   }

   private void invokeInitMethod(Object bean, String initMethod) {
      try {
         Class<?> clazz = bean.getClass();
         Method method = clazz.getDeclaredMethod(initMethod);
         method.invoke(bean);
      }
      catch(Exception e) {

      }

   }

   private void populateBean(Object bean, BeanDefinition beanDefinition) {
      List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();

      if(propertyValues != null && !propertyValues.isEmpty()) {
         for(PropertyValue pv : propertyValues) {
            String name = pv.getName();
            Object value = pv.getValue();

            Object valueToUse = null;

            if(value instanceof TypedStringValue) {
               TypedStringValue typedStringValue = (TypedStringValue) value;
               String stringValue = typedStringValue.getValue();
               Class<?> targetType = typedStringValue.getTargetType();

               //TODO 通过策略模式去优化
               //typedStringValue.getTypeHandler();
               if(targetType == Integer.class) {
                  valueToUse = Integer.parseInt(stringValue);

               }
               else if(targetType == String.class) {
                  valueToUse = stringValue;
               }


            }
            else if(value instanceof RuntimeBeanReference) {
               RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) value;
               String ref = runtimeBeanReference.getRef();
               valueToUse = getBean(ref);

            }

            //通过反射去给Bean实例设置指定name的值
            setProperty(bean, name, valueToUse);

         }
      }


   }

   private void setProperty(Object bean, String name, Object valueToUse) {
      try {
         Class<?> clazz = bean.getClass();
         Field field = clazz.getDeclaredField(name);
         field.setAccessible(true);

         field.set(bean, valueToUse);

      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }

   private Object createInstanceBean(Class<?> clazzType) {

      //TODO 通过实例工厂方式去创建Bean实例，比如通过factory-bean标签属性指的FactoryBean工厂去创建；
      //TODO 通过静态工厂方法方式去创建Bean实例，比如通过factory-method标签属性指的静态方法去创建实例；
      //构造方法去创建Bean实例（反射，此处只针对无参构造进行操作）
      Object bean = doCreateInstanceWithConstructor(clazzType);


      return bean;
   }

   private Object doCreateInstanceWithConstructor(Class<?> clazzType) {
      try {
         // TODO 获取所有的构造方法
         // TODO 根据BeanDefinition中存储的constructor-arg子标签的数据来获取构造参数类型
         Constructor<?> constructor = clazzType.getDeclaredConstructor();
         return constructor.newInstance();
      }
      catch(Exception e) {
         //handle Exception
      }

      return null;
   }
}
