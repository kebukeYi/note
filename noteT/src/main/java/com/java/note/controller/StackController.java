package com.java.note.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * @author : kebukeYi
 * @date :  2021-08-01 17:56
 * @description:
 * @question:
 * @link:
 **/
@RestController
@RequestMapping("/stack")
public class StackController {


    @GetMapping("/info")
    public Map getStackInfo() {
        final Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        return allStackTraces;
    }

    public static void main(String[] args) {
        System.out.println(Thread.getAllStackTraces());
    }
}
 
