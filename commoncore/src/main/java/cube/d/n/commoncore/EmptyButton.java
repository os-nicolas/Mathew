package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.view.MotionEvent;

import cube.d.n.commoncore.Action.Action;

/**
 * Created by Colin_000 on 5/27/2015.
 */
public class EmptyButton extends Button{

    public EmptyButton() {
        super("",null);
    }

    public void draw(Canvas canvas, Paint p){

        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();

        if (!hover) {
            int currentColor = bkgPaint.getColor();
            currentColor = BaseApp.colorFade(currentColor, BaseApp.getApp().lightColor);
            bkgPaint.setColor(currentColor);
        }
        Paint bkgbkgPaint = new Paint();
        bkgbkgPaint.setColor(targetBkgColor);
        bkgbkgPaint.setAlpha(p.getAlpha());
        RectF r = new RectF(left(), top(), right(), bottom());
        canvas.drawRect(r, bkgbkgPaint);
    }

    @Override
    public void hover(MotionEvent event) {
    }

    @Override
    public boolean click(MotionEvent event) {
        return true;
    }


}
