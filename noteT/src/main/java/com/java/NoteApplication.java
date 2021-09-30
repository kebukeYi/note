package com.java;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.java.note.spring.mapper")
@EnableTransactionManagement(proxyTargetClass = true)//强迫事务使用CGLib代理方式
//@EnableTransactionManagement(proxyTargetClass = false)//强迫事务使用JDK动态代理方式[需要接口哦]
//@EnableAsync
public class NoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteApplication.class, args);
    }

}
