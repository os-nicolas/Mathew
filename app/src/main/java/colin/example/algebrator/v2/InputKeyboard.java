package colin.example.algebrator.v2;

import android.graphics.Typeface;

import java.util.ArrayList;

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
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.Button;
import colin.example.algebrator.PopUpButton;
import colin.example.algebrator.R;

/**
 * Created by Colin_000 on 5/7/2015.
 */
public class InputKeyboard extends KeyBoard {
    @Override
    protected void addButtons() {

        popUpButtons.add(new PopUpButton(this,Algebrator.getAlgebrator().getResources().getString(R.string.clear)  ,new ClearAction(this)));

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
        Button solve = new Button(this,Algebrator.getAlgebrator().getResources().getString(R.string.solve), new Solve(this));
        solve.setLocation(7f / 9f, 1f, 7f / 9f, 8f / 9f);
        buttons.add(solve);
        addButtonsRow(thridRow, 8f / 9f, 9f / 9f);
    }

    @Override
    public float getBaseButtonsPercent() {
        return 1/3f;
    }
}
