package cn.gof.snapshot.input;

import java.util.Scanner;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 15:39
 * @description:
 * @question:
 * @link:
 **/
public class ApplicationMain {

    public static void main(String[] args) {
        InputText inputText = new InputText();
        SnapshotHolder snapshotsHolder = new SnapshotHolder();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String input = scanner.next();
            if (input.equals(":list")) {
                System.out.println(inputText.getText());
                System.out.println(snapshotsHolder.getStack());
            } else if (input.equals(":undo")) {
//                InputText snapshot = snapshotsHolder.popSnapshot();
//                inputText.setText(snapshot.getText());
                Snapshot snapshot = snapshotsHolder.popSnapshot();
                inputText.restoreSnapshot(snapshot);
            } else {
                snapshotsHolder.pushSnapshot(inputText.createSnapshot());
                inputText.append(input);
            }
        }
    }

}
 
