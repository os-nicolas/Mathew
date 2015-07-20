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

    public CircleDrawer circleDrawer = new CircleDrawer();



    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        circleDrawer.setColors("5",0xffff0000,0xff000000);
        circleDrawer.setView(this);
    }

    public CircleView(Context context) {
        super(context);
        circleDrawer.setColors("5",0xffff0000, 0xff000000);
        circleDrawer.setView(this);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        circleDrawer.setColors("5",0xffff0000,0xff000000);
        circleDrawer.setView(this);
    }




    @Override
    public void onDraw(Canvas canvas){

        circleDrawer.draw(canvas,Math.min(getHeight()/2f,getWidth()/2f),getWidth()/2,getHeight()/2);

        if (circleDrawer.notDone()){
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
