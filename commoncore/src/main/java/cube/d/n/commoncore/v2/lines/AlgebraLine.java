package cube.d.n.commoncore.v2.lines;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import cube.d.n.commoncore.Animation;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.CanTrackChanges;
import cube.d.n.commoncore.DragLocation;
import cube.d.n.commoncore.EquationButton;
import cube.d.n.commoncore.LongTouch;
import cube.d.n.commoncore.eq.DragEquation;
import cube.d.n.commoncore.eq.DragLocations;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MonaryEquation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.v2.Main;
import cube.d.n.commoncore.v2.Selects;
import cube.d.n.commoncore.v2.TouchMode;
import cube.d.n.commoncore.v2.keyboards.AlgebraKeyboard;
import cube.d.n.commoncore.v2.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class AlgebraLine extends Line implements CanTrackChanges,Selects {

    private Equation selected;
    public int stupidAlpha=0xff;
    public DragEquation dragging;
    public ArrayList<EquationButton> history = new ArrayList<>();
    protected DragLocations dragLocations = new DragLocations();
    public LongTouch lastLongTouch = null;
    private ArrayList<Animation> animation = new ArrayList<>();


    public AlgebraLine(Main owner) {
        super(owner);
    }

    private KeyBoard myKeyBoard = null;

    public AlgebraLine(Main owner, Equation newEq) {
        super(owner);
        stupid.set(newEq);
    }

    @Override
    public KeyBoard getKeyboad() {
        if (myKeyBoard == null){
            myKeyBoard = new AlgebraKeyboard(owner,this);
        }
        return myKeyBoard;
    }

    @Override
    public float measureHeight() {
        float sum = 0 ;
        sum = equationHeight(stupid.get());

        for (EquationButton e: history){
            if ((!e.equals(history.get(0)))) {
                sum += e.measureHeight() + getHistoryBuffer();
            }
        }

        return sum;
    }

    private float equationHeight(Equation equation) {
        return stupid.get().measureHeight() + 2 * buffer;
    }

    @Override
    protected void innerDraw(Canvas canvas, float top, float left, Paint paint) {

        int targetAlpha;
        if (EquationButton.current != null && EquationButton.current.lastLongTouch != null){
            stupidAlpha = (int)(Math.max(.7f-EquationButton.current.lastLongTouch.percent(),0)*0xff);
            targetAlpha = stupidAlpha;
        }else{
            targetAlpha=0xff;
        }
        int rate = BaseApp.getApp().getRate();
        stupidAlpha = (stupidAlpha * rate + targetAlpha) / (rate + 1);
        stupid.get().setAlpha(stupidAlpha);

        if (stupid.get() instanceof EqualsEquation){
            stupid.get().draw(canvas, left + measureWidth()/2f, top + measureHeight() - buffer - stupid.get().measureHeightLower());
        }else{
            stupid.get().draw(canvas, left + measureWidth()/2f, top + measureHeight() - buffer - stupid.get().measureHeightLower());
        }

        if (dragging != null) {
            dragging.eq.draw(canvas, dragging.eq.x, dragging.eq.y);
        }

        for (int i = 0; i < animation.size(); i++) {
            animation.get(i).draw(canvas);
        }


        drawHistory(canvas,top,left,paint);
    }


    double lastZoom = BaseApp.getApp().zoom;
    float baseBuffer = 75 * BaseApp.getApp().getDpi();
    float fade = 0.4f;
    private void drawHistory(Canvas canvas, float top, float left, Paint paint) {
        float historyBuffer = getHistoryBuffer();

        float atHeight = top + measureHeight() - buffer - stupid.get().measureHeight() - historyBuffer;
        float currentPercent = fade;
        for (EquationButton eb : history) {
            if (lastZoom != BaseApp.getApp().zoom){
                eb.myEq.deepNeedsUpdate();
                eb.updateZoom(lastZoom,BaseApp.getApp().zoom);
            }
            if ((!eb.equals(history.get(0)))) {

                atHeight -= eb.myEq.measureHeightLower();
                eb.targetColor = getHistoryColor(0xff000000, BaseApp.getApp().darkColor, currentPercent);
                eb.x = 0;
                eb.targetY = atHeight;
                int centerX;
                int centerY;
                if (stupid.get() instanceof EqualsEquation){
                    centerX= stupid.get().lastPoint.get(0).x;
                    centerY= stupid.get().lastPoint.get(0).y;
                }else{
                    centerX= (int) stupid.get().getX();
                    centerY= (int) stupid.get().getY();
                }

                eb.update(centerX,centerY);
                if ((centerY + atHeight + eb.myEq.measureHeightLower()) > 0 &&
                        (centerY + atHeight - eb.myEq.measureHeightUpper()) < measureHeight()) {
                    eb.draw(canvas, centerX, centerY);
                } else  if (  centerY > -(measureHeight()/4)*BaseApp.getApp().getDpi()
                        || centerY < (measureHeight()*5/4)*BaseApp.getApp().getDpi()){
                    // update the locations
                    eb.updateLocations(centerX, centerY);
                }
                atHeight -= eb.myEq.measureHeightUpper() + historyBuffer;
                currentPercent *= fade;
            }
        }
        lastZoom = BaseApp.getApp().zoom;
    }



    TouchMode myMode;
    public boolean hasUpdated = false;
    private float lastX;
    private float lastY;
    private long lastTapTime;
    private Point lastTapPoint;

    @Override
    public boolean onTouch(MotionEvent event) {


        if (!hasUpdated){
            stupid.get().updateLocation();
        }
        hasUpdated = false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (in(event)) {
                if (stupid.get().nearAny(event.getX(), event.getY())) {
                    resolveSelected(event);
                    myMode = TouchMode.SELECT;
                } else {
                    myMode = TouchMode.MOVE;
                    if (selected != null) {
                        removeSelected();
                    }
                }
                lastX = event.getX();
                lastY = event.getY();
                return true;
            } else {
                myMode = TouchMode.NOPE;
            }
        }  if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (myMode == TouchMode.SELECT) {
                selectMoved(event);
                return true;
            }

            // if we are dragging something move it
            // drag is only a mode in the solve screen
            if (myMode == TouchMode.DRAG) {
                dragging.eq.x = event.getX();
                dragging.eq.y = event.getY();

                DragLocation closest = dragLocations.closest(event);

                if (closest != null) {
                    stupid.set(closest.myStupid);
                }
                return true;
            }

            if (myMode == TouchMode.MOVE) {
                //TODO scrolling
                return true;
            }

            if (myMode == TouchMode.NOPE) {
                //TODO scrolling
                return false;
            }


        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (myMode != TouchMode.NOPE){
                // did we click anything?
                boolean clicked = false;
                long now = System.currentTimeMillis();
                Point tapPoint = new Point();
                tapPoint.x = (int) event.getX();
                tapPoint.y = (int) event.getY();
                long tapSpacing = now - lastTapTime;
                if (tapSpacing < BaseApp.getApp().doubleTapSpacing && dis(tapPoint, lastTapPoint) < BaseApp.getApp().getDoubleTapDistance() && myMode == TouchMode.SELECT) {
                    Log.i("", "doubleTap! dis: " + dis(tapPoint, lastTapPoint) + " time: " + tapSpacing);

                        stupid.get().tryOperator(event.getX(),
                                event.getY());
                        clicked = true;
                            if (hasChanged()) {
                                clicked = false;
                                // TODO
                                // TODO
                                // this is def a band-aid
                                // the problem is that some operation don't change the eq
                                // while making a new copy of it
                                // in these case we still select - altho maybe we don't need to
                                // however sometimes selected is in the old version of stupid
                                // this casues problem when we try to select it
                                // the real solution is to pass selected on in a good way
                                // this probably mean editing copy and rewriting it like constructors
                                // TODO
                                // TODO
                                if (selected != null) {
                                    selected.setSelected(false);
                                }
                            } else {
                                if (selected != null) {
                                    selected.setSelected(false);
                                }
                            }

                    // set the lastTapTime to zero so they can not triple tap and get two double taps
                    lastTapTime = 0;
                } else {
                    lastTapTime = now;
                }
                lastTapPoint = tapPoint;
                if (!clicked) {
                    endOnePointer(event);
                }

                // if we were dragging everything around
                if (myMode == TouchMode.MOVE) {
                //TODO scrolling
                    return true;
                }

                lastLongTouch = null;

            }else{
                return false;
            }
        }
        return false;
    }

    private void endOnePointer(MotionEvent event) {
        if (myMode == TouchMode.SELECT) {
            resolveSelected(event);
        } else if (myMode == TouchMode.DRAG) {
            DragLocation closest = dragLocations.closest(event);

            if (closest != null) {
                closest.select();
            }


            stupid.get().fixIntegrety();


            dragging.demo.isDemo(false);
            dragging = null;

            if (selected != null) {
                selected.setSelected(false);
            }
        }
    }

    private float dis(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return (float) Math.sqrt((dx * dx) + (dy * dy));
    }

    public void removeSelected() {
        if (selected instanceof PlaceholderEquation) {
            if (!(selected.parent.size() == 1 && selected.parent != null)) {
                Equation oldEq = selected;
                selected.setSelected(false);
                oldEq.remove();
            }
        }
        if (selected != null) {
            if ( selected.parent != null && selected.parent.size() != 1 ) {
                Equation oldEq = selected;
                selected.setSelected(false);
                oldEq.tryFlatten();
            }else {
                selected.setSelected(false);
            }
        }
    }

    protected void selectMoved(MotionEvent event) {
        // if they get too far from were they started we are going to start dragging
        //TODO scale by dpi
        float maxMovement = 50 * BaseApp.getApp().getDpi();
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
            // you can't do anything with the contents of a power equation so if they have that selected let's get the whole power equation
            while (selected.parent instanceof MonaryEquation
                    || (selected.parent instanceof PowerEquation && selected.parent.indexOf(selected) == 0)) {
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
        stupid.get().getDragLocations(dragging.demo, dragLocations, dragging.ops);
    }


    private boolean willRemove = false;

    protected void resolveSelected(MotionEvent event) {

        HashSet<Equation> willSelect = stupid.get().closetOn(event.getX(),
                event.getY());

        int side = -1;
        for (Equation e : willSelect) {
            if (side == -1) {
                side = e.side();
            } else if (side != e.side()) {
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

        ArrayList<Equation> selectingSet;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            willRemove = false;
        }
        if (selected != null) {
            if (willSelect.isEmpty()) {
                // if they did not click anything let's not select anything
                selectingSet = new ArrayList<>();
            } else {
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
                    if (newIsCurrent) {
                        // nothing
                        if (willRemove && event.getAction() == MotionEvent.ACTION_UP) {
                            selectingSet = new ArrayList<>();
                        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            willRemove = true;
                            selectingSet = current;
                        } else {
                            selectingSet = current;
                        }
                    } else {
                        // just new
                        if (willRemove && event.getAction() == MotionEvent.ACTION_UP) {
                            selectingSet = newLeafs;
                        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            willRemove = true;
                            selectingSet = current;
                        } else {
                            selectingSet = current;
                        }
                    }
                }
            }

            // now we need to deselect and flatten
            selected.setSelected(false);
            stupid.get().fixIntegrety();

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

    protected void selectSet(ArrayList<Equation> selectingSet) {
        Equation lcp = null;
        // if anything in selectingSet is contained by something else remove it
        for (int i1 = 0; i1 < selectingSet.size(); i1++) {
            Equation eq1 = selectingSet.get(i1);
            for (int i2 = i1 + 1; i2 < selectingSet.size(); i2++) {
                Equation eq2 = selectingSet.get(i2);
                if (!(eq1.equals(eq2)) && eq1.deepContains(eq2)) {
                    Log.i("removed eq2", eq2.toString() +" killed by: "+ eq1.toString());
                    selectingSet.remove(i2);
                    i2--;
                }
                if (!(eq1.equals(eq2)) && eq2.deepContains(eq1)) {
                    Log.i("removed eq1", eq1.toString()+" killed by: "+ eq2.toString());
                    selectingSet.remove(i1);
                    i2--;
                    i1--;
                    break;
                }
            }
        }
        for (Equation eq : selectingSet) {
            if (lcp == null) {
                lcp = eq;//eq.parent
            } else { //if (!eq.parent.equals(lcp))
                lcp = lcp.lowestCommonContainer(eq);
            }
        }
        if (lcp != null && selectingSet.size() > 1) {
            // make sure they are a continous block
            ArrayList<Integer> indexs = new ArrayList<Integer>();
            for (Equation eq : selectingSet) {
                int index = lcp.deepIndexOf(eq);
                if (index == -1) {
                    Log.e("index is -1!", lcp.toString() + " " + eq.toString());
                }

                if (!indexs.contains(index)) {
                    indexs.add(index);
                }
            }
            Collections.sort(indexs);
            //Collections.reverse(indexs);
            int min = indexs.get(0);
            Log.d("min", min + "");
            if (lcp.get(min) instanceof WritingLeafEquation) {
                // the div lines in isOp could be a problem here
                // i think it's no a problem... I hope
                ((WritingLeafEquation) lcp.get(min)).isOpRight();
                if (min != 0) {
                    indexs.add(0, min - 1);
                }
            }
            int max = indexs.get(indexs.size() - 1);
            Log.d("max", max + "");
            if (lcp.get(max) instanceof WritingLeafEquation) {
                // the div lines in isOp could be a problem here
                // i think it's no a problem... I hope
                ((WritingLeafEquation) lcp.get(max)).isOpLeft();
                if (max != lcp.size() - 1) {
                    indexs.add(max + 1);
                }
            }

            // the we need to add the indexes between min and max
            for (int newIndex = min + 1; newIndex < max; newIndex++) {
                if (!indexs.contains(newIndex)) {
                    indexs.add(newIndex);
                }
            }
            Collections.sort(indexs);

            // if they do not make up all of lcp
            if (indexs.size() != lcp.size()) {
                // we make a new equation of the type of lcp
                Equation toSelect = null;
                if (lcp instanceof MultiEquation) {
                    toSelect = new MultiEquation(this);
                } else if (lcp instanceof AddEquation) {
                    toSelect = new AddEquation(this);
                } else if (lcp instanceof WritingEquation) {
                    toSelect = new WritingEquation(this);
                }
                //sort selected set
                ArrayList<Equation> selectedList = new ArrayList<Equation>();
                for (int i : indexs) {
                    selectedList.add(lcp.get(i));
                }
                // remove the selectingSet from lcp and add it to our
                // new equation
                for (Equation eq : selectedList) {
                    lcp.justRemove(eq);
                    toSelect.add(eq);
                }
                // insert the new equation in to lcp
                lcp.add(indexs.get(0), toSelect);
                // and select the new equation
                toSelect.setSelected(true);
            } else {
                lcp.setSelected(true);
            }
        } else {
            if (lcp != null) {
                lcp.setSelected(true);
            }
        }
    }

    private float getHistoryBuffer() {
        return (float)(baseBuffer* BaseApp.getApp().zoom);
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

    @Override
    public pm parentThesisMode() {
        return pm.SOLVE;
    }

    public void changed() {    }

    @Override
    public boolean hasChanged() {
        return false;
    }

    @Override
    public ArrayList<Animation> getAfterAnimations() {
        return animation;
    }

    public void drawProgress(Canvas canvas, float percent, int i) {

    }

    @Override
    public Equation getSelected() {
        return selected;
    }

    @Override
    public void setSelected(Equation equation) {
        selected=equation;
    }

    public void tryWarn(Equation equation) {

    }
}
