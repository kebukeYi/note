package com.java;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.java.note.spring.mapper")
//@EnableTransactionManagement(proxyTargetClass = true)//强迫事务使用CGLib代理方式
//@EnableAsync
public class NoteApplication {

    public static void main(Strings[] args) {
        SpringApplication.run(NoteApplication.class, args);
    }

}
