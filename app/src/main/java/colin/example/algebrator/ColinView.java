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
import java.util.Random;

import colin.algebrator.eq.AddEquation;
import colin.algebrator.eq.DivEquation;
import colin.algebrator.eq.DragEquation;
import colin.algebrator.eq.DragLocations;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.MonaryEquation;
import colin.algebrator.eq.MultiEquation;
import colin.algebrator.eq.NumConstEquation;
import colin.algebrator.eq.Operations;
import colin.algebrator.eq.PowerEquation;
import colin.algebrator.eq.VarEquation;
import colin.example.algebrator.Actions.SovleScreen.BothSides;
import colin.example.algebrator.Actions.SovleScreen.SolveQuadratic;
import colin.example.algebrator.Actions.WriteScreen.DivAction;
import colin.example.algebrator.Actions.WriteScreen.NumberAction;
import colin.example.algebrator.Actions.WriteScreen.Solve;
import colin.example.algebrator.Actions.WriteScreen.TimesAction;
import colin.example.algebrator.Actions.WriteScreen.VarAction;
import colin.example.algebrator.tuts.SolvedTut;
import colin.example.algebrator.tuts.TutMessage;

public class ColinView extends SuperView {

    protected DragLocations dragLocations = new DragLocations();
    public Equation changedEq;

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
        BASE_BUTTON_PERCENT = 8f/9f;
        buttonsPercent = BASE_BUTTON_PERCENT;
        alreadySolved = false;

        PopUpButton quadratic = new PopUpButton(this,"Use quadratic formula", new SolveQuadratic(this));
        quadratic.setTargets(1f / 9f,0f,1f);
        popUpButtons.add(quadratic);
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




    @Override
    protected void addButtons() {
        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button(this,"+", new BothSides(this)));
        firstRow.add(new Button(this,"-", new BothSides(this)));
        char[] timesUnicode = {'\u00D7'};
        firstRow.add(new Button(this,new String(timesUnicode), new BothSides(this)));
        char[] divisionUnicode = {'\u00F7'};
        firstRow.add(new Button(this,new String(divisionUnicode), new BothSides(this)));

        addButtonsRow(firstRow, 8f / 9f, 9f / 9f);

    }







    public boolean changed = false;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (!message.inBar(event) ) {
            for (int i = 0; i < history.size(); i++) {
                history.get(i).click(event);
            }
        }

        boolean result = super.onTouch(view, event);

        if (changed) {
            // if they could have divided by 0 we need to warn them
            // we don't have to worry warn checks for null
            history.get(0).warn(changedEq);
            changedEq=null;

            // add a new Equation to history
            history.add(0, new EquationButton(stupid.copy(), this));
            Log.i("add to History", stupid.toString());
            changed = false;

            if (!alreadySolved && isSolved()){
                showSolvedMessage();
            }
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
                // you can't do anything with the contents of a power equation so if they have that selected let's get the whole power equation
                while (selected.parent instanceof MonaryEquation
                        || (selected.parent instanceof PowerEquation && selected.parent.indexOf(selected)==0) ) {
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

        ArrayList<Equation> selectingSet;

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            willRemove = false;
        }
        if (selected != null) {
            if (willSelect.isEmpty()) {
                // if they did not click anything let's not select anything
                selectingSet = new ArrayList<>();
            }else {
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
            if (!eb.equals(history.get(0)) ) {
                float myDis = disToCenter(eb.myEq.x, eb.myEq.y);
                if (myDis < dis) {
                    dis = myDis;
                    closest = eb.myEq;
                }
            }
        }
        return closest;
    }

    //TODO these (left,rigth,top, bottom)est are all a bit wrong:
        // 2 - for left and right stupid get included even when it is not in the screeen

    private Physical rightest(){
        Physical closest = stupid;
        float dis = stupid.x + stupid.measureWidth()/2;
        for (EquationButton eb : history) {
            if (!eb.equals(history.get(0)) && disToCenter(eb.myEq.x, eb.myEq.y) < width/2) {
                float myDis = eb.myEq.x+ eb.myEq.measureWidth()/2;
                if (myDis > dis) {
                    dis = myDis;
                    closest = eb.myEq;
                }
            }
        }
        return closest;
    }


    private Physical leftest(){
        Physical closest = stupid;
        float dis = stupid.getX() - stupid.measureWidth()/2;
        for (EquationButton eb : history) {
            if (!eb.equals(history.get(0))  && disToCenter(eb.getX(), eb.getY()) < width/2) {
                float myDis = eb.getX()- (eb.measureWidth()/2);
                if (myDis < dis) {
                    dis = myDis;
                    closest = eb;
                }
            }
        }
        return closest;
    }

    private Physical topest(){
//        Physical closest = stupid;
//        float dis = stupid.y - stupid.measureHeightUpper();
//        for (EquationButton eb : history) {
//            if (!eb.equals(history.get(0))  && disToCenter(eb.getX(), eb.getY()) < width/2) {
//                float myDis = eb.getY()- (eb.measureHeight()/2);
//                if (myDis < dis) {
//                    dis = myDis;
//                    closest = eb;
//                }
//            }
//        }
        Physical closest;
        if (history.isEmpty()){
            closest = stupid;
        }else{
            closest = history.get(history.size()-1);
        }
        return closest;
    }

    private Physical bottumest(){
        Physical closest = stupid;
//        float dis = stupid.y + stupid.measureHeightLower();
//        for (EquationButton eb : history) {
//            if (!eb.equals(history.get(0))  && disToCenter(eb.getX(), eb.getY()) < width/2) {
//                float myDis = eb.y+ (eb.measureHeight()/2);
//                if (myDis > dis) {
//                    dis = myDis;
//                    closest = eb;
//                }
//            }
//        }
        return closest;
    }


    private float disToCenter(float x, float y) {
        return (float) Math.sqrt((x - width / 2) * (x - width / 2) + (y - buttonLine() / 2) * (y - buttonLine() / 2));
    }


    //these are all wrong they neeed to look at two things,
    // the left and right
    // at least the horizonal ones
    // these all should also use math.min of the two condition to return


    @Override
    protected float outTop() {
        Physical top = topest();//getCenterEq();
        Physical bot = bottumest();
        if (top.getY() + top.measureHeight()/2 - buffer < 0 && bot.getY() + (bot.measureHeight()/2)  < buttonLine() - (height*(3f/4f))) {
            Log.d("out,top", "closest");
            //message.db("outtop, closest");
            return -(top.getY() + top.measureHeight()/2 - buffer);
        }
        return super.outTop();
    }

    @Override
    protected float outLeft() {
        Physical left = leftest();//getCenterEq();
        Physical right = rightest();
        if (left.getX() + left.measureWidth() / 2 - buffer < 0 && right.getX() + right.measureWidth() / 2 + buffer < width) {
            Log.d("out,left", "closest");
            //message.db("outleft, closest");
            return -(left.getX() + left.measureWidth() / 2 - buffer);
        }
        return super.outLeft();
    }

    @Override
    protected float outBottom() {
        Physical bot = bottumest();//getCenterEq();
        Physical top = topest();
        if (bot.getY() - (bot.measureHeight()/2) + buffer > buttonLine() && top.getY() - (top.measureHeight()/2) > (height*(3f/4f))) {
            Log.d("out,bot", "closest");
            //message.db("outbot, closest");
            return (bot.getY() - (bot.measureHeight()/2) + buffer) - buttonLine();
        }
        return super.outBottom();
    }

    @Override
    protected float outRight() {
        Physical left = leftest();//getCenterEq();
        Physical right = rightest();
        if (right.getX() - (right.measureWidth() / 2) + buffer > width && left.getX() - (left.measureWidth() / 2 )- buffer > 0) {
            Log.d("out,right", "closest");
            //message.db("outright, closest");
            return (right.getX() - (right.measureWidth() / 2) + buffer) - width;
        }
        return super.outRight();
    }

    public boolean tryWarn(Equation equation) {
        if (changedEq == null) {
            Equation warnEq = equation.CouldBeZero();
            if (warnEq != null) {
                changedEq = warnEq;
            }
        }else{
            Equation warnEq = equation.CouldBeZero();
            if (warnEq != null) {
                if (!(changedEq instanceof MultiEquation)){
                    Equation old = changedEq;
                    changedEq = new MultiEquation(this);
                    changedEq.add(old);

                }
                if (warnEq instanceof MultiEquation) {
                    changedEq.addAll(warnEq);
                }else{
                    changedEq.add(warnEq);
                }
            }
        }

        return false;
    }

    private boolean alreadySolved = false;
    public boolean isSolved() {
        if (alreadySolved){
            return true;
        }
        if (stupid.get(0) instanceof VarEquation){
            return Operations.sortaNumber(stupid.get(1)) || isNumDiv(stupid.get(1));
        }else if(stupid.get(1) instanceof VarEquation){
            return Operations.sortaNumber(stupid.get(0)) || isNumDiv(stupid.get(0));
        }
        return false;
    }

    private boolean isNumDiv(Equation eq){
        return (eq instanceof DivEquation && Operations.sortaNumber(eq.get(0)) && Operations.sortaNumber(eq.get(1)));
    }

    public void showSolvedMessage(){
        Log.i("show solved message","trying");
        Random r = new Random();
        int i = Math.abs(r.nextInt())%4;
        if (i==0) {
            this.message.enQue(TutMessage.shortTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_1_0));
        }else if (i==1){
            this.message.enQue(TutMessage.shortTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_1_1));
        }else if (i==2){
            this.message.enQue(TutMessage.shortTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_1_2));
        }else if (i==3){
            this.message.enQue(TutMessage.shortTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_1_3));
        }
        alreadySolved = true;
        ((SolvedTut)TutMessage.getMessage(SolvedTut.class)).okToShow = true;
    }
}
