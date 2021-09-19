package com.mmy.mvc.service.lmpl;

import com.mmy.mvc.bean.Account;
import com.mmy.mvc.mapper.AccountMapper;
import com.mmy.mvc.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : kebukeYi
 * @date :  2021-09-17 20:25
 * @description:
 * @question:
 * @link:
 **/
@Service
public class AccountServicelmpl implements IAccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void save(Account account) {
        //另外一种 mybatis 没有和spring 进行整合的使用方案
        //        try {
//            //加载配置文件
//            InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
//            //构建会话工厂
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
//            //创建连接
//            SqlSession sqlSession = sqlSessionFactory.openSession();
//            //获取映射对象
//            AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);
//            //执行方法
//            mapper.save(account);
//            //提交连接
//            sqlSession.commit();
//            //关闭连接
//            sqlSession.close();
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
        accountMapper.save(account);
    }

    @Override
    public List<Account> findAll() {
        //另外一种 mybatis 没有和spring 进行整合的使用方案
        //        try {
//            InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
//            SqlSession sqlSession = sqlSessionFactory.openSession();
//            AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);
//            List<Account> accountList = mapper.findAll();
//            sqlSession.close();
//            return accountList;
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
        return accountMapper.findAll();
    }
}
 
