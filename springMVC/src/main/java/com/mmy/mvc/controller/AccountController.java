package com.mmy.mvc.controller;

import com.mmy.mvc.bean.Account;
import com.mmy.mvc.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * @author : kebukeYi
 * @date :  2021-09-17 20:30
 * @description: 一:springMVC 处理大体流程
 * 1.浏览器访问 http://localhost:8080/account/index
 * 2.来到tomcat服务器,那么谁来处理请求?  springmvc的前端控制器收到所有的请求
 * 3. 进行请求路径匹配 @RequestMapping 注解 找到哪个类的哪个方法来处理
 * 4. 前端控制器 springmvc 找到了 目标类以及目标方法 直接利用 反射进行调用
 * 5.执行完成以后,会有一个返回值,springmvc会认为这个是 要返回的页面.
 * 6.拿到方法返回值以后就根据视图解析器  进行拼接 获得完整的页面路径
 * 7.拿到页面地址,前端控制器帮我们转发到这个页面上
 * 二: @RequestMapping  可以设置到方法上也可以设置到类上
 * 1.   @RequestMapping 进行标注请求路径
 * 2.
 * 三: 配置文件
 * 在web.xml中不指定配置文件后 springmvc会在启动后找默认的 WEB-INF/{dispatcher}-servlet.xml
 * @question:
 * @link:
 **/
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @RequestMapping(value = "/save", produces = "text/html;charset=UTF-8")
    @ResponseBody//RequestResponseBodyMethodProcessor 针对返回值处理
    public String save(Account account) {
        accountService.save(account);
        System.out.println(account);
        return "保存成功";
    }

    @RequestMapping(value = "/index")
    // private  也可以执行
    public String index(@RequestParam("name") String name, Map<String, Object> map) {
        System.out.println("index 请求处理中.....");
        System.out.println(name);
        System.out.println(map);
        // WEB-INF/pages/(index).jsp
        return "index";
    }

    @RequestMapping(value = "/info/{name}/{age}")
    @ResponseBody
    public String info(@PathVariable("name") String name, @PathVariable("age") String age, Map<String, Object> map) {
        System.out.println("info 请求处理中.....");
        System.out.println(name);
        System.out.println(age);
        System.out.println(map);
        // WEB-INF/pages/(index).jsp
        return "success";
    }


    @RequestMapping("/findAll")
    public ModelAndView findAll() {
        List<Account> accountList = accountService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("accountList", accountList);
        modelAndView.setViewName("accountList");
        return modelAndView;
    }
}
 
