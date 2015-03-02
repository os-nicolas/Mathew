package colin.example.algebrator;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import colin.algebrator.eq.EqualsEquation;
import colin.algebrator.eq.Equation;

import java.util.ArrayList;

/**
 * Created by Colin on 1/3/2015.
 */
public class EquationButton extends Button {
    public static EquationButton current = null;

    Equation myEq;
    float x = 0;
    float y = 0;
    float targetX;
    float targetY;
    int currentAlpha = 0;
    private int targetAlpha = 0xff;
    int bkgCurrentAlpha = 0x0;
    int bkgTargetAlpha = 0x0;
    ColinView cv;
    public int targetColor;
    public int currentColor = Color.BLACK;

    public EquationButton(Equation e, ColinView cv) {
        myEq = e;
        e.active = false;
        this.cv = cv;
    }

    public void draw(Canvas canvas, int stupidX, int stupidY) {
        drawBkg(canvas, x + stupidX, y + stupidY);
        myEq.setColor(currentColor);
        myEq.setAlpha(currentAlpha);
        ((EqualsEquation) myEq).drawCentered(canvas, x + stupidX, y + stupidY);
    }

    // x and y are the center of the equation
    public void drawBkg(Canvas canvas, float x, float y) {

        //TODO scale by dpi
        float buffer = 10*Algebrator.getAlgebrator().getDpi();

        float middle = myEq.measureWidth() - (myEq.get(0).measureWidth() + myEq.get(1).measureWidth());
        float leftEnd = x - (middle / 2) - myEq.get(0).measureWidth() - buffer;
        float rightEnd = x + (middle / 2) + myEq.get(1).measureWidth() + buffer;
        float topEnd = y - myEq.measureHeightUpper() - buffer;
        float bottomEnd = y + myEq.measureHeightLower() + buffer;

        Paint temp = new Paint();
        //TODO scale by dpi - also do i really want to blurr this?
        temp.setMaskFilter(new BlurMaskFilter(32*Algebrator.getAlgebrator().getDpi(), BlurMaskFilter.Blur.NORMAL));

        temp.setColor(Algebrator.getAlgebrator().lightColor);
        temp.setAlpha(bkgCurrentAlpha);

        RectF r = new RectF(leftEnd, topEnd, rightEnd, bottomEnd);


        canvas.drawRoundRect(r, Algebrator.getAlgebrator().getCornor(), Algebrator.getAlgebrator().getCornor(), temp);

    }

    public void tryRevert(Canvas canvas) {
        if (!this.equals(cv.history.get(0))) {
            if (lastLongTouch != null && lastLongTouch.started()) {
                if (lastLongTouch.done()) {
                    Log.i("lastLongTouch", "done");
                    cv.animation.add(new DragStarted(cv, 0x7f));
                    revert();
                    lastLongTouch = null;
                } else {
                    cv.drawProgress(canvas, lastLongTouch.percent(), 0xff);
                    Log.i("lastLongTouch", lastLongTouch.percent() + "");
                }
            }
        }
    }

    LongTouch lastLongTouch = null;

    //long lastTap = 0;
    public void click(MotionEvent event) {
        if (inBox(event) && !cv.history.get(0).equals(this)) {
            Log.d("highlighting ", myEq.toString());

            if (lastLongTouch == null && event.getAction() == MotionEvent.ACTION_DOWN) {
                lastLongTouch = new LongTouch(event);
            } else if (lastLongTouch != null && lastLongTouch.outside(event)) {
                lastLongTouch = null;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            lastLongTouch = null;
        }

    }

    private boolean inBox(MotionEvent event) {
        float stupidX = cv.stupid.lastPoint.get(0).x;
        float stupidY = cv.stupid.lastPoint.get(0).y;

        float middle = myEq.measureWidth() - (myEq.get(0).measureWidth() + myEq.get(1).measureWidth());
        float leftEnd = (x + stupidX) - (middle / 2) - myEq.get(0).measureWidth();
        float rightEnd = (x + stupidX) + (middle / 2) + myEq.get(1).measureWidth();
        float topEnd = (y + stupidY) - myEq.measureHeightUpper();
        float bottomEnd = (y + stupidY) + myEq.measureHeightLower();
        if (event.getX() < rightEnd && event.getX() > leftEnd && event.getY() > topEnd && event.getY() < bottomEnd) {
            return true;
        }
        return false;
    }

    private void revert() {
        // update the offset
        cv.offsetX += x;
        cv.offsetY += y;



        // and set this back to be the boss
        cv.stupid = myEq.copy();
        // we don't have to do this but
        cv.stupidAlpha = 0xff;
        current = null;
        // at time is writing copy does not change active but it might someday so let's be safe
        cv.stupid.active = true;
        cv.stupid.updateLocation();

        // we need to remove all history and including this
        cv.history = new ArrayList<EquationButton>(cv.history.subList(cv.history.indexOf(this), cv.history.size()));

        // update the offsets of the remaining histories
        for (EquationButton eb : cv.history) {
            if (eb != this) {
                eb.x -= x;
                eb.y -= y;
            }
        }
        x = 0;
        y = 0;
        bkgCurrentAlpha = 0x0;
        bkgTargetAlpha = 0x0;
    }

    public void update(int stupidX, int stupidY) {
        if (lastLongTouch == null) {
            if (current!=null && current.equals( this)){
                current = null;
            }
            bkgTargetAlpha = 0x00;
            // current copy
            if (current!= null && current.lastLongTouch!= null && cv.history.indexOf(this) < cv.history.indexOf(current)){
                currentAlpha = (int)(Math.max(((.7f-current.lastLongTouch.percent()) ),0)*0xff);
                targetAlpha = currentAlpha;
            }else{
                targetAlpha = 0xff;
            }
        } else if (lastLongTouch.started()){
            bkgTargetAlpha = 0xff;
            targetColor = Color.BLACK;
            current = this;
        }

        int rate = Algebrator.getAlgebrator().getRate();

        currentAlpha = (currentAlpha * rate + targetAlpha) / (rate + 1);
        bkgCurrentAlpha = (bkgCurrentAlpha * rate + bkgTargetAlpha) / (rate + 1);
        currentColor = Algebrator.colorFade(currentColor,targetColor);


        x = (x * rate + targetX) / (rate + 1);
        y = (y * rate + targetY) / (rate + 1);

    }

    public void updateLocations(int stupidX, int stupidY) {
        ((EqualsEquation) myEq).drawCentered(null, x + stupidX, y + stupidY);
    }
}
