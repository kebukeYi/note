package com.mmy.mvc.service;

import com.mmy.mvc.bean.Account;

import java.util.List;

/**
 * @author : kebukeyi
 * @date :  2021-09-17 20:25
 * @description :
 * @question :
 * @usinglink :
 **/
public interface IAccountService {

    void save(Account account);

    List<Account> findAll();

}
