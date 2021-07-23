package cn.gof.command.mands;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 17:42
 * @description:
 * @question:
 * @link:
 **/
public class PasteCommand extends Command {
    public PasteCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        if (editor.clipboard == null || editor.clipboard.isEmpty()) return false;

        backup();
        editor.textField.insert(editor.clipboard, editor.textField.getCaretPosition());
        return true;
    }
}
 
