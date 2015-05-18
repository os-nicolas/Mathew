package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;
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
public class OutputLine extends Line {

    float eqY;
    float currentAlpha;


    public OutputLine(final Main owner, Equation newEq) {
        super(owner);

        //Equation oldEq = newEq.copy();
        stupid.set(newEq);
        reduce();
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
                    owner.addLine(new InputLine(owner));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        th.start();


    }


    private void reduce() {

        Equation outerLast = null;
        int startAt =0;
        while (outerLast == null || !outerLast.reallySame(stupid.get())) {
            int at = startAt;
            outerLast = stupid.get().copy();

            while (at < stupid.get().size()) {
                reduce(stupid.get() ,at);

                stupid.get().fixIntegrety();
                if (at+1 < stupid.get().size()){
                    reduce(stupid.get(),at+1);
                    stupid.get().fixIntegrety();
                }

                if (shouldOperate(stupid.get(),at)){
                    stupid.get().tryOperator(at);
                }
                stupid.get().fixIntegrety();
                if (at<  stupid.get().size()){
                if ((stupid.get().get(at) instanceof DivEquation &&
                        Operations.sortaNumber(stupid.get().get(at).get(1)) &&
                        Operations.getValue(stupid.get().get(at).get(1)).doubleValue() ==0)||(
                        stupid.get().get(at) instanceof PlusMinusEquation && stupid.get().get(at).get(0).size()==0
                )){
                    Equation oldEq = stupid.get().get(at);
                    stupid.get().get(at).justRemove();
                    stupid.get().add(0,oldEq);
                    startAt++;
                }}
                at++;
            }
        }
    }



    private Equation reduce(Equation parent,int index) {
        Equation outerLast = null;
        Log.i("outerLast",  stupid.get() + "");
        Equation newEq = parent.get(index);
        int startAt =0;
        while (outerLast == null || !outerLast.reallySame(newEq)) {
            int at = startAt;
            outerLast = newEq.copy();
            Log.i("outerLast", stupid.get() + "");

            while (at < newEq.size()) {
                reduce(newEq,at);
                newEq.fixIntegrety();
                if (at+1 < newEq.size()){
                    reduce(newEq,at+1);
                    newEq.fixIntegrety();
                }
                if (shouldOperate(newEq,at)) {
                    newEq.tryOperator(at);
                }
                newEq = parent.get(index);
                newEq.fixIntegrety();

                if (at<  newEq.size()) {
                    if ((newEq.get(at) instanceof DivEquation &&
                            Operations.sortaNumber(newEq.get(at).get(1)) &&
                            Operations.getValue(newEq.get(at).get(1)).doubleValue() == 0) || (
                            newEq.get(at) instanceof PlusMinusEquation && newEq.get(at).get(0).size() == 0 && newEq  instanceof AddEquation
                    )) {
                        Equation oldEq = newEq.get(at);
                        newEq.get(at).justRemove();
                        newEq.add(0, oldEq);
                        startAt++;
                    }
                }
                at++;
            }
        }

        return newEq;
    }

    private boolean shouldOperate(Equation equation, int at) {
        if (equation instanceof BinaryOperator &&  at +1 <equation.size() ){
            if ((equation.get(at).removeNeg() instanceof NumConstEquation)&&
                    (equation.get(at+1).removeNeg() instanceof NumConstEquation)){
                return true;
            }
            //div and multi don'r care about +-
            if (equation instanceof MultiDivSuperEquation){
                if ((equation.get(at).removeSign() instanceof NumConstEquation || equation.get(at).removeSign() instanceof DivEquation)&&
                        (equation.get(at+1).removeSign() instanceof NumConstEquation || equation.get(at+1).removeSign() instanceof DivEquation)){
                    return true;
                }
            }
            // we want to expand tho
            if (equation instanceof MultiEquation){
                if ((equation.get(at).removeNeg() instanceof NumConstEquation || equation.get(at).removeNeg() instanceof AddEquation)&&
                        (equation.get(at+1).removeNeg() instanceof NumConstEquation || equation.get(at+1).removeNeg() instanceof AddEquation)){
                    return true;
                }
            }
            // we want to put things under a common denominator also
            if (equation instanceof AddEquation){
                if ((equation.get(at).removeNeg() instanceof NumConstEquation || equation.get(at).removeNeg() instanceof DivEquation)&&
                        (equation.get(at+1).removeNeg() instanceof NumConstEquation || equation.get(at+1).removeNeg() instanceof DivEquation)){
                    return true;
                }
            }

        }


        if (equation instanceof MonaryEquation){
            return true;
        }
        return false;
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
