package com.qetch.springmvc.service;

import com.qetch.springmvc.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    Integer saveUser(User user);
}
