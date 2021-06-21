package com.java.note.Jdk.OOM;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  12:15
 * @Description
 */
public class GCOverHeadLimitExceededDemo {
    static class Key {
        Integer id;

        Key(Integer id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }


    public static void main(Strings[] args) {
        Map m = new HashMap();
        while (true) {
            for (int i = 0; i < 1000; i++) {
                if (!m.containsKey(new Key(i))) {
                    m.put(new Key(i), "Number:" + i);
                }
            }
            System.out.println("m.size()=" + m.size());
        }
    }

}
