package com.yjs.mulu;

import com.alibaba.fastjson.JSON;
import com.yjs.bean.Base;
import com.yjs.bean.HostUnit;
import com.yjs.bean.Request;
import com.yjs.mapper.BaseMapper;
import com.yjs.mapper.HostUnitMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : kebukeYi
 * @date :  2021-10-04 21:59
 * @description: 研究生科目爬虫项目
 * @question:
 * @link:
 **/
public class CityDomain {

    public static String url = "https://yz.chsi.com.cn";
    public static String path = "/zsml";
    public static String action = "/queryAction.do";

    //城市
    public static Map<String, String> SSS = new HashMap<>();

    //门类类别
    public static Map<String, String> MIS = new HashMap<>();

    //key 门科类别  value 学科类别
    public static Map<String, List<Base>> map = new HashMap<>();

    //key 学科类别	 value 专业名称
    public static Map<String, List<String>> map2 = new HashMap<>();

    //学科类别
    public static Map<String, String> ZYS = new HashMap<>();

    //专硕的学科类别
    public static Map<String, String> ZYSs = new HashMap<>();

    //专业名称
    public static Map<String, String> ZY = new HashMap<>();

    //全日制 非全日制
    public static List<String> style = new ArrayList<String>() {{
        add("1");
        add("2");
    }};


    public static List<Base> getCy(String url) {
        String s = HttpClientUtil.sendHttpGet(url);
        final List<Base> cities = JSON.parseArray(s, Base.class);
        SSS = cities.stream().collect(Collectors.toMap(Base::getDm, Base::getMc));
        return cities;
    }

    public static List<Base> getMIS(String url) {
        String s = HttpClientUtil.sendHttpGet(url);
        final List<Base> cities = JSON.parseArray(s, Base.class);
        MIS = cities.stream().collect(Collectors.toMap(Base::getDm, Base::getMc));
        return cities;
    }

    public static List<Base> getZYS(String url) {
        String s = HttpClientUtil.sendHttpGet(url);
        final List<Base> cities = JSON.parseArray(s, Base.class);
        ZYS = cities.stream().collect(Collectors.toMap(Base::getDm, Base::getMc));
        return cities;
    }

    public static List<String> getZYStringArray(String url) {
        String s = HttpClientUtil.sendHttpGet(url);
        final List<String> cities = JSON.parseArray(s, String.class);
        return cities;
    }


    public static List<Base> getZYSs(String url, String parmer) {
        String s = HttpClientUtil.sendHttpGet(url + parmer);
        final List<Base> cities = JSON.parseArray(s, Base.class);
        ZYSs = cities.stream().collect(Collectors.toMap(Base::getDm, Base::getMc));
        return cities;
    }

    public static void setMIS() {
        final List<Base> mis = getMIS("https://yz.chsi.com.cn/zsml/pages/getMl.jsp");
        String url = "https://yz.chsi.com.cn/zsml/pages/getZy.jsp?mldm=";
        String zyUrl = "https://yz.chsi.com.cn/zsml/code/zy.do?q=";
        String zyxw = "zyxw";
        //门类大课
        mis.add(new Base("专业学位", zyxw));
        for (int i = 0; i < mis.size(); i++) {
            final Base base = mis.get(i);
            final List<Base> zys1 = getZYS(url + base.getDm());
            map.put(base.getDm(), zys1);
            for (int i1 = 0; i1 < zys1.size(); i1++) {
                final Base base1 = zys1.get(i1);
                final List<String> zys2 = getZYStringArray(zyUrl + base1.getDm());
                map2.put(base1.getDm(), zys2);
            }
        }
        System.out.println(map);
        final List<Base> zyxw1 = map.get("zyxw");
        System.out.println(map.get("zyxw"));
        final List<String> strings = map2.get(zyxw1.get(0).getDm());
        System.out.println(strings);
    }

    public static void getData() throws InterruptedException {
        String MIS = "https://yz.chsi.com.cn/zsml/pages/getMl.jsp";
        final List<Base> mis = getMIS(MIS);
        mis.add(new Base("专业学位", "zyxw"));
        String city = "https://yz.chsi.com.cn/zsml/pages/getSs.jsp";
        final List<Base> cys = getCy(city);
        //北京市
        for (Base cy : cys) {
            //01 哲学
            for (Base mi : mis) {
                //0101 哲学
                final List<Base> xklbs = map.get(mi.getDm());
                //以往万一
                if (xklbs == null || xklbs.size() == 0) continue;
                //竟然存在 (0784) 这样的数据
                for (Base xklb : xklbs) {
                    //专业名称  竟然可能 专业为空
                    final List<String> zymcs = map2.get(xklb.getDm());
                    if (zymcs == null || zymcs.size() == 0) continue;
                    for (String zymc : zymcs) {
                        //全日制 非全日制
                        for (String type : style) {
                            Request request = new Request(url, path, action, cy.getDm(), mi.getDm(), xklb.getDm(), zymc, type);
                            Thread.sleep(100);
                            HttpClientUtil.run(request);
                        }
                    }
                }
            }
        }
    }

    public static void init() throws IOException {
        String SSS = "https://yz.chsi.com.cn/zsml/pages/getSs.jsp";
        final List<Base> cy = getCy(SSS);

        //31
        // System.out.println(cy.size());

        String MIS = "https://yz.chsi.com.cn/zsml/pages/getMl.jsp";
        final List<Base> mis = getMIS(MIS);

        //14
        //System.out.println(mis.size());

        String ZYS = "https://yz.chsi.com.cn/zsml/pages/getZy.jsp";
        final List<Base> zys = getZYS(ZYS);

        //189
        //System.out.println(zys.size());

        String ZYSs = "https://yz.chsi.com.cn/zsml/pages/getZy.jsp";
        String parmer = "?mldm=zyxw";
        final List<Base> zySs = getZYSs(ZYSs, parmer);
        //47
        //System.out.println(zySs.size());
    }

    public static void initDB() throws IOException {
        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //当我们单独使用Mybatis时，需要创建一个SqlSessionFactory，然而当MyBatis和Spring整合时，却需要一个SqlSessionFactoryBean, 细节 之前想过吗???
        //怎么实现的mapper 代理对象的???
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        System.out.println(session);
        try {
            //第一种方法
            //User user = session.selectOne("com.java.note.mapper.UserMapper.getUserByAge", 19);
            //System.out.println(user);
            //第二种
            //获取mapper接口的代理对象
            //这样我们写的每一个Mapper接口都会对应一个MapperFactoryBean，每一个MapperFactoryBean的getObject()方法最终会采用JDK动态代理创建一个对象，
            // 所以每一个Mapper接口最后都对应一个代理对象，这样就实现了Spring和MyBatis的整合
            BaseMapper baseMapper = session.getMapper(BaseMapper.class);

            String SSS = "https://yz.chsi.com.cn/zsml/pages/getSs.jsp";
            final List<Base> cy = getCy(SSS);
            baseMapper.insertBatchSSSBase(cy);
            //31
            // System.out.println(cy.size());

            String MIS = "https://yz.chsi.com.cn/zsml/pages/getMl.jsp";
            final List<Base> mis = getMIS(MIS);
            baseMapper.insertBatchMISBase(mis);
            //14
            //System.out.println(mis.size());

            String ZYS = "https://yz.chsi.com.cn/zsml/pages/getZy.jsp";
            final List<Base> zys = getZYS(ZYS);
            baseMapper.insertBatchZYSBase(zys);
            //189
            //System.out.println(zys.size());

            String ZYSs = "https://yz.chsi.com.cn/zsml/pages/getZy.jsp";
            String parmer = "?mldm=zyxw";
            final List<Base> zySs = getZYSs(ZYSs, parmer);
            baseMapper.insertBatchZYSSBase(zySs);
            //47
            //System.out.println(zySs.size());
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }
    }

    public static void testHostUnit() throws IOException, SQLException {
        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //当我们单独使用Mybatis时，需要创建一个SqlSessionFactory，然而当MyBatis和Spring整合时，却需要一个SqlSessionFactoryBean, 细节 之前想过吗???
        //怎么实现的mapper 代理对象的???
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        session.getConnection().setAutoCommit(true);
        System.out.println(session);
        try {
            //第一种方法
            //User user = session.selectOne("com.java.note.mapper.UserMapper.getUserByAge", 19);
            //System.out.println(user);
            //第二种
            //获取mapper接口的代理对象
            //这样我们写的每一个Mapper接口都会对应一个MapperFactoryBean，每一个MapperFactoryBean的getObject()方法最终会采用JDK动态代理创建一个对象，
            // 所以每一个Mapper接口最后都对应一个代理对象，这样就实现了Spring和MyBatis的整合
            HostUnitMapper hostUnitMapper = session.getMapper(HostUnitMapper.class);
            final ArrayList<HostUnit> hostUnits = new ArrayList<>();
            hostUnits.add(new HostUnit("北京市", "10001", 10001L, "清华大学"));
            hostUnits.add(new HostUnit("北京市", "10002", 10002L, "北京大学"));
            hostUnits.add(new HostUnit("北京市", "10001", 10001L, "清华大学"));
            hostUnitMapper.insertBatchHostUnitBase(hostUnits);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
//        testHostUnit();
        setMIS();
        final List<String> string0 = map2.get("0782");
        final List<String> strings1 = map2.get("0781");
        final List<String> strings2 = map2.get("0783");
        final List<String> strings3 = map2.get("0784");
        getData();
        Thread.sleep(20000);
        HtmlToDocument.service1.shutdown();
        HtmlToDocument.service.shutdown();
        System.out.println("over");
    }
}
