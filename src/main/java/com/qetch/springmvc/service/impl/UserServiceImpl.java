package com.qetch.springmvc.service.impl;

import com.qetch.springmvc.dao.UserMapper;
import com.qetch.springmvc.entity.User;
import com.qetch.springmvc.entity.UserExample;
import com.qetch.springmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getUsers() {
        UserExample example = new UserExample();
        return userMapper.selectByExample(example);
    }

    @Override
    public Integer saveUser(User user) {
        return userMapper.insertSelective(user);
    }
}
