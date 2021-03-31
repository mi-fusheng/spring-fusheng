package com.mi.fusheng.dao;


import com.mi.fusheng.po.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    List<User> queryUserList(Map<String, Object> param);
}
