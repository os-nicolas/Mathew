package cube.d.n.commoncore;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin on 1/3/2015.
 */
public class EquationButton extends Button {
    private static final float WARN_SPACE = 50*BaseApp.getApp().getDpi();
    public static EquationButton current = null;

    public Equation myEq;
    public float x = 0;
    float y = 0;
    float targetX;
    public float targetY;
    int currentAlpha = 0;
    private int targetAlpha = 0xff;
    int bkgCurrentAlpha = 0x0;
    int bkgTargetAlpha = 0x0;
    public EquationLine owner;
    public int targetColor;
    public int currentColor = Color.BLACK;
    private boolean warn = false;
    public Equation warnEq = null;


    public EquationButton(Equation e, EquationLine owner) {
        myEq = e;
        e.active = false;
        this.owner = owner;
    }

    public EquationButton warn(Equation bot) {
        if (bot != null) {
            warn = true;
            warnEq = new WritingEquation(owner);
            warnEq.add(new WritingLeafEquation(BaseApp.getApp().getResources().getString(R.string.assume) + ": ", owner));
            warnEq.add(bot);
            warnEq.add(new WritingLeafEquation("\u2260", owner));
            warnEq.add(NumConstEquation.create(0, owner));

        }
        return this;
    }

    public void draw(Canvas canvas, int stupidX, int stupidY) {
        drawBkg(canvas, x + stupidX, y + stupidY);
        myEq.setColor(currentColor);
        myEq.setAlpha(currentAlpha);

        if (myEq instanceof EqualsEquation){
            ((EqualsEquation) myEq).drawCentered(canvas, x + stupidX, y + stupidY);
        }else {
            myEq.draw(canvas, x + stupidX, y + stupidY);
        }

        // if there is a warning show that too
        if (warn && canvas != null) {
            // we need to find the right end
            float at = (float) (myEq.getX() + (myEq.measureWidth()/2f) + (WARN_SPACE * BaseApp.getApp().zoom));
            Paint p = new Paint(BaseApp.getApp().textPaint);
            p.setTextSize(myEq.getPaint().getTextSize());
            p.setAlpha(currentAlpha);
            p.setColor(currentColor);
//            String s ="assuming: ";
//
//            Rect out =  new Rect();
//            p.getTextBounds(s, 0,(s).length(),out);
//            float h= out.height();
//            float w= out.width();
//            at+= w/2;
//            canvas.drawText(s,at,y + stupidY,p);
//            at+= w/2;
            //at += 10*Algebrator.getAlgebrator().getDpi();
            at += warnEq.measureWidth() / 2;
            warnEq.setAlpha(currentAlpha);
            warnEq.setColor(currentColor);
            warnEq.draw(canvas, at, y + stupidY);
        }
    }

    // x and y are the center of the equation
    public void drawBkg(Canvas canvas, float x, float y) {

        //TODO scale by dpi
        float buffer = (float) (10 * BaseApp.getApp().getDpi() * BaseApp.getApp().zoom);

        float leftEnd;
        float rightEnd;
        float topEnd = (y) - myEq.measureHeightUpper() - buffer;
        float bottomEnd = (y) + myEq.measureHeightLower() + buffer;

       if (myEq instanceof EqualsEquation) {
            float middle = myEq.measureWidth() - (myEq.get(0).measureWidth() + myEq.get(1).measureWidth());
            leftEnd = (x) - (middle / 2) - myEq.get(0).measureWidth() - buffer;
            rightEnd = (x) + (middle / 2) + myEq.get(1).measureWidth() + buffer;
        } else {
            leftEnd = (x) - (myEq.measureWidth() / 2f)- buffer;
            rightEnd = (x) + (myEq.measureWidth() / 2f)+ buffer;
        }

        Paint temp = new Paint();
        //TODO scale by dpi - also do i really want to blurr this?
        temp.setMaskFilter(new BlurMaskFilter(32 * BaseApp.getApp().getDpi(), BlurMaskFilter.Blur.NORMAL));

        temp.setColor(BaseApp.getApp().lightColor);
        temp.setAlpha(bkgCurrentAlpha);

        RectF r = new RectF(leftEnd, topEnd, rightEnd, bottomEnd);


        canvas.drawRoundRect(r, BaseApp.getApp().getCornor(), BaseApp.getApp().getCornor(), temp);

    }

    public void tryRevert(Canvas canvas,float top,float left) {
        if (!this.equals(((AlgebraLine) owner).history.get(0))) {
            if (lastLongTouch != null && lastLongTouch.started()) {
                if (lastLongTouch.done()) {
                    Log.i("lastLongTouch", "done");
                    ((CanTrackChanges) owner).getAfterAnimations().add(new DragStarted((AlgebraLine)owner, 0x7f,0,left));
                    revert();
                    lastLongTouch = null;
                } else {
                     ProgressManager pm = owner.owner.progressManager;
                     pm.drawProgress(0, lastLongTouch.percent(), 0xff,canvas.getWidth(),canvas.getHeight());
                    Log.i("lastLongTouch", lastLongTouch.percent() + "");
                }
            }
        }
    }

    public LongTouch lastLongTouch = null;


    public boolean in(MotionEvent event){
        return inBox(event) && !((AlgebraLine) owner).history.get(0).equals(this);
    }


    TouchMode myMode;
    //long lastTap = 0;
    public boolean click(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (in(event)) {
                myMode = TouchMode.HIS;
                lastLongTouch = new LongTouch(event);
            }else {
                myMode = TouchMode.NOPE;
            }
        }

         if (( lastLongTouch != null && lastLongTouch.outside(event)) || event.getAction() == MotionEvent.ACTION_UP || event.getPointerCount() >1) {
            lastLongTouch = null;
            myMode = TouchMode.NOPE;
        }

        if (myMode ==  TouchMode.NOPE){
            return false;
        }else{
            return true;
        }

    }

    private boolean inBox(MotionEvent event) {

        float buffer = (float) (10 * BaseApp.getApp().getDpi() * BaseApp.getApp().zoom);

        float stupidX;
        float  stupidY=((AlgebraLine) owner).stupid.get().getY();;
        float  leftEnd;
        float  rightEnd;

        if (myEq instanceof EqualsEquation) {
            stupidX = ((AlgebraLine) owner).stupid.get().getX() - ((AlgebraLine) owner).stupid.get().measureWidth()/2f + ((EqualsEquation)((AlgebraLine) owner).stupid.get()).measureLeft();
            leftEnd = (x + stupidX) -  ((EqualsEquation) myEq).measureLeft() - buffer;
            rightEnd = (x + stupidX) + ((EqualsEquation) myEq).measureRight() + buffer;
        } else {
            stupidX = ((AlgebraLine) owner).stupid.get().getX();
            leftEnd = (x + stupidX) - (myEq.measureWidth() / 2f) - buffer;
            rightEnd = (x + stupidX) + (myEq.measureWidth() / 2f) + buffer;
        }


        float topEnd = (y + stupidY) - myEq.measureHeightUpper();
        float bottomEnd = (y + stupidY) + myEq.measureHeightLower();

        if (event.getX() < rightEnd && event.getX() > leftEnd && event.getY() > topEnd && event.getY() < bottomEnd) {
            return true;
        }
        return false;
    }

    private void revert() {
        // update the offset

        if (((Selects) owner).getSelected() != null) {
            ((Selects) owner).setSelected(null);
        }

        // and set this back to be the boss
        ((AlgebraLine) owner).stupid.set(myEq.copy());
        // we don't have to do this but
        ((AlgebraLine) owner).stupidAlpha = 0xff;
        current = null;
        // at time of writing copy does not change active but it might someday so let's be safe


        owner.owner.addToOffsetY(y);

        // we need to remove all history and including this
        ((AlgebraLine) owner).history = new ArrayList<EquationButton>(((AlgebraLine) owner).history.subList(((AlgebraLine) owner).history.indexOf(this), ((AlgebraLine) owner).history.size()));

        // update the offsets of the remaining histories
        for (EquationButton eb : ((AlgebraLine) owner).history) {
            if (eb != this) {
                eb.x -= x;
                eb.y -= y;
            }
        }
        x = 0;
        y = 0;
        bkgCurrentAlpha = 0x0;
        bkgTargetAlpha = 0x0;


            ((AlgebraLine) owner).owner.couldHaveSolved(((AlgebraLine) owner).stupid.get().copy());

    }

    public void update(int stupidX, int stupidY) {
        if (lastLongTouch == null) {
            if (current != null && current.equals(this)) {
                current = null;
            }
            bkgTargetAlpha = 0x00;
            // current copy
            if (current != null && current.lastLongTouch != null && ((AlgebraLine) owner).history.indexOf(this) < ((AlgebraLine) owner).history.indexOf(current) && current.owner.equals(owner)) {
                currentAlpha = (int) (Math.max(((.7f - current.lastLongTouch.percent())), 0) * 0xff);
                targetAlpha = currentAlpha;
            } else {
                targetAlpha = 0xff;
            }
        } else if (lastLongTouch.started()) {
            bkgTargetAlpha = 0xff;
            targetColor = Color.BLACK;
            current = this;
        }

        int rate = BaseApp.getApp().getRate();

        currentAlpha = (currentAlpha * rate + targetAlpha) / (rate + 1);
        bkgCurrentAlpha = (bkgCurrentAlpha * rate + bkgTargetAlpha) / (rate + 1);
        currentColor = BaseApp.colorFade(currentColor, targetColor);


        x = (x * rate + targetX) / (rate + 1);
        y = (y * rate + targetY) / (rate + 1);

    }

    public void updateLocations(int stupidX, int stupidY) {
        if (myEq instanceof EqualsEquation) {
            ((EqualsEquation) myEq).drawCentered(null, x + stupidX, y + stupidY);
        } else {
            myEq.draw(null, x + stupidX, y + stupidY);
        }
    }

    //TODO these are not tatally right
    // stupid should have a get equals center function these should call

    @Override
    protected float top() {
        if (((AlgebraLine) owner).stupid.get().getDrawnAtY() == -1) {
            ((AlgebraLine) owner).stupid.get().updateLocation();
        }
        return y + ((AlgebraLine) owner).stupid.get().getDrawnAtY() - myEq.measureHeightUpper();
    }

    @Override
    protected float bottom() {
        if (((AlgebraLine) owner).stupid.get().getDrawnAtY() == -1) {
            ((AlgebraLine) owner).stupid.get().updateLocation();
        }
        return y + ((AlgebraLine) owner).stupid.get().getDrawnAtY() + myEq.measureHeightLower();
    }

    private void updateLocations() {

        int stupidX;
        int stupidY;
        if (((AlgebraLine) owner).stupid.get() instanceof EqualsEquation) {
            stupidX = (int) ((EqualsEquation)((AlgebraLine) owner).stupid.get()).getCenter();
            stupidY = (int) ((AlgebraLine) owner).stupid.get().getDrawnAtY();
        } else {
            stupidX = (int) ((AlgebraLine) owner).stupid.get().getX();
            stupidY = (int) ((AlgebraLine) owner).stupid.get().getY();
        }
        updateLocations(stupidX, stupidY);

    }

    public float measureRight(){
        float stupidX;
        if (myEq instanceof EqualsEquation) {
            stupidX = ((AlgebraLine) owner).stupid.get().getX() - ((AlgebraLine) owner).stupid.get().measureWidth()/2f + ((EqualsEquation)((AlgebraLine) owner).stupid.get()).measureLeft();
        } else {
            stupidX = ((AlgebraLine) owner).stupid.get().getX();
        }
        return right() - (x+stupidX);
    }

    public float measureLeft(){
        float stupidX;
        if (myEq instanceof EqualsEquation) {
            stupidX = ((AlgebraLine) owner).stupid.get().getX() - ((AlgebraLine) owner).stupid.get().measureWidth()/2f + ((EqualsEquation)((AlgebraLine) owner).stupid.get()).measureLeft();
        } else {
            stupidX = ((AlgebraLine) owner).stupid.get().getX();
        }
        return  (x+stupidX)- left();
    }

    @Override
    public float right() {
        if (((AlgebraLine) owner).stupid.get().getDrawnAtY() == -1) {
            ((AlgebraLine) owner).stupid.get().updateLocation();
        }

        float buffer = (float) (10 * BaseApp.getApp().getDpi() * BaseApp.getApp().zoom);

        float stupidX;
        float  rightEnd;

        if (myEq instanceof EqualsEquation) {
            stupidX = ((AlgebraLine) owner).stupid.get().getX() - ((AlgebraLine) owner).stupid.get().measureWidth()/2f + ((EqualsEquation)((AlgebraLine) owner).stupid.get()).measureLeft();
            rightEnd = (x + stupidX) + ((EqualsEquation) myEq).measureRight() + buffer;
        } else {
            stupidX = ((AlgebraLine) owner).stupid.get().getX();
            rightEnd = (x + stupidX) + (myEq.measureWidth() / 2f) + buffer;
        }

        if (warn) {
            return (float) (rightEnd + warnEq.measureWidth() + WARN_SPACE * BaseApp.getApp().zoom);
        }
        return rightEnd;
    }

    @Override
    public float left() {
        if (((AlgebraLine) owner).stupid.get().getDrawnAtY() == -1) {
            ((AlgebraLine) owner).stupid.get().updateLocation();
        }

        float buffer = (float) (10 * BaseApp.getApp().getDpi() * BaseApp.getApp().zoom);

        float stupidX;
        float  leftEnd;

        if (myEq instanceof EqualsEquation) {
            stupidX = ((AlgebraLine) owner).stupid.get().getX() - ((AlgebraLine) owner).stupid.get().measureWidth()/2f + ((EqualsEquation)((AlgebraLine) owner).stupid.get()).measureLeft();
            leftEnd = (x + stupidX) -  ((EqualsEquation) myEq).measureLeft() - buffer;

        } else {
            stupidX = ((AlgebraLine) owner).stupid.get().getX();
            leftEnd = (x + stupidX) - (myEq.measureWidth() / 2f) - buffer;
        }

        return leftEnd;
    }

    public void updateZoom(double lastZoom, double zoom) {
        y = (float) (y * zoom / lastZoom);

    }
}
