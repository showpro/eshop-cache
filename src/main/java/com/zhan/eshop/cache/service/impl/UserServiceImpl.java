package com.zhan.eshop.cache.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhan.eshop.cache.mapper.UserMapper;
import com.zhan.eshop.cache.model.User;
import com.zhan.eshop.cache.service.UserService;

/**
 * @author zhanzhan
 * @date 2021/5/21 22:42
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserInfo() {
        return userMapper.findUserInfo();
    }
}
