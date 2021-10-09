package com.yjs.mulu;

import com.yjs.bean.HostUnit;
import com.yjs.bean.Request;
import com.yjs.bean.TestRange;
import com.yjs.bean.UnitCollege;
import com.yjs.dao.DBDao;
import lombok.var;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : kebukeYi
 * @date :  2021-10-04 22:35
 * @description:
 * @question:
 * @link:
 **/
public class HtmlToDocument {

    public static DBDao dbDao = new DBDao();
    static ExecutorService service = Executors.newFixedThreadPool(1);
    static ExecutorService service1 = Executors.newFixedThreadPool(1);
    public static AtomicInteger college_id = new AtomicInteger(1);

    //           <thead>
    //                    <tr>
    //                        <th>招生单位</th>
    //                        <th>所在地</th>
    //                        <th class="ch-table-center     ">研究生院</th>
    //                        <th class="ch-table-center">自划线院校</th>
    //                        <th class="ch-table-center">博士点</th>
    //                    </tr>
    //                    </thead>

    //<tbody>
// <tr>
//  <td>
//   <form name="form3" method="post" id="form3" action="/zsml/queryAction.do">
//    <a href="/zsml/querySchAction.do?ssdm=11&amp;dwmc=%E4%B8%AD%E5%9B%BD%E4%BA%BA%E6%B0%91%E5%A4%A7%E5%AD%A6&amp;mldm=02&amp;mlmc=&amp;yjxkdm=0201&amp;xxfs=1&amp;zymc=%E5%8F%91%E5%B1%95%E7%BB%8F%E6%B5%8E%E5%AD%A6" target="_blank">(10002)中国人民大学</a>
//   </form> </td>
//  <td>(11)北京市</td>
//  <td class="ch-table-center"><i class="iconfont ch-table-tick"></i></td>
//  <td class="ch-table-center"><i class="iconfont ch-table-tick"></i></td>
//  <td class="ch-table-center"><i class="iconfont ch-table-tick"></i></td>
// </tr>
// <tr>
//  <td>
//   <form name="form3" method="post" id="form3" action="/zsml/queryAction.do">
//    <a href="/zsml/querySchAction.do?ssdm=11&amp;dwmc=%E4%B8%AD%E5%9B%BD%E7%A4%BE%E4%BC%9A%E7%A7%91%E5%AD%A6%E9%99%A2%E5%A4%A7%E5%AD%A6&amp;mldm=02&amp;mlmc=&amp;yjxkdm=0201&amp;xxfs=1&amp;zymc=%E5%8F%91%E5%B1%95%E7%BB%8F%E6%B5%8E%E5%AD%A6" target="_blank">(14596)中国社会科学院大学</a>
//   </form> </td>
//  <td>(11)北京市</td>
//  <td class="ch-table-center"> </td>
//  <td class="ch-table-center"> </td>
//  <td class="ch-table-center"><i class="iconfont ch-table-tick"></i></td>
// </tr>
//</tbody>
    public static void schoolsParse(String url, String xxfs, String html, Request request) {
        if (html == null) return;
        String u = url;//基本 url 不能变
        Document document = Jsoup.parse(html);
        //像js一样，通过标签获取 tbody
        final Element tbody = document.getElementsByTag("tbody").first();
        final ArrayList<HostUnit> hostUnits = new ArrayList<>();
        if (tbody == null) return;
        //多个学校
        final Elements trs = tbody.getElementsByTag("tr");
        for (Element tr : trs) {
            System.out.println("=============school start===============");
            HostUnit hostUnit = new HostUnit();
            final Elements tds = tr.getElementsByTag("td");
            //单个学校
            for (int i = 0; i < tds.size(); i++) {
                final Element td = tds.get(i);
                final Element form = td.getElementsByTag("form").first();
                //1. 有学校结果
                //2. 没有结果
                if (form != null) {
                    final Element a = form.getElementsByTag("a").first();
                    if (a == null) return;
                    final String html1 = a.html();
                    final int i1 = html1.indexOf("(");
                    // (80000)中共中央
                    final int i2 = html1.indexOf(")");//忽略了 这种情况 (80000)中共中央党校(国家行政学院)
                    //学校单位代码
                    String code = html1.substring(i1 + 1, i2);
                    //单位名称
                    hostUnit.setUnit_id(Long.parseLong(code));
                    hostUnit.setUnit_code(code);
                    hostUnit.setUnit_name(a.html());
                    String href = a.attr("href");
                    String finalUrl = url + href;//马虎之处 : 造成 url  每次追加了
                    //直接线程池去查询院校
                    try {
                        service.execute(() -> {
                            //有可能 httpGet 为空
                            String httpGet = HttpClientUtil.sendHttpGet(finalUrl);
                            //处理院校相关研究方向
                            collegeParse(u, code, xxfs, httpGet, request);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                if (td.getElementsByTag("i").first() != null) {
                    if (i == 2) hostUnit.setGraduate_school(1);
                    if (i == 3) hostUnit.setSelf_marking(1);
                    if (i == 4) hostUnit.setDoctoral_degree(1);
                    continue;
                }
                System.out.println("学校列表td：" + td.html());
                //单位 所在地
                if (i == 1) hostUnit.setUnit_address(td.html());
            }
            if (hostUnit.getUnit_id() != null) hostUnits.add(hostUnit);//容易忽略  招生单位  很抱歉，没有找到您要搜索的数据！ 这种情况
            System.out.println("=============school end================");
        }
        System.out.println(hostUnits.size());
        if (hostUnits.size() > 0)
            dbDao.getHostUnitMapper().insertBatchHostUnitBase(hostUnits);// 忽略了 这个  很抱歉，没有找到您要搜索的数据！
    }

    //<tbody>
//                    <tr>
//                        <td class="ch-table-center">统考</td>
//                        <td>(002)经济学院</td>
//                        <td>(0201Z3)发展经济学</td>
//                        <td>(02)人力资本与收入分配理论</td>
//                        <td class="ch-table-center">全日制</td>
//                        <td class="ch-table-center">
//                            <!--  -->
//                            <div style=" width: 100px; overflow:hidden;  white-space: nowrap; text-overflow: ellipsis; ">
//                                <span class="js-from-title" data-title=''></span>
//                            </div>
//                        </td>
//                        <td class="ch-table-center">
//                            <script language="javascript">
//                                document.write(cutString('专业：1(不含推免)', 6));
//                            </script>
//                        </td>
//                        <td class="ch-table-center"><a href="/zsml/kskm.jsp?id=14596210020201Z3021"
//                                                       target="_blank">查看</a></td>
//                        <td class="ch-table-center">
//                            <script language="javascript">
//                                document.write(cutString('', 6));
//                            </script>
//                        </td>
//                    </tr>

//                    <tr>
//                        <td class="ch-table-center">统考</td>
//                        <td>(002)经济学院</td>
//                        <td>(0201Z3)发展经济学</td>
//                        <td>(04)发展经济学</td>
//                        <td class="ch-table-center">全日制</td>
//                        <td class="ch-table-center">
//                            <!-- 指导教师  不区分导师-->
//                            <div style=" width: 100px; overflow:hidden;  white-space: nowrap; text-overflow: ellipsis; ">
//                                <span class="js-from-title" data-title=''></span>
//                            </div>
//                        </td>
//                        <td class="ch-table-center">
//                            <script language="javascript">
//                                document.write(cutString('专业：1(不含推免)', 6));
//                            </script>
//                        </td>
//                        <td class="ch-table-center"><a href="/zsml/kskm.jsp?id=14596210020201Z3041" target="_blank">查看</a>
//                        </td>
//                        <td class="ch-table-center">
//                            <script language="javascript">
//                                document.write(cutString('', 6));
//                            </script>
//                        </td>
//                    </tr>
//                    <tr>
//                        <td class="ch-table-center">统考</td>
//                        <td>(002)经济学院</td>
//                        <td>(0201Z3)发展经济学</td>
//                        <td>(05)公共经济学</td>
//                        <td class="ch-table-center">全日制</td>
//                        <td class="ch-table-center">
//                            <!--  不区分导师-->
//                            <div style=" width: 100px; overflow:hidden;  white-space: nowrap; text-overflow: ellipsis; ">
//                                <span class="js-from-title" data-title=''></span>
//                            </div>
//                        </td>
//                        <td class="ch-table-center">
//                            <script language="javascript">
//                                document.write(cutString('专业：1(不含推免)', 6));
//                            </script>
//                        </td>
//                        <td class="ch-table-center"><a href="/zsml/kskm.jsp?id=14596210020201Z3051"target="_blank">查看</a></td>
//                        <td class="ch-table-center">
//                            <script language="javascript">
//                                document.write(cutString('', 6));
//                            </script>
//                        </td>
//                    </tr>
//                    </tbody>

    //处理院校相关研究方向
    public static void collegeParse(String url, String parentCode, String xxfs, String html, Request request) {
        final ArrayList<String> titles = new ArrayList<>();
        if (html == null) return;
        Document document = Jsoup.parse(html);
        //像js一样，通过标签获取 tbody
        final Element tbody = document.getElementsByTag("tbody").first();
        final ArrayList<UnitCollege> unitColleges = new ArrayList<>();
        if (tbody == null) return;
        final Elements trs = tbody.getElementsByTag("tr");
        //多个学院
        for (Element tr : trs) {
            System.out.println("---------------------college start-----------------------------");
            UnitCollege unitCollege = new UnitCollege();
            //学院 id 自增  可用 redis 实现自增 id
            unitCollege.setCollege_id((long) college_id.getAndIncrement());
            unitCollege.setUnit_id(Long.parseLong(parentCode));
            unitCollege.setLearning_style(xxfs);
            unitCollege.setMllb(request.getMldm());
            unitCollege.setXklb(request.getYjxkdm());
            //单个学院
            final Elements tds = tr.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i++) {
                final Element td = tds.get(i);
                final String s1 = td.html();
                //考试方式 统考
                if (i == 0) {
                    unitCollege.setCollege_type(s1);
                    continue;
                }
                //学院院所名称  (002)经济学院
                if (i == 1) {
                    final int i1 = s1.indexOf("(");
                    final int i2 = s1.indexOf(")");
                    unitCollege.setCollege_name(s1);
                    unitCollege.setCollege_code(s1.substring(i1 + 1, i2));
                    continue;
                }
                //专业 (0201Z3)发展经济学
                if (i == 2) {
                    final int i1 = s1.indexOf("(");
                    final int i2 = s1.indexOf(")");
                    final int i3 = s1.length();
                    unitCollege.setMajor_name(s1.substring(i2 + 1, i3));//发展经济学
                    unitCollege.setMajor_code(s1.substring(i1 + 1, i2));//0201Z3
                    continue;
                }
                //研究方向  (02)人力资本与收入分配理论
                if (i == 3) {
                    unitCollege.setResearch_direction(s1);
                    continue;
                }
                //获取指导教师
                final Element div = td.getElementsByTag("div").first();
                if (div != null) {
                    final Element span = div.getElementsByTag("span").first();
                    System.out.println(span.html());
                    unitCollege.setTutor(span.html());
                    continue;
                }

                //获取 专业 详情 招收人数
                final Element script = td.getElementsByTag("script").first();
                if (script != null) {
                    String subString = script.html();
                    int indexOf1 = subString.indexOf("'");
                    int lastIndexOf = subString.lastIndexOf("'");
                    subString = subString.substring(indexOf1 + 1, lastIndexOf);
                    System.out.println(subString);
                    if (i == 6) {
                        unitCollege.setProposed_enrollment(subString);
                    } else if (i == 8) {
                        unitCollege.setComment(subString);
                    }
                    continue;
                }

                //获取 查看考试范围 id
                final Element a = td.getElementsByTag("a").first();
                if (a != null) {
                    final String href = a.attr("href");
                    // https://yz.chsi.com.cn/zsml/kskm.jsp?id=10002211100201Z3021
                    String testRangeIdUrl = url + href;
                    System.out.println(" 考试范围id " + testRangeIdUrl);
                    final String[] split = href.split("=");
                    unitCollege.setTest_range_id(split[1]);
                    try {
                        service1.execute(() -> {
                            String html3 = HttpClientUtil.sendHttpGet(testRangeIdUrl);
                            //处理查看考试范围 详情
                            testRange(split[1], html3);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //学院理应都有数据的
            unitColleges.add(unitCollege);
            System.out.println("---------------------college end------------------------------");
        }
        dbDao.getUnitCollegeMapper().insertBatchUnitCollegeBase(unitColleges);
    }

    //查看考试范围 详情
    public static void testRange(String test_range_id, String html) {
        if (html == null) return;
        Document document = Jsoup.parse(html);
        int index = 0;
        //像js一样，通过标签获取 tbody
        final Elements tbodys = document.getElementsByTag("tbody");
        final ArrayList<TestRange> testRanges = new ArrayList<>();
        //当有 或 字时 直接掠过 0 2 4 有
        for (int i = 1; i < tbodys.size(); i = i + 2) {
            System.out.println("---------------------testRange start------------------------------");
            TestRange testRange = new TestRange();
            testRange.setTest_range_id(test_range_id);
            final var tbody = tbodys.get(i);
            final Element tr = tbody.getElementsByTag("tr").first();
            final Elements tds = tr.getElementsByTag("td");
            //思想品德 英语 ...
            for (Element td : tds) {
                final var major = td.text();
                if (index == 0) testRange.setMajor_1(major);
                if (index == 1) testRange.setMajor_2(major);
                if (index == 2) testRange.setSubobject_1(major);
                if (index == 3) testRange.setSubobject_2(major);
                index++;
            }
            index = 0;
            testRanges.add(testRange);
            System.out.println("---------------------testRange end------------------------------");
        }
        System.out.println(testRanges);
        dbDao.getTestRangeMapper().insertBatchTestRangeBase(testRanges);
    }

    //测试字符串切割
    public static void main(String[] args) {
        String ss = "document.write(cutString('专业：1(不含推免)',6));";
        System.out.println(ss.length());
        int indexOf = ss.indexOf("'");
        final int lastIndexOf1 = ss.lastIndexOf("'");
        System.out.println(indexOf);
        String substring = ss.substring(indexOf + 1, lastIndexOf1);
        System.out.println(substring);
        System.out.println("==================================");
        String sss = "document.write(cutString('（1）本方向不招收少数民族高层次骨干人才计划考生(2)该招生计划根据往年录取情况编制，因教育部暂未下达2022年总规模，实际招生人数会因国家下拨招生计划和实际报考人数等因素有所调整。',6));";
        int length = sss.length();
        int indexOf1 = sss.indexOf("'");
        System.out.println(indexOf1);
        final int lastIndexOf = sss.lastIndexOf("'");
        System.out.println(lastIndexOf);
        sss = sss.substring(indexOf + 1, lastIndexOf);
        System.out.println(sss);

        System.out.println("==================================");
        String ssss = "(0201Z3)发展经济学";
        final int i1 = ssss.indexOf("(");
        final int i2 = ssss.indexOf(")");
        final int i3 = ssss.length();
        final String substring1 = ssss.substring(i2 + 1, i3);//发展经济学
        final String substring2 = ssss.substring(i1 + 1, i2);//0201Z3

        System.out.println(substring1);
        System.out.println(substring2);
    }
}
 
