package colin.example.algebrator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import colin.algebrator.eq.MyPoint;
import colin.example.algebrator.Animation;

/**
 * Created by Colin on 2/19/2015.
 */
public class Pop extends Animation {
    float targetRadius = 32*Algebrator.getAlgebrator().getDpi();
    long runTime = 180;
    long startedAt;
    int startAlpha = 0x90;
    public MyPoint myPoint;

    public Pop(MyPoint myPoint,SuperView owner) {
        super(owner);
        this.startedAt = System.currentTimeMillis();
        this.myPoint = myPoint;
    }

    @Override
    public void draw(Canvas canvas) {
        long now = System.currentTimeMillis();
        if ((now - startedAt) < runTime) {
            float currentAlpha = (float)(((double)startAlpha)*((float)(runTime -(now - startedAt))/(float)runTime));
            Paint p = new Paint();
            p.setColor(Algebrator.getAlgebrator().lightColor);
            p.setAlpha((int) currentAlpha);
            float rad = 25*Algebrator.getAlgebrator().getDpi()+((targetRadius)*((now - startedAt)/(float)runTime));
            canvas.drawCircle(myPoint.x, myPoint.y, rad, p);
            //Log.i("I tried", "x: " + myPoint.x + " y: " + myPoint.y + " alpha: " + (int) currentAlpha + " rad: " + rad);
        }else{
            remove();
        }
    }
}
