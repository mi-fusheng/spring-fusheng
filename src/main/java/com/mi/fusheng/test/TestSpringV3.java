package com.mi.fusheng.test;

import com.mi.fusheng.factory.support.DefaultListableBeanFactory;
import com.mi.fusheng.po.User;
import com.mi.fusheng.reader.XmlBeanDefinitionReader;
import com.mi.fusheng.resource.ClasspathResource;
import com.mi.fusheng.service.UserService;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSpringV3 {


   @Test
   public void test() {

      DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

      XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

      ClasspathResource resource = new ClasspathResource("beans.xml");

      InputStream stream = resource.getResource();

      beanDefinitionReader.registerBeanDefinition(stream);
      // 创建UserServiceImpl对象
      UserService userService = (UserService) beanFactory.getBean("userService");

      Map<String, Object> param = new HashMap<>();
      param.put("username", "王五");
      List<User> users = userService.queryUsers(param);
      System.out.println(users);
   }
}
