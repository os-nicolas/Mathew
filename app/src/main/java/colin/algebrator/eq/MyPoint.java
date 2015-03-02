package colin.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.Random;

import colin.example.algebrator.Algebrator;

/**
 * Created by Colin on 2/11/2015.
 */
public class MyPoint extends Point {
    public final float myWidth;
    public final float myHeight;


    public MyPoint(float myWidth, float myHeight){
        super();
        this.myHeight = myHeight;
        this.myWidth =  myWidth;
    }

    public boolean on(float x, float y) {
        // we use 1.75 instead of 2 to make the target a little bigger

        return x < this.x + Algebrator.getAlgebrator().getBuffer() + this.myWidth / 2
                && x > this.x - Algebrator.getAlgebrator().getBuffer() - this.myWidth / 2
                && y < this.y+ Algebrator.getAlgebrator().getBuffer()  + this.myHeight / 2
                && y > this.y - Algebrator.getAlgebrator().getBuffer() - this.myHeight / 2;
    }

    public void draw(Canvas canvas) {
        if (canvas != null) {
            RectF r = new RectF(x - myWidth / 2, y - myHeight / 2, x + myWidth / 2, y + myWidth / 2);
            Paint p = new Paint();
            p.setAlpha(0x40);
            canvas.drawRect(r, p);
        }
    }

    public boolean near(float x, float y) {
        float near = 100* Algebrator.getAlgebrator().getDpi();
        return x < this.x + near + this.myWidth / 2
                && x > this.x - near - this.myWidth / 2
                && y < this.y + near + this.myHeight / 2
                && y > this.y - near - this.myHeight / 2;
    }
}
