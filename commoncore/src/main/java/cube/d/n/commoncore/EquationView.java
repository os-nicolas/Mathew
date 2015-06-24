package cube.d.n.commoncore;

import android.content.Context;
import android.graphics.Canvas;
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

    @Override
    public void onDraw(Canvas canvas){
        if (myEq!= null){
            int h =getHeight();
            int w = getWidth();

            float buffer = BaseApp.getApp().getBuffer();
            myEq.overWriteZoom(1.5f);
            while (myEq.measureWidth() + 2 * buffer > w ||myEq.measureHeight() + 2 * buffer > h) {
                myEq.overWriteZoom(myEq.getMyZoom()*.9f);
            }
            myEq.draw(canvas,myEq.measureWidth()/2f,h/2f);
        }
    }
}
