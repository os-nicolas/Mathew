package cube.d.n.commoncore.v2.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import cube.d.n.commoncore.Animation;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Physical;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.v2.BitmapBacked;
import cube.d.n.commoncore.v2.GS;
import cube.d.n.commoncore.v2.Main;
import cube.d.n.commoncore.v2.keyboards.KeyBoard;

/**
* Created by Colin_000 on 5/7/2015.
*/
public abstract class Line extends BitmapBacked implements Physical {

    protected Line getThis(){return this;}
    public final GS<Equation> stupid  = new GS<Equation>(){


    @Override
    public void set(Equation newStupid){
            super.set(newStupid);
            get().parent = null;
            if (!get().owner.equals(getThis())){
                get().updateOwner(getThis());
                get().deepNeedsUpdate();
            }
        }
    };
    public final Main owner;
    public float buffer =20* BaseApp.getApp().getDpi();

    private float x;
    private float y;

    public Line(Main owner){
        this.owner=owner;
    }


    public abstract KeyBoard getKeyboad();

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

    protected boolean in(MotionEvent event) {
        return event.getY() < getY() + measureHeight()/2f && event.getY() > getY() - measureHeight()/2f;
    }


    public enum pm  {WRITE,BOTH,SOLVE}
}
