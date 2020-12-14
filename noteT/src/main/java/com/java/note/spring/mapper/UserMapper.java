package com.java.note.spring.mapper;

import com.java.note.mybatis.dto.StatisticalBrandDto;
import com.java.note.mybatis.vo.StatisticalBrandVo;
import com.java.note.redis.bean.User;

import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/23  16:09
 * @Description
 */
public interface UserMapper {

    User getUserByAge(int age);

    List<StatisticalBrandVo> getStatisticalBrandList(StatisticalBrandDto statisticalBrandDto);
}
