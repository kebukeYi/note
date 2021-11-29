package com.sharding.jdbc.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharding.jdbc.entity.Course;
import com.sharding.jdbc.entity.Dict;
import com.sharding.jdbc.entity.User;
import com.sharding.jdbc.mapper.CourseMapper;
import com.sharding.jdbc.mapper.DictMapper;
import com.sharding.jdbc.mapper.UserMapper;
import org.apache.shardingsphere.api.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class ShardingJdbcApplicationTests {

    @Resource
    CourseMapper courseMapper;
    @Resource
    DictMapper dictMapper;
    @Resource
    UserMapper userMapper;

    @Test
    public void addCourse() {
        //01 配置文件
        for (int i = 0; i < 10; i++) {
            Course c = new Course();
            //c.setCid(Long.valueOf(i));
            c.setCname("shardingsphere");
            c.setUserId(Long.valueOf("" + (1000 + i)));
            c.setCstatus("1");
            courseMapper.insert(c);
            //未有主键时
            //Logic SQL: INSERT INTO course  ( cname, user_id,cstatus )  VALUES  ( ?,?,? )
            //Actual SQL: m1 ::: INSERT INTO course_1  ( cname,user_id, cstatus , cid)  VALUES  (?, ?, ?, ?)
            //手动设置主键时
            //Logic SQL: INSERT INTO course  ( cname, user_id,cstatus )  VALUES  ( ?,?,?,?)
            //Actual SQL: m1 ::: INSERT INTO course_1  ( cname,user_id, cstatus , cid)  VALUES  (?, ?, ?, ?) ::: [0, shardingsphere, 1000, 1]
        }
    }

    @Test
    public void queryNullCourse() {
        //01 配置文件
        //select * from course
        //Logic SQL: SELECT  cid,cname,user_id,cstatus  FROM course
        //Actual SQL: m1 ::: SELECT  cid,cname,user_id,cstatus  FROM course_1
        //Actual SQL: m1 ::: SELECT  cid,cname,user_id,cstatus  FROM course_2
        //Actual SQL: m2 ::: SELECT  cid,cname,user_id,cstatus  FROM course_1
        //Actual SQL: m2 ::: SELECT  cid,cname,user_id,cstatus  FROM course_2
        final List<Course> courses = courseMapper.selectList(null);
        courses.forEach(System.out::println);
    }

    @Test
    public void queryCourse() {
        //02 配置文件 inline分片策略打开
        //select * from course
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("cid");
        wrapper.eq("cid", 672064099705688064L);
        //wrapper.in()
        //Logic SQL: SELECT  cid,cname,user_id,cstatus  FROM course
        //Actual SQL: m1 ::: SELECT  cid,cname,user_id,cstatus  FROM course_1
        //WHERE  cid = ? ORDER BY cid DESC ::: [672064099705688064]
        List<Course> courses = courseMapper.selectList(wrapper);
        courses.forEach(course -> System.out.println(course));
    }

    @Test
    public void queryOrderRange() {
        //02 配置文件  standard标准分片策略   关闭/打开
        //select * from course
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        //跨库 范围查询  结果是 失败
        //原因是 当前策略是不能获取 准确 cid 的 ，因为是范围，还要 根据 cid 来路由 库和表
        //Inline strategy cannot support this type sharding:RangeRouteValue(columnName=cid,
        //tableName=course, valueRange=[672064103556059136‥672064104604635137])
        //使用 stand 策略 后 就可以查询到了
        wrapper.between("cid", 672064103556059136L, 672064104604635137L);
        // wrapper.in()
        List<Course> courses = courseMapper.selectList(wrapper);
        courses.forEach(course -> System.out.println(course));
    }

    @Test
    public void queryCourseComplex() {
        //02 配置文件  复杂策略打开
        //复杂查询
        //Logic SQL: SELECT  cid,cname,user_id,cstatus  FROM course
        // WHERE  cid BETWEEN ? AND ? AND user_id = ?
        //Actual SQL: m2 ::: SELECT  cid,cname,user_id,cstatus  FROM course_2
        //WHERE  cid BETWEEN ? AND ? AND user_id = ? ::: [672064103556059136, 672064104604635137, 1009]
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.between("cid", 672064103556059136L, 672064104604635137L);
        wrapper.eq("user_id", 1009L);
        //wrapper.in()
        List<Course> courses = courseMapper.selectList(wrapper);
        courses.forEach(course -> System.out.println(course));
    }

    @Test
    public void queryCourseByHint() {
        //02 配置文件  强制路由策略打开
        HintManager hintManager = HintManager.getInstance();
        //直接 定死 分表路由
        hintManager.addTableShardingValue("course", 2);
        //直接 定死 分库路由
        // hintManager.addDatabaseShardingValue("coursedb",2);

        //Logic SQL: SELECT  cid,cname,user_id,cstatus  FROM course
        //Actual SQL: m1 ::: SELECT  cid,cname,user_id,cstatus  FROM course_2
        //Actual SQL: m2 ::: SELECT  cid,cname,user_id,cstatus  FROM course_2
        List<Course> courses = courseMapper.selectList(null);
        courses.forEach(course -> System.out.println(course));
        hintManager.close();
    }

    @Test
    public void addDict() {
        //03 配置文件
        // 广播表 每个库的每个表都是全量数据
        Dict d1 = new Dict();
        d1.setUstatus("1");
        d1.setUvalue("正常");
        //Logic SQL: INSERT INTO t_dict  ( ustatus, uvalue )  VALUES  ( ?, ? )
        //Actual SQL: m1 ::: INSERT INTO t_dict  ( ustatus,
        // uvalue , dict_id)  VALUES  (?, ?, ?) ::: [1, 正常, 672096116510031872]
        dictMapper.insert(d1);

        Dict d2 = new Dict();
        d2.setUstatus("0");
        d2.setUvalue("不正常");
        dictMapper.insert(d2);

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("user No " + i);
            user.setUstatus("" + (i % 2));
            user.setUage(i * 10);
            userMapper.insert(user);
        }
    }

    @Test
    public void queryUserStatus() {
        //Logic SQL: select u.user_id,u.username,d.uvalue ustatus from user u left join t_dict d on u.ustatus = d.ustatus

        //没有绑定内味儿
        //Actual SQL: m1 ::: select u.user_id,u.username,d.uvalue ustatus from t_user_2 u left join t_dict_1 d on u.ustatus = d.ustatus
        //Actual SQL: m1 ::: select u.user_id,u.username,d.uvalue ustatus from t_user_2 u left join t_dict_2 d on u.ustatus = d.ustatus
        //Actual SQL: m1 ::: select u.user_id,u.username,d.uvalue ustatus from t_user_1 u left join t_dict_1 d on u.ustatus = d.ustatus
        //Actual SQL: m1 ::: select u.user_id,u.username,d.uvalue ustatus from t_user_1 u left join t_dict_2 d on u.ustatus = d.ustatus
        //设置 ：spring.shardingsphere.sharding.binding-tables[0]=user,t_dict
        //有绑定内味了
        //Actual SQL: m1 ::: select u.user_id,u.username,d.uvalue ustatus from t_user_1 u left join t_dict_1 d on u.ustatus = d.ustatus
        //Actual SQL: m1 ::: select u.user_id,u.username,d.uvalue ustatus from t_user_2 u left join t_dict_2 d on u.ustatus = d.ustatus

        List<User> users = userMapper.queryUserStatus();
        users.forEach(user -> System.out.println(user));
    }

    @Test
    public void addDictByMS() {
        //04 配置文件 读写分离样例
        //写操作
        Dict d1 = new Dict();
        d1.setUstatus("1");
        d1.setUvalue("正常");
        dictMapper.insert(d1);

        Dict d2 = new Dict();
        d2.setUstatus("0");
        d2.setUvalue("不正常");
        dictMapper.insert(d2);
    }

    @Test
    public void queryDictByMS() {
        //04 配置文件 读写分离样例
        //读操作
        List<Dict> dicts = dictMapper.selectList(null);
        dicts.forEach(dict -> System.out.println(dict));
    }

}
