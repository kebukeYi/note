package cn.gof.decorator.dataSource;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 13:41
 * @description: 定义了读取和写入操作的通用数据接口
 * @question :
 * @usinglink : https://refactoringguru.cn/design-patterns/decorator/java/example
 **/
public interface DataSource {

    void writeData(String data);

    String readData();
}
