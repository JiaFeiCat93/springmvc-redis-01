package com.qetch.springmvc.controller;

import com.qetch.springmvc.dao.UserMapper;
import com.qetch.springmvc.entity.User;
import com.qetch.springmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/userlist")
    public ModelAndView userList(ModelAndView modelAndView) {
        List<User> users = userService.getUsers();
        modelAndView.addObject("users", users);
        modelAndView.setViewName("UserList");
        return  modelAndView;
    }

    @RequestMapping("/saveuser")
    public String saveUser(@ModelAttribute User user, Model model) {
        userService.saveUser(user);
        List<User> users = userService.getUsers();
        model.addAttribute("user", users);
        return "UserList";
    }
}
