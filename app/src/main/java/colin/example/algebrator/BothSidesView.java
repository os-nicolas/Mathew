package colin.example.algebrator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.lang.reflect.Type;
import java.util.ArrayList;

import colin.algebrator.eq.AddEquation;
import colin.algebrator.eq.DivEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.MultiEquation;
import colin.example.algebrator.Actions.SovleScreen.BothSides;
import colin.example.algebrator.Actions.WriteScreen.ClearAction;
import colin.example.algebrator.Actions.WriteScreen.DecimalAction;
import colin.example.algebrator.Actions.WriteScreen.DeleteAction;
import colin.example.algebrator.Actions.WriteScreen.DivAction;
import colin.example.algebrator.Actions.WriteScreen.EqualsAction;
import colin.example.algebrator.Actions.WriteScreen.LeftAction;
import colin.example.algebrator.Actions.WriteScreen.MinusAction;
import colin.example.algebrator.Actions.WriteScreen.NumberAction;
import colin.example.algebrator.Actions.WriteScreen.ParenthesesAction;
import colin.example.algebrator.Actions.WriteScreen.PlusAction;
import colin.example.algebrator.Actions.WriteScreen.PowerAction;
import colin.example.algebrator.Actions.WriteScreen.RightAction;
import colin.example.algebrator.Actions.WriteScreen.Solve;
import colin.example.algebrator.Actions.WriteScreen.SqrtAction;
import colin.example.algebrator.Actions.WriteScreen.TimesAction;
import colin.example.algebrator.Actions.WriteScreen.VarAction;

/**
 * Created by Colin_000 on 3/28/2015.
 */
public class BothSidesView extends EmilyView {

    public enum BothSidesMode {ADD,SUB,MULTI,DIV};

    BothSidesMode myBothSidesMode = BothSidesMode.ADD;

    public BothSidesView(Context context) {
        super(context);
        init(context);
    }

    protected void init(Context context) {
        skipZero = false;
        BASE_BUTTON_PERCENT = 4f / 6f;
        buttonsPercent = BASE_BUTTON_PERCENT;
    }

    public BothSidesView(Context context, AttributeSet attrs) {
        super(context, attrs);init(context);
    }

    public BothSidesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
@Override
    public void initEq() {
        super.initEq();
        Equation modie = getModie();

        if (myBothSidesMode == BothSidesMode.ADD){

            for (int i =0;i<2;i++){
                if (!(modie.get(i) instanceof  AddEquation)){
                    Equation oldEq = modie.get(i);
                    Equation toAdd = new AddEquation(this);
                    modie.get(i).replace(toAdd);
                    toAdd.add(oldEq);
                }
                modie.get(i).add(stupid);
            }


        }else if (myBothSidesMode == BothSidesMode.SUB){

            for (int i =0;i<2;i++){
                if (!(modie.get(i) instanceof  AddEquation))
                {
                    Equation oldEq = modie.get(i);
                    Equation toAdd = new AddEquation(this);
                    modie.get(i).replace(toAdd);
                    toAdd.add(oldEq);
                }
                modie.get(i).add(stupid);
            }


        }else if (myBothSidesMode == BothSidesMode.MULTI){

            for (int i =0;i<2;i++){
                if (!(modie.get(i) instanceof MultiEquation)){
                    Equation oldEq = modie.get(i);
                    Equation toAdd = new MultiEquation(this);
                    modie.get(i).replace(toAdd);
                    toAdd.add(oldEq);
                }
                modie.get(i).add(stupid);
            }


        }else if (myBothSidesMode == BothSidesMode.DIV){

            for (int i =0;i<2;i++){
                if (!(modie.get(i) instanceof DivEquation)){
                    Equation oldEq = modie.get(i);
                    Equation toAdd = new DivEquation(this);
                    modie.get(i).replace(toAdd);
                    toAdd.add(oldEq);
                }
                modie.get(i).add(stupid);
            }


        }

    }

    private Equation getModie() {
        return history.get(0).myEq;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHistory(canvas);

        onDrawAfter(canvas);
    }

    @Override
    protected void addButtons() {
        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button(this,"7", new NumberAction(this, "7")));
        firstRow.add(new Button(this,"8", new NumberAction(this, "8")));
        firstRow.add(new Button(this,"9", new NumberAction(this, "9")));
        firstRow.add(new Button(this,"a", new VarAction(this, "a")));
        firstRow.add(new Button(this,"b", new VarAction(this, "b")));
        firstRow.add(new Button(this,"+", new PlusAction(this)));
        firstRow.add(new Button(this,"-", new MinusAction(this)));
        firstRow.add(new Button(this,"=", new EqualsAction(this)));
        //TODO this does not work since my font does not support this
        char[] backSpaceUnicode = { '\u232B'};
        Button del = new Button(this,new String(backSpaceUnicode), new DeleteAction(this));
        Typeface myTypeface = Typeface.createFromAsset(Algebrator.getAlgebrator().getAssets(), "fonts/DejaVuSans.ttf");
        del.textPaint.setTypeface(myTypeface);
        firstRow.add(del);//

        ArrayList<Button> secondRow = new ArrayList<Button>();
        secondRow.add(new Button(this,"4", new NumberAction(this, "4")));
        secondRow.add(new Button(this,"5", new NumberAction(this, "5")));
        secondRow.add(new Button(this,"6", new NumberAction(this, "6")));
        secondRow.add(new Button(this,"(", new ParenthesesAction(this, true)));
        secondRow.add(new Button(this,")", new ParenthesesAction(this, false)));
        char[] timesUnicode = {'\u00D7'};
        secondRow.add(new Button(this,new String(timesUnicode), new TimesAction(this)));
        char[] divisionUnicode = {'\u00F7'};
        secondRow.add(new Button(this,new String(divisionUnicode), new DivAction(this)));


        ArrayList<Button> thridRow = new ArrayList<Button>();
        thridRow.add(new Button(this,"1", new NumberAction(this, "1")));
        thridRow.add(new Button(this,"2", new NumberAction(this, "2")));
        thridRow.add(new Button(this,"3", new NumberAction(this, "3")));
        thridRow.add(new Button(this,"0", new NumberAction(this, "0")));
        thridRow.add(new Button(this,".", new DecimalAction(this, ".")));
        thridRow.add(new Button(this,"cⁿ", new PowerAction(this)));
        char[] sqrtUnicode = {'\u221A'};
        thridRow.add(new Button(this,new String(sqrtUnicode), new SqrtAction(this)));
        char[] leftUnicode = {'\u2190'};
        thridRow.add(new Button(this,new String(leftUnicode), new LeftAction(this)));
        char[] rightUnicode = {'\u2192'};
        thridRow.add(new Button(this,new String(rightUnicode), new RightAction(this)));

        addButtonsRow(firstRow, 6f / 9f, 7f / 9f);
        addButtonsRow(secondRow, 0f, 7f / 9f, 7f / 9f, 8f / 9f);
        Button solve = new Button(this,getResources().getString(R.string.solve), new Solve(this));
        solve.setLocation(7f / 9f, 1f, 7f / 9f, 8f / 9f);
        buttons.add(solve);
        addButtonsRow(thridRow, 8f / 9f, 9f / 9f);
    }
}