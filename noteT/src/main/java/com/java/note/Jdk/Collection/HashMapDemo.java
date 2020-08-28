package com.java.note.Jdk.Collection;

import lombok.Data;

import java.util.HashSet;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/16  17:41
 * @Description
 */
public class HashMapDemo {

    public static final int MAXIMUM_CAPACITY = 1 << 30;

    static final int tableSizeFor(int cap) {
        //容量减1，为了防止初始化容量已经是2的幂的情况，否则传入16返回32，-1的话 传入16返回16，最后有+1运算。
        int n = cap - 1;
//        int n = cap ;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        //如果入参cap为小于或等于0的数，那么经过cap-1之后n为负数，n经过无符号右移和或操作后仍未负数,
        // 所以如果n<0,则返回1;如果n大于或等于最大容量，则返回最大容量;否则返回n+1
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }


    public static void main(String[] args) {
        String string = "a";
        String b = "a";
        String c = new String("a");
        System.out.println(string == b);
        System.out.println(c == b);
        System.out.println(c == string);
        System.out.println(tableSizeFor(32));

        System.out.println("===============================");


        HashSet hashSet = new HashSet();

        Point point1 = new Point(1, 2);
        Point point2 = new Point(1, 2);
        Point point3 = new Point(1, 3);

//        System.out.println(point1.equals(point2));
//        System.out.println(point1 == point2);


        System.out.println(point2.equals(point3));
        System.out.println(point2 == point3);


        System.out.println(point1.hashCode());
        System.out.println(point2.hashCode());
        System.out.println(point3.hashCode());


        hashSet.add(point1);
        
        hashSet.add(point2);
        hashSet.add(point3);

        System.out.println(hashSet);


    }
}


class Point {

    int lat;
    int lng;

    public Point(int lat, int lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) return false;
        if (o == null) return false;
        if (o == this) return true;
        Point point = (Point) o;
        return this.lng == point.getLng() && this.lat == point.getLat();
//        return super.equals(o);
    }

    @Override
    public int hashCode() {
//        return this.lat + this.lng;
        return 2;
//        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Point{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
