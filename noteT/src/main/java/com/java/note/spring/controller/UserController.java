package com.java.note.spring.controller;

import com.java.note.spring.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    PeopleService peopleService;


    @GetMapping("/user/set")
    public String setPeople() {
        peopleService.add03();
        return "success";
    }


}
