package com.java.note.Jvm.joonwhee.demo;

import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.ReflectorFactory;

/**
 * 本例的运行JVM参数：
 * -XX:MetaspaceSize=16M -XX:MaxMetaspaceSize=55M -Xms2g -Xmx2g -Xss128k -XX:+UseConcMarkSweepGC
 * -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+PrintGCTimeStamps
 * -XX:+PrintGCDetails -XX:+PrintHeapAtGC -XX:+PrintTenuringDistribution -Dsun.reflect.inflationThreshold=0
 * -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:/dump/java_reflect_heapdump.hprof
 * <p>
 * Metadata GC Threshold：metaspace空间不能满足分配时触发的FGC，这个阶段不会清理软引用
 * Last ditch collection：FGC后还是不能满足条件，这个时候会触发再一次Last ditch collection的full gc，这次full gc会清理掉软引用。
 */
public class ReflectTest {

    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws Exception {
        ReflectorFactory reflectorFactory = new DefaultReflectorFactory();

        System.out.println("load class start");
        // model有700个方法
        Reflector reflector1 = reflectorFactory.findForClass(TestModel.class);
        Reflector reflector2 = reflectorFactory.findForClass(TestModel2.class);
        Reflector reflector3 = reflectorFactory.findForClass(TestModel3.class);
        System.out.println("load class end");

        // 新建3个model，每个model有700个方法
        TestModel testModel = new TestModel();
        TestModel2 testModel2 = new TestModel2();
        TestModel3 testModel3 = new TestModel3();

        // get方法和set方法的参数
        Object[] getArgs = {};
        Object[] setArgs = {"a"};

        System.out.println("method invoke start");
        // 700个属性
        for (int i = 1; i <= 700; i++) {
            // 3个类6个方法，每个方法会生成一个单独的类加载器
            reflector1.getSetInvoker("field" + i).invoke(testModel, setArgs);
            reflector1.getGetInvoker("field" + i).invoke(testModel, getArgs);
            reflector2.getSetInvoker("field" + i).invoke(testModel2, setArgs);
            reflector2.getGetInvoker("field" + i).invoke(testModel2, getArgs);
            reflector3.getSetInvoker("field" + i).invoke(testModel3, setArgs);
            reflector3.getGetInvoker("field" + i).invoke(testModel3, getArgs);
        }
        System.out.println("method invoke end");
        System.in.read();
    }
}
