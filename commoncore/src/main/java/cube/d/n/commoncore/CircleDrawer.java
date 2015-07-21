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
    private float targetR;
    private float targetSweep;

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
        smallPaint.setTextSize(8*BaseApp.getApp().getDpi()/BaseApp.getApp().scale);
        smallPaint.setTypeface(BaseApp.getApp().getDJVL());
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(0xffAAAAAA);
        textPaint.setTypeface(BaseApp.getApp().getDJVL());
        out = new Rect();
    }

    public void setPrecent(float precent){
        if (precent != this.precent) {
            this.precent = precent;
            if (view != null) {
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

    public void draw(Canvas canvas, float tr, int cx,int cy){

        targetR = tr;
        targetSweep = precent*360;

        if (Math.abs(targetR-currentR)< .1){
            currentR = targetR;
        }

        float buffer = BaseApp.getApp().getBuffer();
        if (currentR ==-1){
            currentR = targetR- buffer;
        }

        Paint stupidPaint = new Paint();
        stupidPaint.setColor( BaseApp.colorFade(bkgPaint.getColor(), textPaint.getColor(),7));

        if (precent!= -1) {

            canvas.drawCircle(cx, cy, currentR-5, stupidPaint);

            if (currentR == targetR) {

                RectF oval = new RectF(cx - targetR, cy - targetR, cx+ targetR, cy + targetR);
                mySweep = ((mySweep * BaseApp.getApp().getRate() * 2) + targetSweep) / (1 + (BaseApp.getApp().getRate() * 2));


                canvas.drawArc(oval, 0, mySweep, true, bkgPaint);
            }
//            Paint reallystupidPaint = new Paint();
//            reallystupidPaint.setColor( BaseApp.colorFade(bkgPaint.getColor(), textPaint.getColor(),2));
//
//            canvas.drawCircle(h/2f,w/2f,currentR-6,reallystupidPaint);
        }else{
            canvas.drawCircle(cx,cy, currentR , stupidPaint);
        }


        textPaint.getTextBounds(text, 0, text.length(), out);
        while (out.width() + (2* buffer) > 2*targetR/Math.sqrt(2) || out.height() + (2*buffer) > 2*targetR/Math.sqrt(2)) {
            textPaint.setTextSize(textPaint.getTextSize()*.9f);
            textPaint.getTextBounds(text, 0, text.length(), out);
        }
        int textH = out.height();
        int textW = out.width();
        float textW2 = textPaint.measureText(text);

        //Log.d("do we not understand width?","measureText: "+ textW2 +" out.width: "+ textW);

//        Paint p = new Paint();
//        p.setColor(Color.WHITE);
//        Rect r = new Rect((int)((w/2f)- (textW2 / 2f)), (int)((h/2f) - (textH / 2f)),(int)((w/2f)+ (textW2 / 2f)), (int)((h/2f) + (textH / 2f)));
//        canvas.drawRect(r,p);
        canvas.drawText(text,cx- (textW2 / 2f), cy + (textH / 2f), textPaint);
        if (!subText.equals("")){
            float wSmall = smallPaint.measureText(subText);

            smallPaint.getTextBounds(subText, 0, subText.length(), out);

            int hSmall = out.height();
            float padding = 3*BaseApp.getApp().getDpi();

            canvas.drawText(subText, cx- (wSmall / 2f), cy + (textH / 2f) + (hSmall) + padding, smallPaint);
        }
        if (!supText.equals("")){
            float wSmall = smallPaint.measureText(supText);

            smallPaint.getTextBounds(supText, 0, supText.length(), out);

            int hSmall = out.height();
            float padding = 3*BaseApp.getApp().getDpi();

            canvas.drawText(supText, cx- (wSmall / 2f), cy - (textH / 2f) - padding, smallPaint);
        }

        currentR = ((currentR*BaseApp.getApp().getRate()*2)+targetR)/(1+(BaseApp.getApp().getRate()*2));
    }

    public boolean notDone() {
        return currentR != targetR || (precent!= -1 &&targetSweep != mySweep);
    }
}
