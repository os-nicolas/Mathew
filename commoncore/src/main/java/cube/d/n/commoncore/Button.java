package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.view.MotionEvent;


import cube.d.n.commoncore.Action.Action;

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
    Action myAction;
    boolean hover = false;

    public Button() {

    }

    public Button( String text, Action myAction) {
        super();
        this.myAction = myAction;
        this.text = text;
        this.bkgPaint = new TextPaint(BaseApp.getApp().bkgPaint);
        this.highlightColor = BaseApp.getApp().darkColor;
        targetBkgColor = BaseApp.getApp().lightColor;
        this.textPaint = new TextPaint(BaseApp.getApp().textPaint);
        //TODO scale by dpi
        //TODO does not work at all
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


        float smaller = 3 * BaseApp.getApp().getDpi();
        RectF r2 = new RectF(left() + smaller, top() + smaller, right() - smaller, bottom() - smaller);
        bkgPaint.setAlpha(p.getAlpha());
        canvas.drawRoundRect(r2, BaseApp.getApp().getCornor(), BaseApp.getApp().getCornor(), bkgPaint);

        Rect out = new Rect();
        //TODO scale by dpi
        float buffer = BaseApp.getApp().getBuffer();
        String textToMeasure = text;
        textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
        while (out.width() + 2 * buffer > measureWidth() || out.height() + 2 * buffer > targetHeight()) {
            textPaint.setTextSize(textPaint.getTextSize() - 1);
            textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
        }
        textToMeasure = "A";
        textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
        while (out.width() + 2 * buffer > measureWidth() || out.height() + 2 * buffer > targetHeight()) {
            textPaint.setTextSize(textPaint.getTextSize() - 1);
            textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
        }
        float h = out.height();


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
        canvas.drawText(text, getX(), getY() + h / 2, textPaint);
    }

    public void draw(Canvas canvas) {
        draw(canvas,new Paint());
    }

    protected float targetHeight() {
        return measureHeight();
    }

    protected float top() {
        return top * canvasHeight;
    }

    protected float left() {
        return left * canvasWidth;
    }

    protected float bottom() {
        return bottom * canvasHeight;
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
            bkgPaint.setColor(BaseApp.getApp().lightColor);
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

}
