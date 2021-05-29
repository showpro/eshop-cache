package com.zhan.eshop.cache.service;

import com.zhan.eshop.cache.model.User;

/**
 * 用户Service接口
 *
 * @author zhanzhan
 * @date 2021/5/21 22:40
 */
public interface UserService {
    /**
     * 查询用户信息
     *
     * @return 用户信息
     */
    User findUserInfo();

}
