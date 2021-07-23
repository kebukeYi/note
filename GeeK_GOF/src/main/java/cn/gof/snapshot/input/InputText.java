package cn.gof.snapshot.input;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 15:35
 * @description:
 * @question:
 * @link:
 **/
public class InputText {

    private StringBuilder append = new StringBuilder();

    public String getText() {
        return append.toString();
    }

    public void append(String str) {
        append.append(str);
    }

    public void setText(String text) {
        append.replace(0, text.length(), text);
    }

    public Snapshot createSnapshot() {
        return new Snapshot(append.toString());
    }

    public void restoreSnapshot(Snapshot snapshot) {
        this.append.replace(0, this.append.length(), snapshot.getText());
    }


}
 
