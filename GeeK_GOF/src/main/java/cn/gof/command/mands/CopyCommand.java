package cn.gof.command.mands;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 17:42
 * @description:
 * @question:
 * @link:
 **/
public class CopyCommand extends Command {

    public CopyCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        editor.clipboard = editor.textField.getSelectedText();
        return false;
    }
}
 
