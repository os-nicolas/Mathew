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
    KeyBoard k;
    public String message;

    public MessageButton(String text, MessageAction ma) {
        this(text,ma,2000l);
    }

    public MessageButton(String text, MessageAction ma, final long runTime) {
        super(text, ma);
        message = text;

        k = ma.owner.getKeyboad();
        targetBkgColor=BaseApp.getApp().darkDarkColor;
        this.textPaint.setColor(Color.WHITE);
        if (runTime!= -1){
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
        });  th.start();
        }

    }

    public MessageButton(String text, EquationLine equationLine) {
        this(text, new MessageAction(equationLine));
    }

    protected void fullyHidden() {
        // we need to remove from keyboard
        k.popUpLines.remove(this);
    }

    public boolean getCan(){
        return ((MessageAction)myAction).canAct();
    }
}
