package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Physical;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 6/29/2015.
 */
public abstract class Line implements Physical {

    public final Main owner;
    private float x;
    private float y;

    public float  getX(){
        return x;
    }
    public float  getY(){
        return y;
    }

    public Line(Main owner){
        this.owner=owner;
    }

    /**
     * @param canvas
     * @param top
     * @param left
     * @param paint
     */
    public void draw(Canvas canvas, float top, float left, Paint paint){
        updateLocation(top,left);
        innerDraw(canvas,top,left,paint);
    }

    public  static float getBuffer(){
        return (float) (20* BaseApp.getApp().getDpi()*BaseApp.getApp().zoom);
    }

    public abstract KeyBoard getKeyboad();


    protected abstract void innerDraw(Canvas canvas, float top, float left, Paint paint);

    public abstract float requestedMaxX();

    public abstract float requestedMinX();

    // return true to claim the event for this type of item
    public boolean onTouch(MotionEvent event){
        return false;
    }

    private void updateLocation(float top, float left) {
        x = left + measureWidth()/2f;
        y = top + measureHeight()/2f;
    }

    @Override
    public float measureWidth() {
        return owner.width;
    }
}
