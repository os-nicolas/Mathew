package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

/**
 * Created by Colin_000 on 7/16/2015.
 */
public class CircleDrawer {

    private static final float DIFF = 5;
    public String text="5";
    public String subText = "";
    public String supText ="";

    private float currentR = -1f;
    private float mySweep =0;
    private float precent=-1f;
    private View view;
    private Paint bkgPaint;
    private Paint textPaint;
    private Paint smallPaint;
    private Paint circlePaint;
    private Rect out;

//    private boolean noAinimate = true;

    public CircleDrawer(){}

    public void setColors(String text,int circleColor,int textColor){
        this.text =text;
        bkgPaint = new Paint();
        // precent = new Random().nextFloat();
        bkgPaint.setColor(circleColor);
        bkgPaint.setAntiAlias(true);
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(100);
        smallPaint = new Paint();
        smallPaint.setColor(textColor);
        smallPaint.setAntiAlias(true);
        //smallPaint.setDither(true);
        smallPaint.setTextSize(10 * BaseApp.getApp().getDpi() / BaseApp.getApp().scale);
        smallPaint.setTypeface(BaseApp.getApp().getDJVL());
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setColor(0xffAAAAAA);
        textPaint.setTypeface(BaseApp.getApp().getDJVL());
        out = new Rect();
    }

    public void setPrecent(float precent){
        if (precent != this.precent) {
            this.precent = precent;
            if (view != null) {
                done = false;
                startedAt = -1;
                targetR = -1;
                startedAtSweep = mySweep;
                startedAtR = currentR;
                view.invalidate();
            }
        }
    }

    public void setSubText(String subText){
        this.subText = subText;
        if (view != null) {
            view.invalidate();
        }
    }

    public void setView(View view){
        this.view = view;
    }


    public void dontAnimate(){
        startedAt = System.currentTimeMillis() - (sweepEnd+1) ;
    }

    private long startedAt = -1;
    long growEnd = 500;
    float startedAtSweep = 0;
    float startedAtR = -1;
    long sweepStart = 500;
    long sweepEnd = 1000;
    private float targetR=-1;
    private float sweepR;
    private float targetSweep;
    boolean done = false;

    public void draw(Canvas canvas, float tr, int cx,int cy){
        long now =  System.currentTimeMillis();
        if (startedAt == -1){
            startedAt = now;
        }

        if (targetR == -1){
            if (precent!= -1){
                targetR = tr-DIFF;
                sweepR = tr;
            }else{
                targetR = tr;
            }
            if (startedAtR ==-1) {
                startedAtR = .6f * targetR;
            }
        }


        targetSweep = precent*360;

        currentR = startedAtR  + (Math.min((float)(now - (startedAt))/(float)(growEnd),1)*(targetR - startedAtR));

        float buffer = BaseApp.getApp().getBuffer();
        Paint stupidPaint = new Paint();
        stupidPaint.setColor( BaseApp.colorFade(bkgPaint.getColor(),0xffffffff ,7));//textPaint.getColor()

        if (precent!= -1 && now> startedAt+ sweepStart) {

            canvas.drawCircle(cx, cy, currentR, stupidPaint);

            RectF oval = new RectF(cx - sweepR, cy - sweepR, cx+ sweepR, cy + sweepR);
            mySweep = startedAtSweep+((targetSweep - startedAtSweep)*Math.min((float)(now - (startedAt+ sweepStart))/(float)(sweepEnd - sweepStart),1));

            canvas.drawArc(oval, 0, mySweep, true, bkgPaint);

        }else{
            canvas.drawCircle(cx,cy, currentR , stupidPaint);
        }


        textPaint.getTextBounds(text, 0, text.length(), out);
        while (out.width() + (2* buffer) > 2*targetR/Math.sqrt(2) || out.height() + (2*buffer) > 2*targetR/Math.sqrt(2)) {
            textPaint.setTextSize(textPaint.getTextSize()*.9f);
            textPaint.getTextBounds(text, 0, text.length(), out);
        }
        int textH = out.height();
        float textW2 = textPaint.measureText(text);




        canvas.drawText(text,cx- (textW2 / 2f), cy + (textH / 2f), textPaint);
        if (!subText.equals("") && (!subText.equals("SOLVED") || now > startedAt + sweepEnd)){
            float wSmall = smallPaint.measureText(subText);

            smallPaint.getTextBounds(subText, 0, subText.length(), out);

            int hSmall = out.height();
            float padding = 4*BaseApp.getApp().getDpi();

            canvas.drawText(subText, cx- (wSmall / 2f), cy + (textH / 2f) + (hSmall) + padding, smallPaint);
        }
        if (!supText.equals("")){
            float wSmall = smallPaint.measureText(supText);

            smallPaint.getTextBounds(supText, 0, supText.length(), out);

            int hSmall = out.height();
            float padding = 4*BaseApp.getApp().getDpi();

            canvas.drawText(supText, cx- (wSmall / 2f), cy - (textH / 2f) - padding, smallPaint);
        }


        if ( now > startedAt+ growEnd && now > startedAt + sweepEnd){
            done = true;
        }
    }


    public boolean notDone() {
        return !done;
    }
}
