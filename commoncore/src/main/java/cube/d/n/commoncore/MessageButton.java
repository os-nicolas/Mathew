package cube.d.n.commoncore;

import android.graphics.Color;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Action.TimeOutAction;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 8/21/2015.
 */
public class MessageButton  extends  PopUpButton {
    boolean done = false;
    long runTime = 2000l;
    KeyBoard k;
    public String message;

    public MessageButton(String text, EquationLine equationLine) {
        super(text, new MessageAction(equationLine));
        message = text;

        k = equationLine.getKeyboad();
        targetBkgColor=BaseApp.getApp().darkDarkColor;
        this.textPaint.setColor(Color.WHITE);
        Thread th = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Thread.sleep(runTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((MessageAction)myAction).done();
            }
        });
        th.start();
    }

    protected void fullyHidden() {
        // we need to remove from keyboard
        k.popUpLines.remove(this);
    }
}
