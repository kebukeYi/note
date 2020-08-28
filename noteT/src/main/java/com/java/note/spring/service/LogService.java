package com.java.note.spring.service;

import com.java.note.spring.Repository.LogRepository;
import com.java.note.spring.bean.Log;
import com.java.note.spring.dao.LogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/10  22:02
 * @Description
 */
@Service
public class LogService {

//    @Autowired
//    LogDao logDao;

//    @Autowired(required = true)
//    LogRepository logRepository;
//
//
//
//
//    @Transactional(readOnly = true)
//    public void add03() {
//        logRepository.save(new Log(new Date().toString(), ""));
//        int i = 10 / 0;
//        logRepository.save(new Log(new Date().toString(), ""));
//    }

}
