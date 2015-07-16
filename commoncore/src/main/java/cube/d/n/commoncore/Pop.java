package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;


import java.util.ArrayList;

import cube.d.n.commoncore.eq.MyPoint;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin on 2/19/2015.
 */
public class Pop extends Animation {
    float targetRadius = 32* BaseApp.getApp().getDpi();
    long runTime = 180;
    long startedAt;
    int startAlpha = 0x90;
    public Point myPoint;
    int color = BaseApp.getApp().lightColor;

    public Pop(Point myPoint,ArrayList<Animation> holder) {
        super(holder);
        init(myPoint);

    }

    public Pop(Point myPoint) {
        super(null);
        init(myPoint);
    }

    private void init(Point myPoint) {
        this.startedAt = System.currentTimeMillis();
        this.myPoint = myPoint;
    }

    public Pop widthColor(int color){
        this.color = color;
        return this;
    }

    @Override
    public void draw(Canvas canvas) {
        long now = System.currentTimeMillis();
        if ((now - startedAt) < runTime) {
            float currentAlpha = (float)(((double)startAlpha)*((float)(runTime -(now - startedAt))/(float)runTime));
            Paint p = new Paint();
            p.setColor(color);
            p.setAlpha((int) currentAlpha);
            float rad = 25*BaseApp.getApp().getDpi()+((targetRadius)*((now - startedAt)/(float)runTime));
            canvas.drawCircle(myPoint.x, myPoint.y, rad, p);
            //Log.i("I tried", "x: " + myPoint.x + " y: " + myPoint.y + " alpha: " + (int) currentAlpha + " rad: " + rad);
        }else{
            remove();
        }
    }
}
