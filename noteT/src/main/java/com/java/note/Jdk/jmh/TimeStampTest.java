package com.java.note.Jdk.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * @author : kebukeYi
 * @date :  2021-12-19 00:00
 * @description: 低开销获取时间戳
 * @question:
 * @link: https://mp.weixin.qq.com/s/hs11xUNF7Vh-zO6zeaXXmw
 **/
@State(Scope.Benchmark)
public class TimeStampTest {


    private static final int MAX = 10000000;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TimeStampTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(1)
                .warmupTime(TimeValue.seconds(5))
                .measurementTime(TimeValue.seconds(5))
                .mode(Mode.AverageTime)
                .syncIterations(false)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    @Threads(1)
    public void test1Thread() {
        for (int i = 0; i < MAX; i++) {
            currentTimeMillis();
        }
    }

    @Benchmark
    @Threads(2)
    public void test2Thread() {
        for (int i = 0; i < MAX; i++) {
            currentTimeMillis();
        }
    }

    @Benchmark
    @Threads(4)
    public void test4Thread() {
        for (int i = 0; i < MAX; i++) {
            currentTimeMillis();
        }
    }

    @Benchmark
    @Threads(8)
    public void test8Thread() {
        for (int i = 0; i < MAX; i++) {
            currentTimeMillis();
        }
    }

    @Benchmark
    @Threads(16)
    public void test16Thread() {
        for (int i = 0; i < MAX; i++) {
            currentTimeMillis();
        }
    }

    @Benchmark
    @Threads(32)
    public void test32Thread() {
        for (int i = 0; i < MAX; i++) {
            currentTimeMillis();
        }
    }

    @Benchmark
    @Threads(64)
    public void test64Thread() {
        for (int i = 0; i < MAX; i++) {
            currentTimeMillis();
        }
    }

    @Benchmark
    @Threads(128)
    public void test128Thread() {
        for (int i = 0; i < MAX; i++) {
            currentTimeMillis();
        }
    }

    private static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

}
 
