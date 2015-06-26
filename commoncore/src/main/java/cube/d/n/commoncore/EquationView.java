package cube.d.n.commoncore;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin_000 on 6/23/2015.
 */
public class EquationView extends View {

    private Equation myEq;

    public EquationView(Context context) {
        super(context);
    }

    public EquationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EquationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setEquation(Equation e){
        myEq = e;
        invalidate();
    }

    float startZoom = 1;

    public void setEquation(Equation e, float startZoom){
        myEq = e;
        this.startZoom = startZoom;
        invalidate();

    }

    @Override
    public void onDraw(Canvas canvas){
        if (myEq!= null){
            int h =getHeight();
            int w = getWidth();

            float buffer = 0;//BaseApp.getApp().getBuffer();
            myEq.overWriteZoom(startZoom);
            while (myEq.measureWidth() + 2 * buffer > w ||myEq.measureHeight() + 2 * buffer > h) {
                myEq.overWriteZoom(myEq.getMyZoom()*.9f);
            }
            myEq.draw(canvas,myEq.measureWidth()/2f,h/2f);
        }
    }

    public void setFont(Typeface dj){
        myEq.getPaint().setTypeface(dj);
    }

    public void setColor(int color){
        myEq.setColor(color);
    }
}
