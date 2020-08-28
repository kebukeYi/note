package com.java.note.spring.Repository;

import com.java.note.spring.bean.Log;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/10  22:31
 * @Description
 */
public interface LogRepository extends JpaRepository<Log, Integer> {
    
}
