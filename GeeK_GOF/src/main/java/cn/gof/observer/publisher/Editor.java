package cn.gof.observer.publisher;

import java.io.File;

/**
 * @author : kebukeYi
 * @date :  2021-07-20 16:32
 * @description: 具体发布者， 由其他对象追踪
 * @question:
 * @link:
 **/
public class Editor {

    public EventManager events;
    private File file;

    public Editor() {
        this.events = new EventManager("open", "save");
    }

    public void openFile(String filePath) {
        this.file = new File(filePath);
        //这里主动调用了
        events.notify("open", file);
    }

    public void saveFile() throws Exception {
        if (this.file != null) {
            //这里主动调用了
            events.notify("save", file);
        } else {
            throw new Exception("Please open a file first.");
        }
    }
}
 
