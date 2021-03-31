package com.mi.fusheng.service;


import com.mi.fusheng.po.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> queryUsers(Map<String, Object> param);
}
