package cn.gof.snapshot.input;

import java.util.Stack;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 15:38
 * @description:
 * @question:
 * @link:
 **/
public class SnapshotHolder {

    private Stack<InputText> snapshots1 = new Stack<>();

    public String getStack() {
        return snapshots.toString();
    }

    public InputText popSnapshot1() {
        return snapshots1.pop();
    }

    public void pushSnapshot(InputText inputText) {
        InputText deepClonedInputText = new InputText();
        deepClonedInputText.setText(inputText.getText());
        snapshots1.push(deepClonedInputText);
    }

    private Stack<Snapshot> snapshots = new Stack<>();

    public Snapshot popSnapshot() {
        return snapshots.pop();
    }

    public void pushSnapshot(Snapshot snapshot) {
        snapshots.push(snapshot);
    }
}
 
