package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.keyboards.EnptyKeyboard;
import cube.d.n.commoncore.keyboards.KeyBoard;


/**
* Created by Colin_000 on 5/7/2015.
*/
public class OutputLine extends Line {

    float eqY;
    float currentAlpha;


    public OutputLine(final Main owner, Equation newEq){
        super(owner);

        //Equation oldEq = newEq.copy();
        stupid.set(newEq);
        reduce();
        //EqualsEquation ee = new EqualsEquation(this);
        //ee.add(oldEq);
        //ee.add(stupid.get());
        //stupid.set(ee);

        float targetY = +  getBuffer() + stupid.get().measureHeightUpper();
        currentAlpha = 0x00;
        eqY = targetY - measureHeight();
        Thread th = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(500l);
                    owner.addLine(new InputLine(owner));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        };
        th.start();



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
//        if (myKeyBoard == null){
//            myKeyBoard = new EnptyKeyboard(owner);
//        }
//        return myKeyBoard;
        return null;
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
        stupid.get().getPaint().setColor(BaseApp.getApp().darkDarkColor);
        stupid.get().setAlpha((int)currentAlpha);
        float targetY = +  getBuffer() + stupid.get().measureHeightUpper();
        if (lastZoom != BaseApp.getApp().zoom){
            eqY *= BaseApp.getApp().zoom/lastZoom;
            lastZoom= BaseApp.getApp().zoom;
        }
        eqY = (eqY * rate + targetY) / (rate + 1);


        stupid.get().draw(canvas,  left +( measureWidth() / 2f),top + eqY);


    }


    @Override
    public float requestedMaxX() {
        return Math.max(0,(stupid.get().measureWidth()+  getBuffer()*2-owner.width)/2f);
    }

    @Override
    public float requestedMinX() {
        return -Math.max(0,(stupid.get().measureWidth()+  getBuffer()*2-owner.width)/2f);
    }

    @Override
    public pm parentThesisMode() {
        return pm.SOLVE;
    }
}
