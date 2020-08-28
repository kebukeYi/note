package com.java.note.spring.Repository;

import com.java.note.spring.bean.People;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/10  22:30
 * @Description
 */
public interface UserRepository extends JpaRepository<People, Integer> {

}
