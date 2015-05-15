package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import cube.d.n.commoncore.Animation;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Physical;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.BitmapBacked;
import cube.d.n.commoncore.GS;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
* Created by Colin_000 on 5/7/2015.
*/
public abstract class Line extends BitmapBacked implements Physical {

    protected Line getThis(){return this;}
    public final GS<Equation> stupid  = new GS<Equation>(){


    @Override
    public void set(Equation newStupid){
            super.set(newStupid);
            if (get().lastPoint.size() == 0) {
                get().updateLocation();
            }
            get().parent = null;
            if (!get().owner.equals(getThis())){
                get().updateOwner(getThis());
                get().deepNeedsUpdate();
            }
            if ( getThis() instanceof InputLine){
                get().setMyTextScale(1.2f);
            }
        }
    };
    public final Main owner;

    public  static float getBuffer(){
        return (float) (20* BaseApp.getApp().getDpi()*BaseApp.getApp().zoom);
    }

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
        return stupid.get().measureHeight() + 2 *  getBuffer();
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

    public abstract float requestedMaxX();

    public abstract float requestedMinX();


    public enum pm  {WRITE,BOTH,SOLVE}
}
