package cube.d.n.commoncore.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;


import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.EquationDis;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Selects;
import cube.d.n.commoncore.TouchMode;
import cube.d.n.commoncore.keyboards.InputKeyboard;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
* Created by Colin_000 on 5/7/2015.
*/
public class InputLine extends Line implements Selects {

    private final PlaceholderEquation selected;

    public InputLine(Main owner) {
        super(owner);
        selected = new PlaceholderEquation(this);
        initEq();
    }

    private KeyBoard myKeyBoard = null;
    @Override
    public KeyBoard getKeyboad() {
        if (myKeyBoard == null){
            myKeyBoard = new InputKeyboard(owner,this);
        }
        return myKeyBoard;
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
                    if (getSelected().nearAny(event.getX(), event.getY())) {
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

    private void updateVelocity(MotionEvent event) {
        //TODO
    }

    public void removeSelected() {
        if (selected instanceof PlaceholderEquation) {
            if (!(selected.parent.size() == 1 && selected.parent != null)) {
                Equation oldEq = selected;
                oldEq.remove();
            }
        }
    }

    private void resolveSelected(MotionEvent event) {
// now we need to figure out what we are selecting
        // find the least commond parent

        int currentBkgAlpha = 0x00;
        if (selected != null) {
            currentBkgAlpha = selected.bkgAlpha;
        }

        removeSelected();

        Equation lcp = null;
        // if it's an action up
        // and it was near the left of stupid
        ArrayList<EquationDis> closest = stupid.get().closest(
                event.getX(), event.getY());

        lcp = closest.get(0).equation;

        // TODO 100 to var scale by dpi
        //float minDis = 100 * Algebrator.getAlgebrator().getDpi();
        //if (Math.abs(event.getY() - lcp.y) < minDis) {
        if (lcp instanceof PlaceholderEquation) {
            lcp.setSelected(true);
        } else {
            // the the lcp is the left or right end of something we might want to select it's parent
            Equation current = lcp;

            boolean left = event.getX() < lcp.x;
            int depth = 0;
            // find how many layor deep we are
            while (current.parent != null && current.parent.indexOf(current) == (left ? 0 : current.parent.size() - 1)) {
                // we don't count binary eq because they are fixed size
                //if (!(current.parent instanceof BinaryEquation)) {
                depth++;
                //}
                current = current.parent;
            }
            // we really should figure how much space we have to work with so we can divide it up


            if (depth != 0) {
                float distance = 100 * BaseApp.getApp().getDpi() + (lcp.measureWidth() / 2f);

                // this means use left or right
                Equation next = (left ? lcp.left() : lcp.right());
                if (next != null) {
                    distance = Math.abs(lcp.x - next.x) / 2;
                }

                float each = distance / ((float) (depth + 1));
                int num = (int) Math.floor(Math.min(Math.abs(lcp.x - event.getX()), distance) / each);

                if (num == depth + 1) {
                    num = depth;
                }

                current = lcp;
                while (num != 0) {
                    //if (!(current.parent instanceof BinaryEquation)) {
                    num--;
                    //}
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

        float eqCenterX=left +(  stupid.get().measureWidth() / 2f) + getBuffer();



        if (!owner.lastLine().equals(this)) {

            float liney = top +  getBuffer()*3 + stupid.get().measureHeight() +  getBuffer()/2f;


            Paint p = new Paint();
            //p.setColor(BaseApp.getApp().darkColor);
            currentAlpha = (currentAlpha * rate + 0xff) / (rate + 1);
            p.setAlpha((int) (currentAlpha * (paint.getAlpha() / (float) 0xff)));

            float targetLeft = getBuffer();//(measureWidth() / 2f) - (stupid.get().measureWidth() / 2f) - (getBuffer()/2f);
            float targetRight = Math.max(measureWidth()-getBuffer(),stupid.get().measureWidth()+2*getBuffer());//(measureWidth() / 2f) + (stupid.get().measureWidth() / 2f) + (getBuffer()/2f);
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
            p.setStrokeWidth(BaseApp.getApp().getStrokeWidth());
            canvas.drawLine(
                    left + lastLeft,
                    liney,
                    left + lastRight,
                    liney,
                    p);

//            float lineybot = top +  getBuffer()*2.5f;
//
//            p.setStrokeWidth(BaseApp.getApp().getStrokeWidth());
//            canvas.drawLine(
//                    left + lastLeft,
//                    lineybot,
//                    left + lastRight,
//                    lineybot,
//                    p);
        }else{
             lastLeft =  eqCenterX;
             lastRight = eqCenterX;
        }

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

}
