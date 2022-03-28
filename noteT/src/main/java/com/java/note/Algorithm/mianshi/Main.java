package com.java.note.Algorithm.mianshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/21  下午 5:19
 * @Description
 */
public class Main {


    int p;

    public Main(int arg) {
        this.p = arg;
    }


    public static void main(String[] args) throws InterruptedException {
//        System.out.print(method(0));
//        mainp();
//        mainf();
//        maink();
//        GetVal();
//        maing();
//        System.out.println(calc(10));
//        gg();
//        System.out.println(getValue2(2));
//        ff();
//        get();
//        pringd();
        getAllData();


    }

    private static Integer method(Integer i) {
        try {
            if (i++ > 0)
                throw new IOException();

            System.out.println("0" + i);
            return i++;
        } catch (IOException e) {
            System.out.println("1" + i);
            i++;
            return i++;
        } catch (Exception e) {
            System.out.println("2" + i);
            i++;
            return i++;
        } finally {
            System.out.println("3" + i);
            return i++;
        }
    }

    private static volatile int s = 0;
    private static final ThreadPoolExecutor async = new ThreadPoolExecutor(
            0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new SynchronousQueue<>());

    public static void mainp() throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            async.execute(() -> s++);
        }
        Thread.sleep(5000L);
        System.out.println(s);
        async.shutdown();
    }


    public static void mainf() {
        System.out.println(" 没人比我更懂java".toCharArray().length);
    }

    public static void maink() {
        Map<String, Object> map = new HashMap<>();
        String str = "没人比我更懂java";
        StrObject obj = new StrObject("没人比我更懂java");
        map.put("str", str);
        map.put("obj", obj);

        str = "真的没人比我更懂java";
        System.out.printf(map.get("str").toString() + "; ");

        StrObject new_obj = (StrObject) map.get("obj");
        new_obj.setStr("真的没人比我更懂java");
        System.out.printf(map.get("obj").toString() + "; ");
    }

    static class StrObject {
        String str;

        public StrObject(String str) {
            this.str = str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
    }

    class Inner {
        public String v1 = "Fake News";
        public String v2 = "Go ahead";
    }

    private static String GetVal() {
        try {
            return Inner.class.newInstance().v1;
        } catch (Exception e) {
            try {
                return Inner.class.getDeclaredConstructor(Main.class).newInstance((Main) null).v2;
            } catch (Exception ee) {
                ee.printStackTrace();
                return "Fake News, Go ahead";
            }
        }
    }

    public static void maing() {
        Thread t = new Thread() {
            @Override
            public void run() {
                cnn();
            }
        };
        t.run();
        System.out.print("FakeNews ");
        System.out.print("; ");
        t.start();
        System.out.print("FakeNews ");
    }

    static void cnn() {
        System.out.print("CNN ");
    }

    public static int calc(int n) {
        try {
            n += 1;
            if (n / 0 > 0) {
                n += 1;
            } else {
                n -= 10;
            }
            return n;
        } catch (Exception e) {
            System.out.println("c " + n);
            n++;
        }
        n++;
        System.out.println(n);
        return n++;
    }

    public static void gg() {
        String s = new String(new char[]{'没', '人', '比', '我', '更', '懂', 'j', 'a', 'v', 'a'});
        String si = "没人比我更懂java";
        System.out.println(s == si);

        System.out.println(s.intern() == "没人比我更懂java");
        System.out.println(s == si.intern());
    }


    public static int getValue2(int i) {
        int result = 0;
        switch (i) {
            case 1:
                result = result + i;
            case 2:
                result = result + i * 2;
            case 3:
                result = result + i * 3;
        }
        return result;
    }


    public static void ff() {
        int i = 1;
        int j = i++;
        if ((j > ++j) && (i++ == j)) {
            j += i;
        }
        System.out.println(j);
    }

    public static void get() {
        int i = 9;
        switch (i) {
            default:
                System.out.println("default");
                break;
            case 0:
                System.out.println("zero");
                break;
            case 1:
                System.out.println("one");
                break;
            case 2:
                System.out.println("two");
                break;
        }
    }


    public static void pringd() {
        System.out.println(2 + 2 + "5" + 2 + 2);
    }

    static class ScoreVo {
        String studentName;
        String courseName;
        double score;

        public ScoreVo(String studentName, String courseName, double score) {
            this.studentName = studentName;
            this.courseName = courseName;
            this.score = score;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }
    @NoArgsConstructor
    static class StudentVo {
        private String name;
        private double chineseScore;
        private double mathScore;
        private double englishScore;
        private double physicalScore;
        private double totalScore;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getChineseScore() {
            return chineseScore;
        }

        public void setChineseScore(double chineseScore) {
            this.chineseScore = chineseScore;
        }

        public double getMathScore() {
            return mathScore;
        }

        public void setMathScore(double mathScore) {
            this.mathScore = mathScore;
        }

        public double getEnglishScore() {
            return englishScore;
        }

        public void setEnglishScore(double englishScore) {
            this.englishScore = englishScore;
        }

        public double getPhysicalScore() {
            return physicalScore;
        }

        public void setPhysicalScore(double physicalScore) {
            this.physicalScore = physicalScore;
        }

        public double getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(double totalScore) {
            this.totalScore = totalScore;
        }

        public StudentVo(String name, double chineseScore, double mathScore, double englishScore, double physicalScore, double totalScore) {
            this.name = name;
            this.chineseScore = chineseScore;
            this.mathScore = mathScore;
            this.englishScore = englishScore;
            this.physicalScore = physicalScore;
            this.totalScore = totalScore;
        }
    }

    public static List<ScoreVo> getScoreList() {
        List<ScoreVo> scoreList = new ArrayList<ScoreVo>();
        scoreList.add(new ScoreVo("张三", "语文", 80));
        scoreList.add(new ScoreVo("张三", "物理", 76));
        scoreList.add(new ScoreVo("李四", "语文", 78));
        scoreList.add(new ScoreVo("茅十八", "英语", 65));
        scoreList.add(new ScoreVo("李四", "数学", 88));
        scoreList.add(new ScoreVo("李四", "物理", 87));
        scoreList.add(new ScoreVo("王五", "语文", 67));
        scoreList.add(new ScoreVo("张三", "数学", 76));
        scoreList.add(new ScoreVo("李四", "英语", 89));
        scoreList.add(new ScoreVo("王五", "数学", 65));
        scoreList.add(new ScoreVo("赵六", "物理", 95));
        scoreList.add(new ScoreVo("王五", "英语", 78));
        scoreList.add(new ScoreVo("王五", "物理", 65));
        scoreList.add(new ScoreVo("赵六", "语文", 89));
        scoreList.add(new ScoreVo("赵六", "英语", 87));
        scoreList.add(new ScoreVo("黄七", "语文", 78));
        scoreList.add(new ScoreVo("黄七", "数学", 65));
        scoreList.add(new ScoreVo("刘八", "英语", 87));
        scoreList.add(new ScoreVo("张三", "英语", 56));
        scoreList.add(new ScoreVo("黄七", "物理", 76));
        scoreList.add(new ScoreVo("刘八", "数学", 89));
        scoreList.add(new ScoreVo("黄七", "英语", 98));
        scoreList.add(new ScoreVo("刘八", "语文", 56));
        scoreList.add(new ScoreVo("刘八", "物理", 76));
        scoreList.add(new ScoreVo("钱九", "语文", 88));
        scoreList.add(new ScoreVo("钱九", "数学", 67));
        scoreList.add(new ScoreVo("茅十八", "数学", 43));
        scoreList.add(new ScoreVo("钱九", "英语", 75));
        scoreList.add(new ScoreVo("茅十八", "语文", 45));
        scoreList.add(new ScoreVo("茅十八", "物理", 56));

        return scoreList;
    }

    public static void getAllData() {
        ArrayList<String> catagloeList = new ArrayList<String>() {{
            add("数学");
            add("语文");
            add("英语");
            add("物理");
            add("总成绩");
        }};

        List<ScoreVo> scoreList = getScoreList();

        Map<String, List<ScoreVo>> listMap = scoreList.stream().collect(Collectors.groupingBy(ScoreVo::getStudentName));

        int count = listMap.size();

        //每科总分
        double sumMath = 0;
        double sumYu = 0;
        double sumEnglish = 0;
        double sumPhy = 0;

        //每科的总分 平均分
        double sumScores = 0;

        //默认各科都是全员考试
        double[] courseArray = new double[]{
                count, count, count, count
        };

        //每科平均成绩
        double aveMath = 0;
        double aveYu = 0;
        double aveEnglish = 0;
        double avePhy = 0;
        double aveSum = 0;

        //默认 key 升序 排列
        //保存升序
        Map<Integer, String> sumScoreAndNameMap = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return b - a;
            }
        });

        //对应 张三   【111  51  61  32】
        Map<String, double[]> nameAndScoreMap = new HashMap<>();


        for (Map.Entry<String, List<ScoreVo>> entry : listMap.entrySet()) {

            //每个人的总成绩
            int sumScore = 0;

            // 按照打印顺序存储 数学 语文 英语 物理 总成绩
            double[] scoresArray = new double[5];

            //遍历 每个人的成绩
            for (ScoreVo scoreVo : entry.getValue()) {

                sumScore += scoreVo.getScore();

                sumScores += scoreVo.getScore();

                //如果是数学
                if (scoreVo.getCourseName().equals(catagloeList.get(0))) {
                    //学科数学总成绩
                    sumMath += scoreVo.getScore();
                    //个人要打印的  数学 成绩 存储
                    scoresArray[0] = scoreVo.getScore();


                } else if (scoreVo.getCourseName().equals(catagloeList.get(1))) {
                    sumYu += scoreVo.getScore();
                    scoresArray[1] = scoreVo.getScore();

                } else if (scoreVo.getCourseName().equals(catagloeList.get(2))) {
                    sumEnglish += scoreVo.getScore();
                    scoresArray[2] = scoreVo.getScore();

                } else if (scoreVo.getCourseName().equals(catagloeList.get(3))) {
                    sumPhy += scoreVo.getScore();
                    scoresArray[3] = scoreVo.getScore();
                }

            }


            //每个人的总成绩
            scoresArray[4] = sumScore;

            //存放 439 == 张三
            //存放 339 ==李四
            sumScoreAndNameMap.put(sumScore, entry.getKey());

            //存放 张三 【数学 110  语文 90  英语30 物理 55 总成绩 123】
            nameAndScoreMap.put(entry.getKey(), scoresArray);
        }


//        System.out.println(sumMath + " " + sumYu + " " + sumEnglish + " " + sumPhy);
        System.out.println("姓名" + "    " + "数学" + "   " + "语文" + "  " + "英语" + " " + "物理 " + "总成绩");


        for (Map.Entry<Integer, String> entry : sumScoreAndNameMap.entrySet()) {
            String nameKey = entry.getValue();
            double[] sortScoreList = nameAndScoreMap.get(nameKey);
            System.out.print(nameKey + "    ");

            for (int i = 0; i < sortScoreList.length; i++) {
                if (sortScoreList[i] == 0) {
                    courseArray[i]--;
                }
                System.out.print(sortScoreList[i] + "  ");
            }
            System.out.println();
        }

        aveMath = sumMath / courseArray[0];
        aveYu = sumYu / courseArray[1];
        aveEnglish = sumEnglish / courseArray[2];
        avePhy = sumPhy / courseArray[3];

        aveSum = sumScores / count;

        System.out.println("平均成绩:" + aveMath + "  " + aveYu + " " + aveEnglish + "  " + avePhy + "  " + aveSum);
    }

}
