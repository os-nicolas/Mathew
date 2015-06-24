package cube.d.n.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

import cube.d.n.commoncore.BaseApp;

/**
 * Created by Colin on 6/24/2015.
 */
public class CircleView extends View {
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

    Paint bkgPaint;
    Paint textPaint;
    String text="5";

    public void setColors(String text,int circleColor,int textColor){
        this.text =text;
        bkgPaint = new Paint();
        bkgPaint.setColor(circleColor);
        bkgPaint.setAntiAlias(true);
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        Typeface dj = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        textPaint.setTypeface(dj);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas){
        int h = getHeight();
        int w = getWidth();

        canvas.drawCircle(h/2f,w/2f,Math.min(h/2f,w/2f),bkgPaint);

        Rect out = new Rect();
        //TODO scale by dpi
        float buffer = 0;//BaseApp.getApp().getBuffer();
        String textToMeasure = text;
        textPaint.setTextSize(80);
        textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
        while (out.width() + 2 * buffer > Math.min(h/2f,w/2f)/Math.sqrt(2) || out.height() + 2 * buffer > Math.min(h/2f,w/2f)/Math.sqrt(2)) {
            textPaint.setTextSize(textPaint.getTextSize() - 1);
            textPaint.getTextBounds(textToMeasure, 0, textToMeasure.length(), out);
        }
        int textH = out.height();
        int textW = out.width();

        canvas.drawText(text, (w/2f)- (textW / 2f), (h/2f) + (textH / 2f), textPaint);
    }

    public static Random r = new Random();

    public static int getBkgColor(int position) {
                return r.nextInt();
    }

    public static int getTextColor(int position) {
        return 0xff000000;
    }
}
