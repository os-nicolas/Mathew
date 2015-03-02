package colin.example.algebrator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;

import colin.algebrator.eq.DragEquation;
import colin.algebrator.eq.DragLocations;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.MonaryEquation;

public class ColinView extends SuperView {
    public ArrayList<EquationButton> history = new ArrayList<EquationButton>();
    protected DragLocations dragLocations = new DragLocations();

    public ColinView(Context context) {
        super(context);
        init(context);
    }

    public ColinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColinView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void init(Context context) {
        canDrag = true;
        buttonsPercent = 1f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHistory(canvas);
        for (EquationButton eb : history) {
            eb.tryRevert(canvas);
        }
        onDrawAfter(canvas);
    }

    // TODO scale with dpi
    float buffer = 75 * Algebrator.getAlgebrator().getDpi();
    float fade = 0.4f;

    private void drawHistory(Canvas canvas) {
        float atHeight = -stupid.measureHeightUpper() - buffer;
        float currentPercent = fade;
        for (EquationButton eb : history) {
            if (!eb.equals(history.get(0))) {
                atHeight -= eb.myEq.measureHeightLower();
                eb.targetColor = getHistoryColor(0xff000000, Algebrator.getAlgebrator().darkColor, currentPercent);
                eb.x = 0;
                eb.targetY = atHeight;
                eb.update(stupid.lastPoint.get(0).x, stupid.lastPoint.get(0).y);
                if ((stupid.lastPoint.get(0).y + atHeight + eb.myEq.measureHeightLower()) > 0 &&
                        (stupid.lastPoint.get(0).y + atHeight - eb.myEq.measureHeightUpper()) < height) {
                    eb.draw(canvas, stupid.lastPoint.get(0).x, stupid.lastPoint.get(0).y);
                } else {
                    // update the locations
                    eb.updateLocations(stupid.lastPoint.get(0).x, stupid.lastPoint.get(0).y);
                }
                atHeight -= eb.myEq.measureHeightUpper() + buffer;
                currentPercent *= fade;
            }
        }
    }

    private int getHistoryColor(int currentColor, int targetColor, float percent) {
        int currentRed = android.graphics.Color.red(currentColor);
        int currentGreen = android.graphics.Color.green(currentColor);
        int currentBlue = android.graphics.Color.blue(currentColor);

        int targetRed = android.graphics.Color.red(targetColor);
        int targetGreen = android.graphics.Color.green(targetColor);
        int targetBlue = android.graphics.Color.blue(targetColor);

        currentColor = android.graphics.Color.argb(
                0xff,
                (int) ((percent * currentRed) + ((1 - percent) * targetRed)),
                (int) ((percent * currentGreen) + ((1 - percent) * targetGreen)),
                (int) ((percent * currentBlue) + ((1 - percent) * targetBlue)));

        return currentColor;
    }


    public boolean changed = false;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        for (int i = 0; i < history.size(); i++) {
            history.get(i).click(event);
        }

        boolean result = super.onTouch(view, event);

        if (changed) {
            history.add(0, new EquationButton(stupid.copy(), this));
            Log.i("add to History", stupid.toString());
            changed = false;
        }

        return result;
    }

    @Override
    protected void selectMoved(MotionEvent event){
        // if they get too far from were they started we are going to start dragging
        //TODO scale by dpi
        float maxMovement = 50 * Algebrator.getAlgebrator().getDpi();
        float distance = (float) Math.sqrt((lastX - event.getX()) * (lastX - event.getX()) + (lastY - event.getY()) * (lastY - event.getY()));
        if (maxMovement < distance) {
            boolean pass = true;
            if (selected != null) {
                startDragging();
            } else {
                myMode = TouchMode.MOVE;
            }
        }
    }

    private void startDragging() {
            if (selected != null) {
                myMode = TouchMode.DRAG;
                // we need to take all the - signs with us
                while (selected.parent instanceof MonaryEquation) {
                    selected.parent.setSelected(true);
                }
                //if (selected.canPop()) {
                dragging = new DragEquation(selected);
                dragging.eq.x = lastX;
                dragging.eq.y = lastY;
                selected.isDemo(true);

                selected.setSelected(false);

                getDragLocations();

                Log.d("Drag Locations", "#######################");
                for (DragLocation dl : dragLocations) {
                    Log.d("Drag Locations", dl.myStupid.toString());
                }
                //}

            } else {
                myMode = TouchMode.MOVE;
            }
    }

    //update DragLocations
    private void getDragLocations() {
        dragLocations = new DragLocations();
        stupid.getDragLocations(dragging.demo, dragLocations, dragging.ops);
    }

    private boolean willRemove= false;

    @Override
    protected void resolveSelected(MotionEvent event) {

        HashSet<Equation> willSelect = stupid.closetOn(event.getX(),
                event.getY());

        int side=-1;
        for (Equation e:willSelect){
            if (side==-1){
                side =e.side();
            }else if (side != e.side()){
                willSelect = new HashSet<>();
                break;
            }

        }

        String db1 = "";
        for (Equation e : willSelect) {
            db1 += e.toString() + ",";
        }

        Log.d("resolving selected, will select: ", db1);
        Log.d("resolving selected, selected: ", (selected == null ? "null" : selected.toString()));

        ArrayList<Equation> selectingSet = new ArrayList<Equation>();

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            willRemove = false;
        }

        if (selected != null) {
            // if everything we are adding is deep contained by selected we want to remove
            // otherwise we want to add what is not already contained



            // if all of new is on the other side we select the other side
            boolean otherSide = false;

            // if new is not all contained by old we add
            boolean newNotAllInCurrent = false;

            // if new is current we select nothing
            boolean newIsCurrent = true;

            // if new is all contained we select new
            boolean newSubSetCurrent = false;


            ArrayList<Equation> current = selected.getLeafs();
            ArrayList<Equation> newLeafs = new ArrayList<>();
            for (Equation e : willSelect) {
                for (Equation l : e.getLeafs()) {
                    if (!newLeafs.contains(l)) {
                        newLeafs.add(l);
                    }
                }
            }

            int currentSide = current.get(0).side();
            for (Equation e : newLeafs) {
                if (e.side() != currentSide) {
                    otherSide = true;
                    break;
                }
                if (!current.contains(e)) {
                    newNotAllInCurrent = true;
                }
            }
            if (otherSide) {
                // just new
                selectingSet = newLeafs;
            } else if (newNotAllInCurrent) {
                // union
                selectingSet = current;
                for (Equation e : newLeafs) {
                    if (!selectingSet.contains(e)) {
                        selectingSet.add(e);
                    }
                }
            } else {
                for (Equation e : current) {
                    if (!newLeafs.contains(e)) {
                        newIsCurrent = false;
                    }
                }
                if (newIsCurrent ) {
                    // nothing
                    if (willRemove && event.getAction() == MotionEvent.ACTION_UP){
                        selectingSet = new ArrayList<>();
                    }else  if (event.getAction() == MotionEvent.ACTION_DOWN){
                        willRemove = true;
                        selectingSet = current;
                    }else{
                        selectingSet = current;
                    }
                } else {
                    // just new
                    if (willRemove && event.getAction() == MotionEvent.ACTION_UP) {
                        selectingSet = newLeafs;
                    }else if (event.getAction() == MotionEvent.ACTION_DOWN){
                        willRemove = true;
                        selectingSet = current;
                    }else{
                        selectingSet = current;
                    }
                }
            }

            // now we need to deselect and flatten
            selected.setSelected(false);
            stupid.fixIntegrety();

        } else {
            selectingSet = new ArrayList<Equation>(willSelect);
        }


        String db2 = "";
        for (Equation e : selectingSet) {
            db2 += e + ",";
        }
        Log.d("resolving selected, selectingSet: ", db2);

        if (selectingSet.isEmpty()) {
            //do nothing
        } else if (selectingSet.size() == 1) {
            ((Equation) selectingSet.toArray()[0]).setSelected(true);
        } else {
            selectSet(selectingSet);
        }
    }

    public void centerEq() {
        stupid.updateLocation();
        offsetX += stupid.lastPoint.get(0).x - stupid.x;
    }

    //
    private Equation getCenterEq() {
        Equation closest = stupid;
        float dis = disToCenter(stupid.x, stupid.y);
        for (EquationButton eb : history) {
            if (!eb.equals(history.get(0))) {
                float myDis = disToCenter(eb.myEq.x, eb.myEq.y);
                if (myDis < dis) {
                    dis = myDis;
                    closest = eb.myEq;
                }
            }
        }
        return closest;
    }

    private float disToCenter(float x, float y) {
        return (float) Math.sqrt((x - width / 2) * (x - width / 2) + (y - buttonLine() / 2) * (y - buttonLine() / 2));
    }


    @Override
    protected float outTop() {
        Equation closest = getCenterEq();
        if (closest.y + closest.measureHeightLower() - buffer < 0 && closest.y + closest.measureHeightLower() + buffer < buttonLine()) {
            Log.d("out,top", "closest");
            //message.db("outtop, closest");
            return -(closest.y + closest.measureHeightLower() - buffer);
        }
        return super.outTop();
    }

    @Override
    protected float outLeft() {
        Equation closest = getCenterEq();
        if (closest.x + closest.measureWidth() / 2 - buffer < 0 && closest.x + closest.measureWidth() / 2 + buffer < width) {
            Log.d("out,left", "closest");
            //message.db("outleft, closest");
            return -(closest.x + closest.measureWidth() / 2 - buffer);
        }
        return super.outLeft();
    }

    @Override
    protected float outBottom() {
        Equation closest = getCenterEq();
        if (closest.y - closest.measureHeightUpper() + buffer > buttonLine() && closest.y - closest.measureHeightUpper() - buffer > 0) {
            Log.d("out,bot", "closest");
            //message.db("outbot, closest");
            return (closest.y - closest.measureHeightUpper() + buffer) - buttonLine();
        }
        return super.outBottom();
    }

    @Override
    protected float outRight() {
        Equation closest = getCenterEq();
        if (closest.x - closest.measureWidth() / 2 + buffer > width && closest.x - closest.measureWidth() / 2 - buffer > 0) {
            Log.d("out,right", "closest");
            //message.db("outright, closest");
            return (closest.x - closest.measureWidth() / 2 + buffer) - width;
        }
        return super.outRight();
    }
}
