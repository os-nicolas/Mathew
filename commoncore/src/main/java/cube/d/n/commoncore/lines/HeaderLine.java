package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.Main;
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

    protected void setButtonsRow(ArrayList<Button> row, float left, float right) {
        float count = row.size();
        float at = left;
        float step = (right - left) / count;

        for (float i = 0; i < count; i++) {
            Button b = row.get((int) i);
            if (b!=null){
                b.setLocation(at, at + step, 0, measureHeight());
                buttons.add(b);
            }
            at += step;
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
        Util.drawShadow(canvas,paint.getAlpha()/2,top+measureHeight(),measureWidth(),true);
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
        return owner.height* BaseApp.getApp().buttonHeight();
    }
}
