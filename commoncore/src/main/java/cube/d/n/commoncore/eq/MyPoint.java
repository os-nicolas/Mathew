package cube.d.n.commoncore.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Physical;
import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin on 2/11/2015.
 */
public class MyPoint extends Point implements Physical {
    public final float myWidth;
    public final float myHeight;


    public MyPoint(float myWidth, float myHeight){
        super();
        this.myHeight = myHeight;
        this.myWidth =  myWidth;
    }

    public boolean on(float x, float y,Equation equation) {
        // we use 1.9 instead of 2 to make the target a little bigger
        double scale =2;

        return x < this.x + (BaseApp.getApp().getBuffer(equation)/2) + (this.myWidth / scale)
                && x > this.x - (BaseApp.getApp().getBuffer(equation)/2) - (this.myWidth / scale)
                && y < this.y+ (BaseApp.getApp().getBuffer(equation)/2)  + (this.myHeight / scale)
                && y > this.y - (BaseApp.getApp().getBuffer(equation)/2) - (this.myHeight / scale);
    }

    public void draw(Canvas canvas,Equation equation) {
        if (canvas != null) {
            RectF r = new RectF(x - (myWidth / 2) - (BaseApp.getApp().getBuffer(equation)/2),
                    y - (myHeight / 2) - (BaseApp.getApp().getBuffer(equation)/2),
                    x + (myWidth / 2)+ (BaseApp.getApp().getBuffer(equation)/2),
                    y + (myHeight / 2)+ (BaseApp.getApp().getBuffer(equation)/2));
            Paint p = new Paint();
            p.setAlpha(0x40);
            canvas.drawRect(r, p);
        }
    }

    public boolean near(float x, float y) {
        float near =(float)( 75* BaseApp.getApp().getDpi() * (BaseApp.getApp().zoom + 1)/2); // we only sort scale by zoom
        return x < this.x + near + this.myWidth / 2
                && x > this.x - near - this.myWidth / 2
                && y < this.y + near + this.myHeight / 2
                && y > this.y - near - this.myHeight / 2;
    }

    public float distance(float x2, float y2) {
        return (float)Math.sqrt(((x2- this.x)*(x2- this.x))+((y2 - this.y)*(y2- this.y)));
    }

    @Override
    public float measureWidth() {
        return myWidth;
    }

    @Override
    public float measureHeight() {
        return myHeight;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
