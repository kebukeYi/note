package com.java.note.spring.config;

import com.java.note.spring.annotation.MyMapperScan;
import com.java.note.spring.bean.People;
import com.java.note.spring.imports.MyImportSelector;
import com.java.note.spring.ioc.MyBeanDefinitionRegister;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/8  22:31
 * @Description
 */
@Configuration
//@MapperScan("com.java.note.spring.mapper")
@MyMapperScan("com.java.note.spring.mapper")
@Import({MyBeanDefinitionRegister.class, MyImportSelector.class})
@ComponentScan(value = "com.java.note.spring")
//        ,
//        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class}),
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {UserService.class})}, useDefaultFilters = true)

//excludeFilters =Filter[]  指定扫描的时候按照什么规则排除那些组件
//includeFilters =Filter[]  指定扫描的时候只需要包含哪些组件
//FilterType.ASSIGNABLE_TYPE  指定的类型
public class MainConfig {

    /**
     * 容器中的id 为方法名字
     * 设置 bean 名字 value = "userBean"
     *
     * @return
     */
//    @Bean(value = "user")
    public People getUser() {
        return new People("mmy", "23");
    }

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource driverDataSource = new DriverManagerDataSource();
//        driverDataSource.setDriverClassName(Driver.class.getName());
//        driverDataSource.setUrl("jdbc:mysql://localhost:3306/mysqlstudy?characterEncoding=UTF-8&useSSL=false&&serverTimezone=UTC");
//        driverDataSource.setUsername("root");
//        driverDataSource.setPassword("123456");
//        return driverDataSource;
//    }
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource());
//        return factoryBean.getObject();
//    }


}
