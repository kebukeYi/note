package com.mmy.mvc.controller;

import com.mmy.mvc.bean.UserDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author : kebukeYi
 * @date :  2021-09-18 21:15
 * @description:
 * @question:
 * @link:
 **/
@RestController
@RequestMapping("/valid")
public class TestValidController {

    @PostMapping("/login")
    public Object test(@Valid UserDto user) {
        return user.toString();
    }
}
 
