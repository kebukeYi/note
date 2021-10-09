package com.my.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @author : kebukeYi
 * @date :  2021-10-04 20:44
 * @description:
 * @question:
 * @link:
 **/
@Controller
public class IndexController {


    @PostMapping("/demo/demo_form")
    public String index(Map<String, String> map) {
        System.out.println(map);
        return "user";
    }


    @GetMapping("/index")
    public String index1() {
        return "index.html";
    }

}
 
