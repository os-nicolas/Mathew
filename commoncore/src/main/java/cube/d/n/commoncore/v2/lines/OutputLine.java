package cube.d.n.commoncore.v2.lines;

import android.graphics.Canvas;
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

    public OutputLine(Main owner, Equation newEq){
        super(owner);
        stupid.set(newEq);
        reduce(newEq);

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

        Rect r = new Rect((int)left,(int)top,(int)(left+ measureWidth()),(int)(top+measureHeight()));
        Paint p = new Paint();
        p.setAlpha(paint.getAlpha());
        p.setColor(0x35243653);
        canvas.drawRect(r,p);

        stupid.get().draw(canvas,  left +( measureWidth() / 2f),top + buffer + stupid.get().measureHeightUpper());
    }

    @Override
    public pm parentThesisMode() {
        return pm.SOLVE;
    }
}
