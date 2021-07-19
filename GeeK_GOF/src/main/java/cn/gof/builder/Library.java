package cn.gof.builder;

import lombok.Builder;
import lombok.ToString;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 10:55
 * @description: 分析 加了 注解后的 class 文件
 * @question:
 * @link:
 **/
@Builder
@ToString
public class Library {

    private boolean isRef;
    private Class type;
    private Object arg;

    public static void main(String[] args) {
        final Library build = Library.builder().type(null).isRef(false).arg(1).build();
        System.out.println(build);
        final Library library = new LibraryBuilder().arg(9).isRef(false).type(null).build();
        System.out.println(library);
    }

}
 
