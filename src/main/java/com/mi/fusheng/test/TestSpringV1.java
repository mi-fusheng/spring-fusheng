package com.mi.fusheng.test;


import com.mi.fusheng.dao.UserDao;
import com.mi.fusheng.dao.UserDaoImpl;
import com.mi.fusheng.po.User;
import com.mi.fusheng.service.UserService;
import com.mi.fusheng.service.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSpringV1 {

    @Test
    public void test() throws Exception {
        // 创建UserServiceImpl对象
        UserServiceImpl userService = new UserServiceImpl();
        UserDaoImpl userDao = new UserDaoImpl();
        userService.setUserDao(userDao);
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        userDao.setDataSource(dataSource);

        // 根据用户名称查询用户信息

        User user = new User();
        user.setUsername("王五");
        Map<String, Object> param = new HashMap<>();
        param.put("username", "王五");
        List<User> users = userService.queryUsers(param);
        System.out.println(users);
    }
}
