package com.java.note.spring.mapper;

import com.java.note.mybatis.vo.PeopleDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/24  11:20
 * @Description
 */
public interface PeopleMapper {

    @Select("select * from people  where age=#{age}")
    String selectById(Integer age);

    Integer insertPeople(PeopleDto peopleDto);

    Integer insertBatch(@Param("list") List<PeopleDto> peopleDtos);

}
