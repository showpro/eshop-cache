package com.zhan.eshop.cache.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.zhan.eshop.cache.model.User;

/**
 * 测试用户的Mapper接口
 */
@Repository
public interface UserMapper {

    /**
     * 查询测试用户的信息
     *
     * @return
     */
    public User findUserInfo();

}
