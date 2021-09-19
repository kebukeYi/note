package com.mmy.mvc.mapper;

import com.mmy.mvc.bean.Account;

import java.util.List;

/**
 * @author : kebukeyi
 * @date :  2021-09-17 20:22
 * @description :
 * @question :
 * @usinglink :
 **/
public interface AccountMapper {

    void save(Account account);

    List<Account> findAll();

}
