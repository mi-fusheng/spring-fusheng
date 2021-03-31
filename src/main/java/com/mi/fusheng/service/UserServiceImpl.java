package com.mi.fusheng.service;


import com.mi.fusheng.dao.UserDao;
import com.mi.fusheng.po.User;

import java.util.List;
import java.util.Map;


public class UserServiceImpl implements UserService {

	// 依赖注入UserDao
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public List<User> queryUsers(Map<String, Object> param) {
		return userDao.queryUserList(param);
	}
}
