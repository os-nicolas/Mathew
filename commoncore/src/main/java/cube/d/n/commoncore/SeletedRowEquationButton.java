package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin_000 on 6/22/2015.
 */
public class SeletedRowEquationButton extends SelectedRowButtons {
    public final Equation myEq;

    public SeletedRowEquationButton(Equation eq, Action myAction) {
        super("", myAction);
        myEq = eq;
    }

    public void draw(Canvas canvas, Paint p){

        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();

        if (!hover) {
            int currentColor = bkgPaint.getColor();
            currentColor = BaseApp.colorFade(currentColor, targetBkgColor);
            bkgPaint.setColor(currentColor);
        }
        Paint bkgbkgPaint = new Paint();
        bkgbkgPaint.setColor(targetBkgColor);
        bkgbkgPaint.setAlpha(p.getAlpha());
        RectF r = new RectF(left(), top(), right(), bottom());
        canvas.drawRect(r, bkgbkgPaint);


        // if they are the same color, but both are somewhat transparent it looks weird
        // this happen on fade ins
        if (p.getAlpha() == 0xff) {
            float smaller = 3 * BaseApp.getApp().getDpi();
            RectF r2 = new RectF(left() + smaller, top() + smaller, right() - smaller, bottom() - smaller);
            bkgPaint.setAlpha(p.getAlpha());
            canvas.drawRoundRect(r2, BaseApp.getApp().getCornor(), BaseApp.getApp().getCornor(), bkgPaint);
        }

        int myAlpha = textPaint.getAlpha() * p.getAlpha() / (0xff);
        if (myAlpha != 0){
        float buffer = BaseApp.getApp().getBuffer();

        int dbCount =0;

        myEq.overWriteZoom(1);
        while (myEq.measureWidth() + 2 * buffer > measureWidth() ||myEq.measureHeight() + 2 * buffer > targetHeight()) {
            myEq.overWriteZoom(myEq.getMyZoom()*.9f);
            dbCount++;
            if (dbCount>20){
                Log.d("sad", "we are stuck");
            }
        }
        myEq.setAlpha(myAlpha);
        myEq.draw(canvas,getX(),getY());
        }

    }
}
