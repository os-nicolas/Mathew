package cube.d.n.commoncore.v2.lines;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.v2.Main;
import cube.d.n.commoncore.v2.keyboards.EnptyKeyboard;
import cube.d.n.commoncore.v2.keyboards.InputKeyboard;
import cube.d.n.commoncore.v2.keyboards.KeyBoard;
import cube.d.n.commoncore.v2.lines.Line;


/**
* Created by Colin_000 on 5/7/2015.
*/
public class OutputLine extends Line {

    float eqY;
    float currentAlpha;

    public OutputLine(Main owner, Equation newEq){
        super(owner);
        stupid.set(newEq);
        reduce();
        float targetY = + buffer + stupid.get().measureHeightUpper();
        currentAlpha = 0x00;
        eqY = targetY - measureHeight();

    }

    @Override
    public float requestedWidth() {
        return stupid.get().measureWidth() + buffer*2;
    }

    private void reduce() {
        int at =0;
        while (at<stupid.get().size()) {
            Equation last = null;
            while (last == null || !last.same(stupid.get())) {
                last = stupid.get().copy();
                reduce(stupid.get().get(at));
            }
            at++;
        }
        at =0;

        while (at<stupid.get().size()) {
            Equation last = null;
            while (last == null || !last.same(stupid.get())) {
                last = stupid.get().copy();
                stupid.get().tryOperator(at);
            }
            at++;
        }
    }

    private Equation reduce(Equation newEq) {
        int at =0;
        while (at<newEq.size()) {
            Equation last = null;
            while (last == null || !last.same(newEq)) {
                last = newEq.copy();
                reduce(newEq.get(at));
            }
            at++;
        }
        at =0;

        while (at<newEq.size()) {
            Equation last = null;
            while (last == null || !last.same(newEq)) {
                last = newEq.copy();
                newEq.tryOperator(at);
            }
            at++;
        }

        return newEq;
    }

    private KeyBoard myKeyBoard = null;
    @Override
    public KeyBoard getKeyboad() {
        if (myKeyBoard == null){
            myKeyBoard = new EnptyKeyboard(owner);
        }
        return myKeyBoard;
    }

    @Override
    protected void innerDraw(Canvas canvas, float top, float left, Paint paint) {

//        Rect r = new Rect((int)0,(int)top,(int)(0+ measureWidth()),(int)(top+measureHeight()));
//        Paint p = new Paint();
//        p.setAlpha(paint.getAlpha());
//        p.setColor(0x35243653);
//        canvas.drawRect(r,p);

        int rate = BaseApp.getApp().getRate();
        currentAlpha = (currentAlpha * rate + 0xff) / (rate + 1);
        stupid.get().setAlpha((int)currentAlpha);
        float targetY = + buffer + stupid.get().measureHeightUpper();
        eqY = (eqY * rate + targetY) / (rate + 1);


        stupid.get().draw(canvas,  left +( measureWidth() / 2f),top + eqY);
    }

    @Override
    public pm parentThesisMode() {
        return pm.SOLVE;
    }
}
