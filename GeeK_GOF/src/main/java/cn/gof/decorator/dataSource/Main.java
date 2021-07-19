package cn.gof.decorator.dataSource;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 14:01
 * @description:
 * @question:
 * @link:
 **/
public class Main {

    public static void main(String[] args) {
        String salaryRecords = "Name,Salary\nJohn Smith,100000\nSteven Jobs,912000";
        final FileDataSource fileDataSource = new FileDataSource("GeeK_GOF/src/main/resources/OutputDemo.txt");
        final EncryptionDecorator encryptionDecorator = new EncryptionDecorator(fileDataSource);
        final CompressionDecorator compressionDecorator = new CompressionDecorator(encryptionDecorator);
        compressionDecorator.writeData(salaryRecords);

        DataSource plain = new FileDataSource("GeeK_GOF/src/main/resources/OutputDemo.txt");
        System.out.println("- Input ----------------");
        System.out.println(salaryRecords);
        System.out.println("- Encoded --------------");
        System.out.println(plain.readData());
        System.out.println("- Decoded --------------");
        System.out.println(compressionDecorator.readData());
    }
}
 
