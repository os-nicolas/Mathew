package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;


import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.eq.any.Equation;

public class Button implements Physical {
    // in percent of width (1 = full width)
    float left;
    // in percent of width (1 = full width)
    float right;
    // in percent of height (1 = full height)
    float top;
    //  in percent of height (1 = full height)


    float canvasWidth;
    float canvasHeight;

    float bottom;
    Paint bkgPaint;
    public Paint textPaint;
    int highlightColor;
    int targetBkgColor;
    String text;
    SuperAction myAction;
    boolean hover = false;
    private int h=-1;
    private long lastClick=0;
    private final long fadeTime=300;

    public Button() {

    }

    public Button( String text, SuperAction myAction) {
        super();
        this.myAction = myAction;
        this.text = text;
        this.bkgPaint = new TextPaint(BaseApp.getApp().bkgPaint);
        this.highlightColor = BaseApp.getApp().darkColor;
        targetBkgColor = BaseApp.getApp().lightColor;
        bkgPaint.setColor(targetBkgColor);
        this.textPaint = new TextPaint(BaseApp.getApp().textPaint);

        //TODO scale by dpi
        //TODO does not work at all
    }

    public Button withColor(int color){
        this.bkgPaint.setColor(color);
        targetBkgColor = color;
        return this;
    }

    public Button withHighLightColor(int color){
        this.highlightColor=color;
        return this;
    }

    public Button withTextColor(int color){
        this.textPaint.setColor(color);
        return this;
    }

    public void setLocation(float left, float right, float top, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public Button(String text, Action myAction, float left, float right, float top, float bottom) {
        this(text, myAction);
        setLocation(left, right, top, bottom);
    }

    public void draw(Canvas canvas, Paint p){
        draw(canvas, p,0);

    }

    public void draw(Canvas canvas) {
        draw(canvas,new Paint());
    }

    protected float targetHeight() {
        return measureHeight();
    }

    float offset=0;
    protected float top() {
        return (top * canvasHeight) + offset;
    }

    protected float left() {
        return left * canvasWidth;
    }

    protected float bottom() {
        return bottom * canvasHeight+ offset;
    }

    protected float right() {
        return right * canvasWidth;
    }

    public boolean couldClick(MotionEvent event) {
        return (event.getX() < right() && event.getX() > left() && event.getY() < bottom() && event.getY() > top() && event.getPointerCount() == 1);
    }


    public boolean click(MotionEvent event) {
        if (couldClick(event)) {
            hover = false;
            if (myAction.canAct()) {
                bkgPaint.setColor(highlightColor);
                if (myAction != null) {
                    lastClick = System.currentTimeMillis();
//                    TutMessage.getMessage(TypeEqTut.class).alreadyDone();
                    myAction.act();
                }
            }else{
                bkgPaint.setColor(toGreyScale(highlightColor));
            }
        }
        return true;
    }

    private int toGreyScale(int input) {
        int alpha = input/0x01000000;
        input -= alpha*0x01000000;
        int red = input/0x010000;
        input -= red*0x010000;
        int green = input/0x0100;
        input -= green*0x0100;
        int blue = input/0x01;
        input -= blue*0x01;

        int target = (red + green + blue)/3;

        int result = alpha*0x01000000
                + target*0x010101;
        return result;
    }

    public void hover(MotionEvent event) {
        if (couldClick(event)) {
            hover = true;
            if (myAction.canAct()) {
                bkgPaint.setColor(highlightColor);
                bkgPaint.setAlpha(0xff/2);
            } else {
                bkgPaint.setColor(toGreyScale(highlightColor));
                bkgPaint.setAlpha(0xff/2);
            }
        }else{
            hover = false;
            bkgPaint.setColor(targetBkgColor);
        }
    }


    @Override
    public float measureWidth() {
        return right() - left();
    }

    @Override
    public float measureHeight() {
        return bottom() - top();
    }

    @Override
    public float getX() {
        return (left() + right()) / 2;
    }

    @Override
    public float getY() {
        return (top() + bottom()) / 2;
    }


    public static void drawEqButton(Button b, Canvas canvas, Paint p, Equation myEq) {
        b.canvasHeight = canvas.getHeight();
        b.canvasWidth = canvas.getWidth();

        long now = System.currentTimeMillis();

        if (!b.hover) {
            int currentColor = b.bkgPaint.getColor();
            currentColor = BaseApp.colorFade(currentColor, b.targetBkgColor);
            b.bkgPaint.setColor(currentColor);
        }
        Paint bkgbkgPaint = new Paint();
        bkgbkgPaint.setColor(b.targetBkgColor);
        bkgbkgPaint.setAlpha(p.getAlpha());
        RectF r = new RectF(b.left(), b.top(), b.right(), b.bottom());
        canvas.drawRect(r, bkgbkgPaint);


        // if they are the same color, but both are somewhat transparent it looks weird
        // this happen on fade ins
        if (p.getAlpha() == 0xff) {
            float smaller = 3 * BaseApp.getApp().getDpi();
            RectF r2 = new RectF(b.left() + smaller, b.top() + smaller, b.right() - smaller, b.bottom() - smaller);
            b.bkgPaint.setAlpha(p.getAlpha());
            canvas.drawRoundRect(r2, BaseApp.getApp().getCornor(), BaseApp.getApp().getCornor(), b.bkgPaint);
        }

        int myAlpha = b.textPaint.getAlpha() * p.getAlpha() / (0xff);
        if (myAlpha != 0){
            float buffer = BaseApp.getApp().getBuffer();

            int dbCount =0;

            myEq.overWriteZoom(1);
            while (myEq.measureWidth() + 2 * buffer > b.measureWidth() ||myEq.measureHeight() + 2 * buffer > b.targetHeight()) {
                myEq.overWriteZoom(myEq.getMyZoom()*.9f);
                dbCount++;
                if (dbCount>20){
                    Log.d("sad", "we are stuck");
                }
            }
            myEq.setAlpha(myAlpha);
            myEq.draw(canvas,b.getX(),b.getY());
        }
    }

    public void draw(Canvas canvas, Paint p, float t) {
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();

        offset = t;

        long now = System.currentTimeMillis();

        if (!hover){
            if (now - lastClick < fadeTime) {
                long elapsedTime = now - lastClick;
                float prcnt = ((float) elapsedTime) / ((float) fadeTime);
                int currentColor = Util.colorMix(highlightColor, targetBkgColor, prcnt);
                bkgPaint.setColor(currentColor);
            } else {
                bkgPaint.setColor(targetBkgColor);
            }
        }
        Paint bkgbkgPaint = new Paint();
        bkgbkgPaint.setColor(targetBkgColor);
        bkgbkgPaint.setAlpha(Math.min(p.getAlpha() ,bkgbkgPaint.getAlpha()));
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


        measureText();

        //TODO this seems ugly
        if (!(this instanceof PopUpButton ) ){
            float rate = BaseApp.getApp().getRate();
            if (myAction.canAct()) {
                float currentAlpha = this.textPaint.getAlpha();
                currentAlpha = (currentAlpha * (rate - 1) + 0xff) / rate;
                this.textPaint.setAlpha((int) currentAlpha);
            } else {
                float currentAlpha = this.textPaint.getAlpha();
                currentAlpha = (currentAlpha * (rate - 1) + (0xff) / 2) / rate;
                this.textPaint.setAlpha((int) currentAlpha);
            }
        }
        textPaint.setAlpha(textPaint.getAlpha() * p.getAlpha() / (0xff));
        canvas.drawText(text, getX(), getY() +(h / 2), textPaint);
    }

    protected void measureText() {
        if (h ==-1) {
            Rect out = new Rect();
            //TODO scale by dpi
            float buffer = BaseApp.getApp().getBuffer();
            String textToMeasure = text;
            textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
            int loops = 0;
            while (out.width() + 2 * buffer > measureWidth() || out.height() + 2 * buffer > targetHeight()) {
                textPaint.setTextSize(textPaint.getTextSize() - 1);
                textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
                loops++;
                if (loops == 100) {
                    Log.e("Button.draw", "i am stuck in a loop!");
                    //TODO error reporter
                }
            }
            textToMeasure = "A";
            textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
            while (out.width() + 2 * buffer > measureWidth() || out.height() + 2 * buffer > targetHeight()) {
                textPaint.setTextSize(textPaint.getTextSize() - 1);
                textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
                loops++;
                if (loops == 100) {
                    Log.e("Button.draw", "i am stuck in a loop!");
                }
            }
            h = out.height();
        }
    }
}
