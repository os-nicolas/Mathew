package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.PopUpButton;
import cube.d.n.commoncore.SelectedRow;
import cube.d.n.commoncore.TouchMode;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 9/3/2015.
 */
public class HeaderLine extends Line {

    ArrayList<Button> buttons = new ArrayList<Button>();

    public HeaderLine(Main owner) {
        super(owner);
    }

    public void setButtonsRow(ArrayList<Button> row, float left, float right) {
        float count = row.size();
        float at = left;
        if (count != 0) {
            float step = (right - left) / count;

            for (float i = 0; i < count; i++) {
                Button b = row.get((int) i);
                if (b != null) {
                    b.setLocation(at, at + step, 0,BaseApp.getApp().buttonHeight());
                    buttons.add(b);
                }
                at += step;
            }
        }
    }

    @Override
    public KeyBoard getKeyboad() {
        return null;
    }

    @Override
    public void setKeyBoard(KeyBoard k) {
    }

    @Override
    protected void innerDraw(Canvas canvas, float top, float left, Paint paint) {
        for (Button myBut: buttons) {
            myBut.draw(canvas,paint,top);
        }

        Paint p = new Paint();
        p.setColor(BaseApp.getApp().lightColor);
        Rect r = new Rect(0,0,(int)owner.width+1,(int)top+1);
        canvas.drawRect(r,p);

        Util.drawShadow(canvas,paint.getAlpha()/2,top+BaseApp.getApp().buttonHeight()*owner.height,measureWidth(),true);
    }
    TouchMode myMode;
    @Override
    public boolean onTouch(MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (in(event)) {
                    myMode = TouchMode.KEYBOARD;

                } else {
                    myMode = TouchMode.NOPE;
                }
            }
            if (myMode == TouchMode.KEYBOARD) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    for (Button myBut : buttons) {
                        myBut.click(event);
                    }

                } else {
                    for (Button myBut : buttons) {
                        myBut.hover(event);
                    }
                }
                return true;
            }
        return false;
    }

    private boolean in(MotionEvent event) {
        for (Button b : buttons) {
            if (b.couldClick(event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public float requestedMaxX() {
        return owner.width;
    }

    @Override
    public float requestedMinX() {
        return owner.width;
    }

    @Override
    public float measureHeight() {
        return (owner.height* BaseApp.getApp().buttonHeight())*2;
    }
}
