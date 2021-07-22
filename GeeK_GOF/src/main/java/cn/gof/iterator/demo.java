package cn.gof.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : kebukeYi
 * @date :  2021-07-21 19:05
 * @description:
 * @question:
 * @link:
 **/
public class demo {


    public static void main(String[] args) {
        List<Object> arrayList = new LinkedList<>();
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        arrayList.add("d");
        System.out.println(arrayList);
        Iterator<Object> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            //这里 会抛出异常
            String str = (String) iterator.next();
            if ("b".equals(str)) {
                //   arrayList.add("e");
                arrayList.remove(str);
                // System.out.println(arrayList);
//                iterator.remove();
//                iterator.next();
//                iterator.remove();
            }
        }
        System.out.println(arrayList);
    }

    public static void main1(String[] args) {
        List names = new ArrayList<>();
        names.add("a");
        names.add("b");
        names.add("c");
        names.add("d");
        Iterator iterator1 = names.iterator();
        Iterator iterator2 = names.iterator();
        iterator1.next();
        iterator1.remove();
        //由于modcount不一样了，所以会出现异常
        iterator2.next(); // 运行结果？
    }
}

 
