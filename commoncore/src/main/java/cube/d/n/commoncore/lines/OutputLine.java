package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.HasHeaderLine;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.BinaryOperator;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MonaryEquation;
import cube.d.n.commoncore.eq.any.MultiDivSuperEquation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.PlusMinusEquation;
import cube.d.n.commoncore.keyboards.KeyBoard;


/**
 * Created by Colin_000 on 5/7/2015.
 */
public class OutputLine extends EquationLine implements HasHeaderLine {

    float eqY;
    float currentAlpha;


    public OutputLine(final Main owner, Equation newEq) {
        super(owner);

        //Equation oldEq = newEq.copy();
        stupid.set(newEq);
        Util.reduce(stupid);
        //EqualsEquation ee = new EqualsEquation(this);
        //ee.add(oldEq);
        //ee.add(stupid.get());
        //stupid.set(ee);

        float targetY = +getBuffer() + stupid.get().measureHeightUpper();
        currentAlpha = 0x00;
        eqY = targetY - measureHeight();
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(500l);
                    owner.addLine(BaseApp.getApp().getInputLine(owner));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        th.start();


    }




    private KeyBoard myKeyBoard = null;

    @Override
    public KeyBoard getKeyboad() {
//        if (myKeyBoard == null){
//            myKeyBoard = new EnptyKeyboard(owner);
//        }
//        return myKeyBoard;
        return null;
    }

    @Override
    public void setKeyBoard(KeyBoard k) {

    }


    double lastZoom = BaseApp.getApp().zoom;

    @Override
    protected void innerDraw(Canvas canvas, float top, float left, Paint paint) {




//        Rect r = new Rect((int)0,(int)top,(int)(0+ measureWidth()),(int)(top+measureHeight()));
//        Paint p = new Paint();
//        p.setAlpha(paint.getAlpha());
//        p.setColor(0x35243653);
//        canvas.drawRect(r,p);

        int rate = BaseApp.getApp().getRate();
        currentAlpha = (currentAlpha * rate + 0xff) / (rate + 1);
        stupid.get().setColor(BaseApp.getApp().darkDarkColor);
        stupid.get().setAlpha((int) currentAlpha);
        float targetY = +getBuffer() + stupid.get().measureHeightUpper();
        if (lastZoom != BaseApp.getApp().zoom) {
            eqY *= BaseApp.getApp().zoom / lastZoom;
            lastZoom = BaseApp.getApp().zoom;
        }
        eqY = (eqY * rate + targetY) / (rate + 1);


        stupid.get().draw(canvas, left + (measureWidth() / 2f), top + eqY);

        if (!centered) {
            getImputLine().toAddToOffsetX(-getImputLine().getOffsetX());
            centered = true;
        }
    }

    boolean centered = false;

    @Override
    public float requestedMaxX() {
        return Math.max(0, (stupid.get().measureWidth() + getBuffer() * 2 - owner.width) / 2f);
    }

    @Override
    public float requestedMinX() {
        return -Math.max(0, (stupid.get().measureWidth() + getBuffer() * 2 - owner.width) / 2f);
    }

    @Override
    public pm parentThesisMode() {
        return pm.SOLVE;
    }
}
