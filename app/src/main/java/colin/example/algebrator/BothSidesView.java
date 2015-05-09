package colin.example.algebrator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import colin.example.algebrator.Actions.BothSides.CancelAction;
import colin.example.algebrator.Actions.BothSides.CheckAction;
import colin.example.algebrator.Actions.WriteScreen.DecimalAction;
import colin.example.algebrator.Actions.WriteScreen.DeleteAction;
import colin.example.algebrator.Actions.WriteScreen.DivAction;
import colin.example.algebrator.Actions.WriteScreen.MinusAction;
import colin.example.algebrator.Actions.WriteScreen.NumberAction;
import colin.example.algebrator.Actions.WriteScreen.ParenthesesAction;
import colin.example.algebrator.Actions.WriteScreen.PlusAction;
import colin.example.algebrator.Actions.WriteScreen.PowerAction;
import colin.example.algebrator.Actions.WriteScreen.SqrtAction;
import colin.example.algebrator.Actions.WriteScreen.TimesAction;
import colin.example.algebrator.Actions.WriteScreen.VarAction;

/**
 * Created by Colin_000 on 3/28/2015.
 */
public class BothSidesView extends EmilyView {

    public void setOGmodie(Equation newOGModie) {
        OGmodie = newOGModie;
        history.add(new EquationButton(OGmodie, this));
    }

    private Equation OGmodie;

    public BothSidesMode getBothSidesMode(){
        return myBothSidesMode;
    }

    public void setUp(BothSidesMode myBothSidesMode, Equation mine) {
        this.myBothSidesMode  =myBothSidesMode;

        setOGmodie(mine);
        if (mine instanceof EqualsEquation){
            if (myBothSidesMode == BothSidesMode.ADD){
                history.add(new EquationButton(new VarEquation( getResources().getString(R.string.bothsides_equals_add),this), this));
            }else if (myBothSidesMode == BothSidesMode.SUB){
                history.add(new EquationButton(new VarEquation(getResources().getString(R.string.bothsides_equals_sub),this), this));
            }else if (myBothSidesMode == BothSidesMode.MULTI){
                history.add(new EquationButton(new VarEquation(getResources().getString(R.string.bothsides_equals_multi),this), this));
            }else if (myBothSidesMode == BothSidesMode.DIV){
                history.add(new EquationButton(new VarEquation(getResources().getString(R.string.bothsides_equals_div),this), this));
            }else if (myBothSidesMode == BothSidesMode.POWER){
                history.add(new EquationButton(new VarEquation(getResources().getString(R.string.bothsides_equals_power),this), this));
            }
        }else{
            if (myBothSidesMode == BothSidesMode.ADD){
                history.add(new EquationButton(new VarEquation(getResources().getString(R.string.bothsides_add),this), this));
            }else if (myBothSidesMode == BothSidesMode.SUB){
                history.add(new EquationButton(new VarEquation(getResources().getString(R.string.bothsides_sub),this), this));
            }else if (myBothSidesMode == BothSidesMode.MULTI){
                history.add(new EquationButton(new VarEquation(getResources().getString(R.string.bothsides_multi),this), this));
            }else if (myBothSidesMode == BothSidesMode.DIV){
                history.add(new EquationButton(new VarEquation(getResources().getString(R.string.bothsides_div),this), this));
            }else if (myBothSidesMode == BothSidesMode.POWER){
                history.add(new EquationButton(new VarEquation(getResources().getString(R.string.bothsides_power),this), this));
            }
        }
    }

    public enum BothSidesMode {ADD, SUB, MULTI, DIV, POWER}


    private BothSidesMode myBothSidesMode;

    public BothSidesView(Context context) {
        super(context);
        init(context);
    }

    protected void init(Context context) {
        skipZero = false;
        BASE_BUTTON_PERCENT = 4f / 6f;
        // we want our equation to be a litle closer together
        baseBuffer = 60 * Algebrator.getAlgebrator().getDpi();
        offsetY = baseBuffer;
        buttonsPercent = BASE_BUTTON_PERCENT;
    }

    public BothSidesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BothSidesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    public void initEq() {
        super.initEq();
        updateModie();
    }

    public Equation makeModie(ArrayList<Equation> toBothSides) {
        Equation modie = OGmodie.copy();


        if (myBothSidesMode == BothSidesMode.ADD) {

            if (modie instanceof EqualsEquation) {
                for (int i = 0; i < 2; i++) {
                    if (!(modie.get(i) instanceof AddEquation)) {
                        Equation oldEq = modie.get(i);
                        Equation toAdd = new AddEquation(this);
                        modie.get(i).replace(toAdd);
                        toAdd.add(oldEq);
                    }
                    modie.get(i).add(toBothSides.get(i));
                }
            } else {
                if (!(modie instanceof AddEquation)) {
                    Equation oldEq = modie;
                    modie = new AddEquation(this);
                    modie.add(oldEq);
                }
                modie.add(toBothSides.get(0));
            }


        } else if (myBothSidesMode == BothSidesMode.SUB) {
            if (modie instanceof EqualsEquation) {
                for (int i = 0; i < 2; i++) {
                    if (!(modie.get(i) instanceof AddEquation)) {
                        Equation oldEq = modie.get(i);
                        Equation toAdd = new AddEquation(this);
                        modie.get(i).replace(toAdd);
                        toAdd.add(oldEq);
                    }
                    modie.get(i).add(toBothSides.get(i).negate());
                }
            } else {
                if (!(modie instanceof AddEquation)) {
                    Equation oldEq = modie;
                    modie = new AddEquation(this);
                    modie.add(oldEq);
                }
                modie.add(toBothSides.get(0).negate());
            }


        } else if (myBothSidesMode == BothSidesMode.MULTI) {
            if (modie instanceof EqualsEquation) {
                for (int i = 0; i < 2; i++) {
                    if (!(modie.get(i) instanceof MultiEquation)) {
                        Equation oldEq = modie.get(i);
                        Equation toAdd = new MultiEquation(this);
                        modie.get(i).replace(toAdd);
                        toAdd.add(oldEq);
                    }
                    modie.get(i).add(toBothSides.get(i));
                }
            } else {
                if (!(modie instanceof MultiEquation)) {
                    Equation oldEq = modie;
                    modie = new MultiEquation(this);
                    modie.add(oldEq);
                }
                modie.add(toBothSides.get(0));
            }


        } else if (myBothSidesMode == BothSidesMode.DIV) {
            if (modie instanceof EqualsEquation) {
                for (int i = 0; i < 2; i++) {

                    Equation oldEq = modie.get(i);
                    Equation toAdd = new DivEquation(this);
                    modie.get(i).replace(toAdd);
                    toAdd.add(oldEq);
                    modie.get(i).add(toBothSides.get(i));
                }
            } else {
                Equation oldEq = modie;
                modie = new DivEquation(this);
                modie.add(oldEq);
                modie.add(toBothSides.get(0));
            }
        } else if (myBothSidesMode == BothSidesMode.POWER) {
            if (modie instanceof EqualsEquation) {
                for (int i = 0; i < 2; i++) {
                    Equation oldEq = modie.get(i);
                    Equation toAdd = new PowerEquation(this);
                    modie.get(i).replace(toAdd);
                    toAdd.add(oldEq);
                    modie.get(i).add(toBothSides.get(i));
                }
            } else {
                Equation oldEq = modie;
                modie = new PowerEquation(this);
                modie.add(oldEq);
                modie.add(toBothSides.get(0));
            }
        }


        return modie;
    }

    private ArrayList<Equation> getAddToBothSides() {
        ArrayList<Equation> toBothSides = new ArrayList<>();
        toBothSides.add(convert(stupid.copy()));
        toBothSides.add(convert(stupid.copy()));
       for (Equation e : toBothSides) {
           if (myBothSidesMode == BothSidesMode.MULTI &&
                   e.get(0) instanceof WritingPraEquation && ((WritingPraEquation)e.get(0)).left &&
                   e.get(e.size() -1) instanceof WritingPraEquation && !((WritingPraEquation)e.get(e.size() -1)).left  ){
               e.remove(0);
               e.remove(e.size() -1);
           }
//            e.demo = true;
//            e.bkgAlpha = 0xff;
       }
        return toBothSides;
    }

    private Equation convert(Equation eq) {
        // we want to remove |
//        if (eq instanceof WritingEquation && eq.size() ==1){
//            return null;
//        }else{
        Equation toReturn = removePlaceHolder(eq);
        Log.i("what we got?", toReturn.toString());
        if ((toReturn instanceof WritingEquation && toReturn.size() != 1) && (myBothSidesMode == BothSidesMode.MULTI || myBothSidesMode == BothSidesMode.SUB)) {
            toReturn.add(0, new WritingPraEquation(true, this));
            toReturn.add(new WritingPraEquation(false, this));
        }

        return toReturn;
//        }
    }

    private Equation removePlaceHolder(Equation eq) {
        if (eq instanceof PlaceholderEquation) {
            if (eq.parent instanceof BinaryEquation ||
                    eq.parent.size() == 1 ||
                    (eq.left() != null && eq.left().isOpLeft()) && (eq.right() == null || eq.right().isOpRight()) ||
                    (eq.right() != null && eq.right().isOpRight()) && (eq.left() == null || eq.left().isOpLeft())) {
                eq.replace(new VarEquation("?", this));
            } else {
                eq.remove();
            }
        } else {
            // we iterate backword so we can remove
            for (int i = eq.size() - 1; -1 < i; i--) {
                removePlaceHolder(eq.get(i));
            }
        }
        return eq;
    }

//    private Equation getModie() {
//        return history.get(0).myEq;
//    }

    private Equation setModie(Equation newModie) {
        return history.get(0).myEq = newModie;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean toReturn = super.onTouch(view, event);
        // this is called a lot
        // maybe we can limit our updates of modie
        updateModie();
        return toReturn;
    }

    private void updateModie() {
        setModie(makeModie(getAddToBothSides()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        onDrawAfter(canvas);
    }

    @Override
    protected void addButtons() {
        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button(this, "7", new NumberAction(this, "7")));
        firstRow.add(new Button(this, "8", new NumberAction(this, "8")));
        firstRow.add(new Button(this, "9", new NumberAction(this, "9")));
        firstRow.add(new Button(this, "a", new VarAction(this, "a")));
        firstRow.add(new Button(this, "b", new VarAction(this, "b")));
        firstRow.add(new Button(this, "+", new PlusAction(this)));
        firstRow.add(new Button(this, "-", new MinusAction(this)));

        //TODO this does not work since my font does not support this


        ArrayList<Button> secondRow = new ArrayList<Button>();
        secondRow.add(new Button(this, "4", new NumberAction(this, "4")));
        secondRow.add(new Button(this, "5", new NumberAction(this, "5")));
        secondRow.add(new Button(this, "6", new NumberAction(this, "6")));
        secondRow.add(new Button(this, "(", new ParenthesesAction(this, true)));
        secondRow.add(new Button(this, ")", new ParenthesesAction(this, false)));
        char[] timesUnicode = {'\u00D7'};
        secondRow.add(new Button(this, new String(timesUnicode), new TimesAction(this)));
        char[] divisionUnicode = {'\u00F7'};
        secondRow.add(new Button(this, new String(divisionUnicode), new DivAction(this)));


        ArrayList<Button> thridRow = new ArrayList<Button>();
        thridRow.add(new Button(this, "1", new NumberAction(this, "1")));
        thridRow.add(new Button(this, "2", new NumberAction(this, "2")));
        thridRow.add(new Button(this, "3", new NumberAction(this, "3")));
        thridRow.add(new Button(this, "0", new NumberAction(this, "0")));
        thridRow.add(new Button(this, ".", new DecimalAction(this, ".")));
        thridRow.add(new Button(this, "cⁿ", new PowerAction(this)));
        char[] sqrtUnicode = {'\u221A'};
        thridRow.add(new Button(this, new String(sqrtUnicode), new SqrtAction(this)));




        addButtonsRow(firstRow, 0f, 7f / 9f,6f / 9f, 7f / 9f);
        char[] backSpaceUnicode = {'\u232B'};
        Button del = new Button(this, new String(backSpaceUnicode), new DeleteAction(this));
        Typeface myTypeface = Typeface.createFromAsset(Algebrator.getAlgebrator().getAssets(), "fonts/DejaVuSans.ttf");
        del.textPaint.setTypeface(myTypeface);
        del.setLocation(7f / 9f, 1f, 6f / 9f, 7f / 9f);
        buttons.add(del);

        addButtonsRow(secondRow, 0f, 7f / 9f, 7f / 9f, 8f / 9f);
        Button solve = new Button(this, getResources().getString(R.string.ok), new CheckAction(this));
        solve.setLocation(7f / 9f, 1f, 7f / 9f, 8f / 9f);
        buttons.add(solve);
        addButtonsRow(thridRow, 0f, 7f / 9f,8f / 9f, 9f / 9f);

        Button cancel =new Button(this, getResources().getString(R.string.cancel), new CancelAction(this));
        cancel.setLocation(7f / 9f, 1f, 8f / 9f, 9f / 9f);
        buttons.add(cancel);
    }
}
