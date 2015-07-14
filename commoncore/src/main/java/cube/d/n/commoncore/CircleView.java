package cube.d.n.commoncore;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

import cube.d.n.commoncore.BaseApp;

/**
 * Created by Colin on 6/24/2015.
 */
public class CircleView extends View {

    float currentR = -1f;
    float mySweep =0;
    private float precent=-1f;

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setColors("5",0xffff0000,0xff000000);
    }

    public CircleView(Context context) {
        super(context);
        setColors("5",0xffff0000, 0xff000000);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColors("5",0xffff0000,0xff000000);
    }

    public void setPrecent(float precent){
        if (precent != this.precent) {
            this.precent = precent;
            invalidate();
        }
    }

    Paint bkgPaint;
    Paint textPaint;
    Paint smallPaint;
    Paint circlePaint;
    String text="5";
    Rect out;
    private String subText = "";

    public void setSubText(String subText){
        this.subText = subText;
        invalidate();
    }

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
        smallPaint.setTextSize(7*BaseApp.getApp().getDpi()/BaseApp.getApp().scale);
        smallPaint.setTypeface(BaseApp.getApp().getDJVL());
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(0xffAAAAAA);
        Typeface dj = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        textPaint.setTypeface(dj);
        out = new Rect();
        //invalidate();
    }



    @Override
    public void onDraw(Canvas canvas){
        int h = getHeight();
        int w = getWidth();

        float targetR = Math.min(h/2f,w/2f);
        float targetSweep = precent*360;

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

            canvas.drawCircle(h / 2f, w / 2f, currentR-3, stupidPaint);

            if (currentR == targetR) {

                RectF oval = new RectF(0, 0, w, h);
                mySweep = ((mySweep * BaseApp.getApp().getRate() * 2) + targetSweep) / (1 + (BaseApp.getApp().getRate() * 2));


                canvas.drawArc(oval, 0, mySweep, true, bkgPaint);
            }
//            Paint reallystupidPaint = new Paint();
//            reallystupidPaint.setColor( BaseApp.colorFade(bkgPaint.getColor(), textPaint.getColor(),2));
//
//            canvas.drawCircle(h/2f,w/2f,currentR-6,reallystupidPaint);
        }else{
            canvas.drawCircle(h / 2f, w / 2f, currentR , stupidPaint);
        }


        textPaint.getTextBounds(text, 0, text.length(), out);
        while (out.width() + (2* buffer) > Math.min(h,w)/Math.sqrt(2) || out.height() + (2*buffer) > Math.min(h,w)/Math.sqrt(2)) {
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
        canvas.drawText(text, (w/2f)- (textW2 / 2f), (h/2f) + (textH / 2f), textPaint);
        if (!subText.equals("")){
            float wSmall = smallPaint.measureText(subText);

            smallPaint.getTextBounds(subText, 0, subText.length(), out);

            int hSmall = out.height();
            float padding = 3*BaseApp.getApp().getDpi();

            canvas.drawText(subText, (w/2f)- (wSmall / 2f), (h/2f) + (textH / 2f) + (hSmall) + padding, smallPaint);
        }

        currentR = ((currentR*BaseApp.getApp().getRate()*2)+targetR)/(1+(BaseApp.getApp().getRate()*2));
        if (currentR != targetR || (precent!= -1 &&targetSweep != mySweep)){
            invalidate();
        }
    }

    public static Random r = new Random();

    public static int getBkgColor(int position) {
        if (position % 7 ==0){
            return 0xffe0d7d7;
        }else if (position % 7 ==1){
            return 0xffff0000;
        }else if (position % 7 ==2){
            return 0xff470a0a;
        }else if (position % 7 ==3){
            return 0xff291f1f;
        }else if (position % 7 ==4){
            return 0xffa02d2d;
        }else if (position % 7 ==5){
            return 0xffb5a8a8;
        }else if (position % 7 ==6){
            return 0xff2c0c0c;
        }
        Log.e("getBkgColor","this should never happen! ");
        return 0xff000000;
    }

    public static int getTextColor(int position) {
        if (position % 7 ==0){
            return 0xff420d0d;
        }else if (position % 7 ==1){
            return 0xff000000;
        }else if (position % 7 ==2){
            return 0xffffffff;
        }else if (position % 7 ==3){
            return 0xffff0000;
        }else if (position % 7 ==4){
            return 0xff250000;
        }else if (position % 7 ==5){
            return 0xff7b1c1c;
        }else if (position % 7 ==6){
            return 0xfff4e8e8;
        }
        Log.e("getTextColor","this should never happen! ");
        return 0xff000000;
    }
}
