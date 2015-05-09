package cube.d.n.commoncore.v2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import cube.d.n.commoncore.Animation;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Physical;
import cube.d.n.commoncore.eq.any.Equation;

/**
* Created by Colin_000 on 5/7/2015.
*/
public abstract class Line extends BitmapBacked implements Physical {


    public final GS<Equation> stupid  = new GS<Equation>();
    public final  Main owner;
    public float buffer =20* BaseApp.getApp().getDpi();

    private float x;
    private float y;

    public Line(Main owner){
        this.owner=owner;
    }

    @Override
    public float measureWidth() {
        return owner.width;
    }

    @Override
    public float measureHeight() {
        return stupid.get().measureHeight() + 2 * buffer;
    }


    // return true to claim the event for this type of item
    public boolean onTouch(MotionEvent event){
        return false;
    }


    /**
     * don't override this!
     * @param canvas
     * @param top
     * @param left
     * @param paint
     */
    public void draw(Canvas canvas, float top, float left, Paint paint){
        updateLocation(top,left);
        innerDraw(canvas,top,left,paint);
    }

    protected abstract void innerDraw(Canvas canvas, float top, float left, Paint paint);

    private void updateLocation(float top, float left) {
        x = left + measureWidth()/2f;
        y = top + measureHeight()/2f;
    }

    public float  getX(){
        return x;
    }
    public float  getY(){
        return y;
    }

    public abstract pm parentThesisMode();

    public void removeAnimation(Animation animation) {

    }

    public enum pm  {WRITE,BOTH,SOLVE}
}
