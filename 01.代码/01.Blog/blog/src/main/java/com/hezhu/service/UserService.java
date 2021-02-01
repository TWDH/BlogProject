package com.hezhu.service;

import com.hezhu.po.User;

public interface UserService {
    User checkUser(String username, String password);
}
