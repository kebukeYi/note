package cn.gof.observer.publisher;

/**
 * @author : kebukeYi
 * @date :  2021-07-20 16:34
 * @description:
 * @question:
 * @link:
 **/
public class PublisherDemo {

    public static void main(String[] args) {
        Editor editor = new Editor();
        //提前进行注册
        editor.events.subscribe("open", new LogOpenListener("/path/to/log/file.txt"));
        editor.events.subscribe("save", new EmailNotificationListener("admin@example.com"));

        try {
            //直接进行操作
            editor.openFile("test.txt");
            editor.saveFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
 
