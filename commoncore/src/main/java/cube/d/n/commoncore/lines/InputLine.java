package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;


import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.HasHeaderLine;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.EquationDis;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Selects;
import cube.d.n.commoncore.TouchMode;
import cube.d.n.commoncore.keyboards.CalcInputKeyboard;
import cube.d.n.commoncore.keyboards.HelperInputKeyboard;
import cube.d.n.commoncore.keyboards.InputKeyboard;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.keyboards.PracInputKeyboard;
import cube.d.n.commoncore.keyboards.SimpleCalcKeyboard;

/**
* Created by Colin_000 on 5/7/2015.
*/
// this is a somewhat confused class
// it does two things
// it's a header and it handles inputs

public class InputLine extends EquationLine implements Selects, HasHeaderLine {

    public enum App {CALC,HELP, INLINE, PRAC}


    private final PlaceholderEquation selected;


    public InputLine(Main owner) {
        super(owner);
        selected = new PlaceholderEquation(this);
        initEq();
    }

    protected InputKeyboard myKeyBoard = null;
    @Override
    public KeyBoard getKeyboad() {
        if (myKeyBoard == null){
            myKeyBoard = owner.modeController.getInputKeyboard(this);

        }
        return myKeyBoard;
    }

    @Override
    public void setKeyBoard(KeyBoard k) {
        myKeyBoard = (InputKeyboard)k;
    }

    public void initEq() {
        stupid.set(new WritingEquation(this));
        stupid.get().add(selected);
    }

    TouchMode myMode;

    @Override
    public boolean onTouch(MotionEvent event) {
        if (event.getPointerCount() ==1) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (in(event)) {
                    if (stupid.get().nearAny(event.getX(), event.getY())) {
                        resolveSelected(event);
                        myMode = TouchMode.SELECT;
                    } else {
                        myMode = TouchMode.NOPE;
                    }
                } else {
                    myMode = TouchMode.NOPE;
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (myMode == TouchMode.SELECT) {
                    resolveSelected(event);

                    float slideBuffer = getBuffer();

                    if ( event.getX() - (selected.measureWidth()/2f) <slideBuffer ){
                        owner.toAddToOffsetX(this,(slideBuffer-(event.getX() - (selected.measureWidth()/2f)))/10f);
                    }

                    if ( event.getX() + (selected.measureWidth()/2f) > owner.width -slideBuffer ){
                        owner.toAddToOffsetX(this,((owner.width -slideBuffer)-(event.getX() + (selected.measureWidth()/2f) ))/10f);
                    }

                    if ( event.getY() - selected.measureHeightUpper() <slideBuffer ){
                        owner.toAddToOffsetY((slideBuffer-( event.getY() - selected.measureHeightUpper()))/10f);
                    }

                    if (event.getY() +selected.measureHeightLower() > owner.height -owner.keyBoardManager.get().measureHeight() -slideBuffer){
                        owner.toAddToOffsetY(((owner.height -owner.keyBoardManager.get().measureHeight() -slideBuffer)-(event.getY() + selected.measureHeightLower() ))/10f);
                    }
                    selected.goDark();
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (myMode == TouchMode.SELECT) {
                    resolveSelected(event);
                }
            }
        }else{
            if (myMode == TouchMode.SELECT) {
                resolveSelected(event);
            }
            myMode = TouchMode.NOPE;

        }
        if (myMode == TouchMode.NOPE) {
            return false;
        }else{
            return  true;
        }
    }

    public void removeSelected() {
            if (!(selected.parent.size() == 1 && selected.parent.parent == null)) {
                Equation oldEq = selected;
                oldEq.remove();
            }
    }

    private void resolveSelected(MotionEvent event) {
// now we need to figure out what we are selecting
        // find the least commond parent

        int currentBkgAlpha = 0x00;
        if (selected != null) {
            currentBkgAlpha = selected.bkgAlpha;
        }




        // if it's an action up
        // and it was near the left of stupid
        ArrayList<EquationDis> closest = stupid.get().closest(
                event.getX(), event.getY());

        Equation lcp = closest.get(0).equation;

        // TODO 100 to var scale by dpi
        //float minDis = 100 * Algebrator.getAlgebrator().getDpi();
        //if (Math.abs(event.getY() - lcp.y) < minDis) {
        if ((lcp.parent.size() == 1 && lcp.parent.parent == null)) {
            lcp.setSelected(true);
        } else {
            removeSelected();
            int cIndex = 1;
            while (lcp instanceof PlaceholderEquation) {
                lcp = closest.get(cIndex).equation;
                cIndex++;
            }


            // the the lcp is the left or right end of something we might want to select it's parent
            Equation current = lcp;

            boolean left = event.getX() < lcp.x;
            int depth = 0;
            // find how many layor deep we are
            while (current.parent != null &&
                    (current.parent.indexOf(current) == (left ? 0 : current.parent.size() - 1)||
                    current.parent instanceof BinaryEquation)) {
                // we don't count binary eq because they are fixed size
                if (!(current.parent instanceof BinaryEquation)) {
                    depth++;
                }
                current = current.parent;
            }
            // we really should figure how much space we have to work with so we can divide it up

        Log.d("depth",depth +"");

            if (depth != 0) {

                //TODO
                // I am not sure I should use 100
                // it is wierd to have to drag different distances for different embedednesse
                // yuk tho I probably want to use inches
                float distance = Math.min(100*BaseApp.getApp().getScale(),BaseApp.getApp().getScale()*25 *depth);//100 * BaseApp.getApp().getDpi() + (lcp.measureWidth() / 2f);

                // this means use left or right
                Equation next = (left ? lcp.nonDivLeft() : lcp.nonDivRight());
                if (next != null) {
                    distance = Math.abs(lcp.x - next.x) / 2;
                }

                float each = distance / ((float) (depth + 1));
                int num = (int) Math.floor(Math.min(Math.abs(lcp.x - event.getX()), distance) / each);

                if (num == depth + 1) {
                    num = depth;
                }

                Log.d("num",num +"");

                current = lcp;
                while (num != 0) {
                    if (!(current.parent instanceof BinaryEquation)) {
                        num--;
                    }
                    current = current.parent;
                }
                lcp = current;
            }


            // insert a Placeholder to the left of everything
            Equation toSelect = selected;
            toSelect.bkgAlpha = currentBkgAlpha;
            toSelect.x = event.getX();
            toSelect.y = event.getY();
            // add toSelect left of lcp
            if (lcp instanceof WritingEquation) {
                lcp.add((left ? 0 : lcp.size()), toSelect);
            } else if (lcp.parent instanceof WritingEquation) {
                int at = lcp.parent.indexOf(lcp);
                lcp.parent.add(at + (left ? 0 : 1), toSelect);
            } else {
                Log.e("InputLine.resolveSelected","we should never hit this case!");

                Equation oldEq = lcp;
                Equation holder = new WritingEquation(this);
                oldEq.replace(holder);
                if (left) {
                    holder.add(toSelect);
                    holder.add(oldEq);
                } else {
                    holder.add(oldEq);
                    holder.add(toSelect);
                }
                toSelect.setSelected(true);
            }
        }

        Log.d("current", stupid.get().toString());

        //}
        if (selected != null) {
            if (event.getAction() == MotionEvent.ACTION_UP || event.getPointerCount() != 1) {
                selected.drawBkg = false;
            } else {
                selected.drawBkg = true;
                selected.goDark();
            }
        }

        return;
    }

    public Equation left() {
        return selected.left();
    }

    private int currentAlpha= 0x00;
    private float lastLeft = -1;// measureWidth() / 2f;
    private float lastRight =-1;// measureWidth() / 2f;
    double lastZoom = BaseApp.getApp().zoom;
    @Override
    public void innerDraw(Canvas canvas, float top, float left, Paint paint) {



//        Rect r = new Rect((int)0,(int)top,(int)(0+ measureWidth()),(int)(top+measureHeight()));
//        Paint p = new Paint();
//        p.setAlpha(paint.getAlpha());
//        p.setColor(BaseApp.getApp().darkColor);
//        canvas.drawRect(r,p);

        int rate = BaseApp.getApp().getRate();

        stupid.get().setAlpha(paint.getAlpha());

        float realLeft = Math.min(0,Math.max(left,owner.width - stupid.get().measureWidth() - 2* getBuffer()));

        float eqCenterX=realLeft +(  stupid.get().measureWidth() / 2f) + getBuffer();



        if (!owner.lastLine().equals(this)) {

            float liney = top +  getBuffer()*3 + stupid.get().measureHeight() +  getBuffer()/2f;


            Paint p = new Paint();
            p.setColor(BaseApp.getApp().lightColor);
            currentAlpha = (currentAlpha * rate + 0xff) / (rate + 1);
            p.setAlpha((int) (currentAlpha * (paint.getAlpha() / (float) 0xff)));

            float targetLeft = getBuffer();//(measureWidth() / 2f) - (stupid.get().measureWidth() / 2f) - (getBuffer()/2f);
            float targetRight =measureWidth()/2;//measureWidth()-getBuffer();// Math.max(measureWidth()-getBuffer(),stupid.get().measureWidth()+getBuffer());//(measureWidth() / 2f) + (stupid.get().measureWidth() / 2f) + (getBuffer()/2f);
            if (lastZoom != BaseApp.getApp().zoom) {
                // umm we should be able to do something with this
                //lastLeft= lastLeft;
                lastZoom = BaseApp.getApp().zoom;
            }
            lastLeft = (lastLeft * rate + targetLeft) / (rate + 1);
            lastRight = (lastRight * rate + targetRight) / (rate + 1);

            //RectF r = new RectF((int)lastLeft,
            //        (int)(top+getBuffer()*3 - (getBuffer()/2f)),
            //        (int)(lastRight),
            //        (int)(top+getBuffer()*3+stupid.get().measureHeight()) + (getBuffer()/2f));

            //canvas.drawRect(r,p);
            //BaseApp.getApp().getCornor(),BaseApp.getApp().getCornor();
//            p.setStrokeWidth(BaseApp.getApp().getStrokeWidth()*10);
//            canvas.drawLine(
//                    realLeft + lastLeft,
//                    liney,
//                    realLeft + lastRight,
//                    liney,
//                    p);

            int x1 = (int)(lastRight-targetRight); //(int) (realLeft + lastLeft);
            int y1 = (int)(top +  getBuffer()*2.5);//(int) (liney -2*getBuffer());
            int x2 = (int)lastRight;//(int)(owner.width/2f); //(int) (realLeft + lastRight);
            int y2 = (int)(top +  getBuffer()*3.5 + stupid.get().measureHeight()) ;//(int) (liney+2*getBuffer());
            Shader shader = new LinearGradient(x1, y1, x2, y1, BaseApp.getApp().lightColor, 0x00000000, Shader.TileMode.CLAMP);
            Paint p2 = new Paint();
            p2.setDither(true);
            p2.setAntiAlias(true);
            p2.setShader(shader);
            canvas.drawRect(new RectF(x1, y1, x2*2, y2), p2);

//            int x1 = 0, y1 = 0, x2 = 300,  y2 = 600;
//            Shader shader = new LinearGradient(0, 0, y2, y2, Color.BLUE, Color.GREEN, Shader.TileMode.CLAMP);
//            Paint paint2 = new Paint();
//            paint2.setShader(shader);
//            canvas.drawRect(new RectF(x1, y1, x2, y2), paint2);

//            float lineybot = top +  getBuffer()*2.5f;
//
//            p.setStrokeWidth(BaseApp.getApp().getStrokeWidth());
//            canvas.drawLine(
//                    left + lastLeft,
//                    lineybot,
//                    left + lastRight,
//                    lineybot,
//                    p);
        }
        //
        // else{
        //     lastLeft =  eqCenterX;
        //     lastRight = eqCenterX;
        //}

        stupid.get().draw(canvas,  eqCenterX ,top +  getBuffer()*3 + stupid.get().measureHeightUpper());

    }

    @Override
    public pm parentThesisMode() {
        return pm.WRITE;
    }

    @Override
    public float requestedMaxX() {
        return 0;
    }

    @Override
    public float requestedMinX() {
        return -Math.max(0,(stupid.get().measureWidth()+  getBuffer()*3-owner.width));
    }

    @Override
    public float measureHeight() {
        return stupid.get().measureHeight() +4 *  getBuffer();
    }

    public void insert(Equation newEq) {
        selected.parent.add(selected.parent.indexOf(selected), newEq);
    }

    @Override
    public PlaceholderEquation getSelected() {
        return selected;
    }

    @Override
    public void setSelected(Equation equation) {
        Log.e("Input.setSelected","InputView can not be set");
    }

    public void deActivate() {
        getSelected().deActivate();
    }

    private float offsetX=0;
    private float vx=0;
    private float toAddToOffsetX=0;
    public float getOffsetX() {
        return offsetX;
    }

    public void updateVeloctiyX(float stepsPass, float maxSteps, float dx) {
        offsetX += dx;
        float currentVx = (dx) / stepsPass;

        boolean print = false;
        if ( Math.abs(vx) > .1) {
            Log.i("vX-in",vx+"");
            print= true;
        }

        if (stepsPass < maxSteps) {
            vx = (((maxSteps - stepsPass) * vx) + ((stepsPass) * currentVx)) / maxSteps;
        } else {
            vx = currentVx;
        }

        if (print) {
            Log.i("vX-out",vx+",stepsPass "+ stepsPass+",maxSteps "+ maxSteps+",dx "+ dx);
        }
    }

    public void toAddToOffsetX(float toAdd) {
        toAddToOffsetX+=toAdd;
    }

    public void addToOffsetX() {
        float toAdd =toAddToOffsetX/BaseApp.getApp().getRate();
        offsetX+= toAdd;
        toAddToOffsetX-= toAdd;
    }

    public void snapBack() {
        float maxOffsetX = requestedMaxX();
        float minOffsetX = requestedMinX();
        for (int i =0;i< owner.getLinesSize();i++){
            if ( this.equals(owner.nextInputLine(i))){
                maxOffsetX = Math.max(owner.getLine(i).requestedMaxX(),maxOffsetX);
                minOffsetX = Math.min(owner.getLine(i).requestedMinX(), minOffsetX);
            }

        }


        if (offsetX > maxOffsetX){
            offsetX = (offsetX*BaseApp.getApp().getRate() +maxOffsetX)/(BaseApp.getApp().getRate()+1);
            vx=0;
        }

        if (offsetX < minOffsetX){
            offsetX = (offsetX*BaseApp.getApp().getRate() +minOffsetX)/(BaseApp.getApp().getRate()+1);
            vx=0;
        }
    }

    public void slide(double friction, float steps) {
        if ( Math.abs(vx) > .1) {
            Log.i("vX-slide",vx+"");
        }else{
            //Log.i("vx-slide-called","true dat");
        }

        float dx = (float) (vx * ((Math.pow(friction, steps) - 1) / Math.log(friction)));
        vx = (float) (vx * Math.pow(friction, steps));
        offsetX+=dx;
    }

    public Equation imedateRight() {
        int at = getSelected().parent.indexOf(getSelected());
        if (at==getSelected().parent.size()-1){
            return null;
        }else{
            return getSelected().parent.get(at+1);
        }
    }

    public Equation imedateLeft() {
        int at = getSelected().parent.indexOf(getSelected());
        if (at==0){
            return null;
        }else{
            return getSelected().parent.get(at-1);
        }
    }

    public void stopSliding() {
        vx = 0;
    }


    public void updateOffset() {
            PlaceholderEquation phe = this.getSelected();
            if (phe.getX() + (phe.measureWidth() / 2f) > owner.width - 4 * EquationLine.getBuffer()) {
                this.toAddToOffsetX((owner.width - 4 * EquationLine.getBuffer()) - (phe.getX() + (phe.measureWidth() / 2f)));
            }
            if (phe.getX() - (phe.measureWidth() / 2f) < 4 * EquationLine.getBuffer()) {
                this.toAddToOffsetX((4 * EquationLine.getBuffer()) - (phe.getX() - (phe.measureWidth() / 2f)));
            }

            // the bottom of the input should not be off the bottom of the screen
            if (owner.getOffsetY() + (this.stupid.get().measureHeight() / 2f) > owner.height - getKeyboad().measureTargetHeight() && (this.stupid.get().measureHeight() < owner.height - owner.keyBoardManager.get().measureHeight())) {
                owner.toAddToOffsetY((owner.height - getKeyboad().measureTargetHeight())
                        - (owner.getOffsetY() + (this.stupid.get().measureHeight() / 2f)));
            }

            // the place holder should not be off the top of the screen
            // this probably does not really work since getY stopping being update off screen
            // it also probably is not need and will never happen because we already scroll if get the last line offscreen
            if ((phe.getY() - phe.measureHeightUpper() < EquationLine.getBuffer())) {
                owner.toAddToOffsetY((EquationLine.getBuffer()) - (phe.getY() - phe.measureHeightUpper()));
            }
            // the place holder should not be off the bot of the screen
//            if (phe.getY() + phe.measureHeightLower() > owner.height - (getKeyboad().measureTargetHeight() + EquationLine.getBuffer())) {
//                owner.toAddToOffsetY((owner.height - (getKeyboad().measureTargetHeight() + EquationLine.getBuffer())) - (phe.getY() + phe.measureHeightLower()));
//            }

        Log.d("updatedOffset","target: "+ getKeyboad().measureTargetHeight() + " normal: "+ getKeyboad().measureHeight() + " total: "+ owner.height);
        }

}
