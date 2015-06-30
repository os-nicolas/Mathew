package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import cube.d.n.commoncore.Animation;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Physical;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.GS;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
* Created by Colin_000 on 5/7/2015.
*/
public abstract class EquationLine extends Line  {

    protected EquationLine getThis(){return this;}
    public final GS<Equation> stupid  = new GS<Equation>(){


    @Override
    public void set(Equation newStupid){
            super.set(newStupid);
            if (get().getDrawnAtY() == -1) {
                get().updateLocation();
            }
            get().parent = null;
            if (!get().owner.equals(getThis())){
                get().updateOwner(getThis());
                get().deepNeedsUpdate();
            }
            if ( getThis() instanceof InputLine){
                get().setMyTextScale(1.1f);
            }
        }
    };

    public EquationLine(Main owner){
        super(owner);
    }

    @Override
    public float measureHeight() {
        return stupid.get().measureHeight() + 2 *  getBuffer();
    }


    public abstract pm parentThesisMode();

    public void removeAnimation(Animation animation) {}

    protected boolean in(MotionEvent event) {
        return event.getY() < getY() + measureHeight()/2f && event.getY() > getY() - measureHeight()/2f;
    }

    public abstract float requestedMaxX();

    public abstract float requestedMinX();

    public InputLine getImputLine(){
        int at = owner.getLineIndex(this);

        for (;at< owner.getLinesSize();at--){
            if (owner.getLine(at) instanceof InputLine && !(owner.getLine(at) instanceof BothSidesLine) ){
                return (InputLine)owner.getLine(at);
            }
        }
        Log.e("getImputLine", "could not find input line");
        return null;
    }

    /**
     * @param canvas
     * @param top
     * @param left
     * @param paint
     */
    public  void draw(Canvas canvas, float top, float left, Paint paint){
        super.draw(canvas,top,left,paint);
        if (ntc){
            center();
        }
    }


    private boolean ntc =false;
    public void needsToCenter(){
        ntc = true;
    }

    public void center(){
        ntc = false;
        if (stupid.get() instanceof EqualsEquation){
            if  (((EqualsEquation) stupid.get()).measureLeft() + ((EqualsEquation) stupid.get()).measureRight() < owner.width){
                if (((EqualsEquation) stupid.get()).getCenter()  -((EqualsEquation) stupid.get()).measureLeft() <getBuffer() ){
                    getImputLine().toAddToOffsetX(-(((EqualsEquation) stupid.get()).getCenter()  -((EqualsEquation) stupid.get()).measureLeft()));
                }
                if (((EqualsEquation) stupid.get()).getCenter()  +((EqualsEquation) stupid.get()).measureRight() >owner.width ){
                    getImputLine().toAddToOffsetX(owner.width- (((EqualsEquation) stupid.get()).getCenter() +((EqualsEquation) stupid.get()).measureRight()));
                }
            }else{
                if (((EqualsEquation) stupid.get()).getCenter()  -((EqualsEquation) stupid.get()).measureLeft() > getBuffer() ){
                    getImputLine().toAddToOffsetX(getBuffer() - (((EqualsEquation) stupid.get()).getCenter()  -((EqualsEquation) stupid.get()).measureLeft()));
                }
                if (((EqualsEquation) stupid.get()).getCenter()  +((EqualsEquation) stupid.get()).measureRight() < owner.width-getBuffer() ){
                    getImputLine().toAddToOffsetX((owner.width-getBuffer())-(((EqualsEquation) stupid.get()).getCenter()  +((EqualsEquation) stupid.get()).measureRight()));
                }
            }
        }else{
            if  (stupid.get().measureWidth() < owner.width){
                if (stupid.get().getX() - (stupid.get().measureWidth()/2f) <0 ){
                    getImputLine().toAddToOffsetX(-(stupid.get().getX() - (stupid.get().measureWidth()/2f)));
                }
                if (stupid.get().getX() + (stupid.get().measureWidth()/2f) >owner.width ){
                    getImputLine().toAddToOffsetX(owner.width- (stupid.get().getX() + (stupid.get().measureWidth()/2f)));
                }
            }else{
                if (stupid.get().getX()  -(stupid.get().measureWidth()/2f) > getBuffer() ){
                    getImputLine().toAddToOffsetX(getBuffer() - (stupid.get().getX()  -(stupid.get().measureWidth()/2f)));
                }
                if (stupid.get().getX()  +(stupid.get().measureWidth()/2f) < owner.width-getBuffer() ){
                    getImputLine().toAddToOffsetX((owner.width-getBuffer())-(stupid.get().getX()  +(stupid.get().measureWidth()/2f)));
                }

            }
        }
    }

    public enum pm  {WRITE,BOTH,SOLVE}
}
