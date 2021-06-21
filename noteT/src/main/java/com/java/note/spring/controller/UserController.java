package com.java.note.spring.controller;

import com.java.note.spring.service.PeopleService;
import com.java.note.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/8  22:47
 * @Description
 */
@RestController
public class UserController {

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private UserService userService;


    @GetMapping("/people/set")
    public Strings setPeople(Integer id) {
        peopleService.selectPeopleById(id);
        return "success";
    }


    @GetMapping("/user/set")
    public Strings setUser() {
        userService.insertUserTry();
        return "success";
    }


}
