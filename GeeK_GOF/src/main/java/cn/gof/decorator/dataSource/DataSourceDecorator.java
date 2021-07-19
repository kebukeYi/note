package cn.gof.decorator.dataSource;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 13:47
 * @description: 抽象基础装饰  类似于  FilterInputStream
 * @question:
 * @link:
 **/
public class DataSourceDecorator implements DataSource {

    private DataSource wrapper;

    DataSourceDecorator(DataSource source) {
        this.wrapper = source;
    }

    @Override
    public void writeData(String data) {
        wrapper.writeData(data);
    }

    @Override
    public String readData() {
        return wrapper.readData();
    }
}
 
