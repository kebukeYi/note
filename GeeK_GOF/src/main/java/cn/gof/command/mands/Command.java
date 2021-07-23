package cn.gof.command.mands;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 17:41
 * @description: 抽象基础命令
 * @question:
 * @link:
 **/
public abstract class Command {

    public Editor editor;
    private String backup;

    Command(Editor editor) {
        this.editor = editor;
    }

    void backup() {
        backup = editor.textField.getText();
    }

    public void undo() {
        editor.textField.setText(backup);
    }

    public abstract boolean execute();

}
 
