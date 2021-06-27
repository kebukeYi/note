package com.java.note.Jdk.enums;

/**
 * @Author : mmy
 * @Creat Time : 2020/10/18  下午 1:25
 * @Description 需要注意的是使用在于switch条件进行结合使用时，无需使用Color引用。
 */
enum Color {GREEN, RED, BLUE}

enum EnumDemo4 {
    ;

    public static void printName(Color color) {
        switch (color) {
            case BLUE: //无需使用Color进行引用
                System.out.println("蓝色");
                break;
            case RED:
                System.out.println("红色");
                break;
            case GREEN:
                System.out.println("绿色");
                break;
        }
    }

    public static void main(String[] args) {
        printName(Color.BLUE);
        printName(Color.RED);
        printName(Color.GREEN);

        //蓝色
        //红色
        //绿色
    }


}


