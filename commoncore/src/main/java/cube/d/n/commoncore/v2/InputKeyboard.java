package cube.d.n.commoncore.v2;

import android.graphics.Typeface;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.WriteScreen.ClearAction;
import cube.d.n.commoncore.Action.WriteScreen.DecimalAction;
import cube.d.n.commoncore.Action.WriteScreen.DeleteAction;
import cube.d.n.commoncore.Action.WriteScreen.DivAction;
import cube.d.n.commoncore.Action.WriteScreen.EqualsAction;
import cube.d.n.commoncore.Action.WriteScreen.LeftAction;
import cube.d.n.commoncore.Action.WriteScreen.MinusAction;
import cube.d.n.commoncore.Action.WriteScreen.NumberAction;
import cube.d.n.commoncore.Action.WriteScreen.ParenthesesAction;
import cube.d.n.commoncore.Action.WriteScreen.PlusAction;
import cube.d.n.commoncore.Action.WriteScreen.PowerAction;
import cube.d.n.commoncore.Action.WriteScreen.RightAction;
import cube.d.n.commoncore.Action.WriteScreen.Solve;
import cube.d.n.commoncore.Action.WriteScreen.SqrtAction;
import cube.d.n.commoncore.Action.WriteScreen.TimesAction;
import cube.d.n.commoncore.Action.WriteScreen.VarAction;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.PopUpButton;


/**
 * Created by Colin_000 on 5/7/2015.
 */
public class InputKeyboard extends KeyBoard {

    private final InputLine line;

    public InputKeyboard(Main owner,InputLine line){
        super(owner);
        this.line = line;
    }

    @Override
    protected void addButtons() {



        popUpButtons.add(new PopUpButton("clear" ,new ClearAction(line)));

        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button("7", new NumberAction(line, "7")));
        firstRow.add(new Button("8", new NumberAction(line, "8")));
        firstRow.add(new Button("9", new NumberAction(line, "9")));
        firstRow.add(new Button("a", new VarAction(line, "a")));
        firstRow.add(new Button("b", new VarAction(line, "b")));
        firstRow.add(new Button("+", new PlusAction(line)));
        firstRow.add(new Button("-", new MinusAction(line)));
        firstRow.add(new Button("=", new EqualsAction(line)));
        //TODO this does not work since my font does not support this
        char[] backSpaceUnicode = { '\u232B'};
        Button del = new Button(new String(backSpaceUnicode), new DeleteAction(line));
        Typeface myTypeface = Typeface.createFromAsset(BaseApp.getApp().getAssets(), "fonts/DejaVuSans.ttf");
        del.textPaint.setTypeface(myTypeface);
        firstRow.add(del);//

        ArrayList<Button> secondRow = new ArrayList<Button>();
        secondRow.add(new Button("4", new NumberAction(line, "4")));
        secondRow.add(new Button("5", new NumberAction(line, "5")));
        secondRow.add(new Button("6", new NumberAction(line, "6")));
        secondRow.add(new Button("(", new ParenthesesAction(line, true)));
        secondRow.add(new Button(")", new ParenthesesAction(line, false)));
        char[] timesUnicode = {'\u00D7'};
        secondRow.add(new Button(new String(timesUnicode), new TimesAction(line)));
        char[] divisionUnicode = {'\u00F7'};
        secondRow.add(new Button(new String(divisionUnicode), new DivAction(line)));


        ArrayList<Button> thridRow = new ArrayList<Button>();
        thridRow.add(new Button("1", new NumberAction(line, "1")));
        thridRow.add(new Button("2", new NumberAction(line, "2")));
        thridRow.add(new Button("3", new NumberAction(line, "3")));
        thridRow.add(new Button("0", new NumberAction(line, "0")));
        thridRow.add(new Button(".", new DecimalAction(line, ".")));
        thridRow.add(new Button("cⁿ", new PowerAction(line)));
        char[] sqrtUnicode = {'\u221A'};
        thridRow.add(new Button(new String(sqrtUnicode), new SqrtAction(line)));
        char[] leftUnicode = {'\u2190'};
        thridRow.add(new Button(new String(leftUnicode), new LeftAction(line)));
        char[] rightUnicode = {'\u2192'};
        thridRow.add(new Button(new String(rightUnicode), new RightAction(line)));

        addButtonsRow(firstRow, 6f / 9f, 7f / 9f);
        addButtonsRow(secondRow, 0f, 7f / 9f, 7f / 9f, 8f / 9f);
        Button solve = new Button("Solve", new Solve(line));
        solve.setLocation(7f / 9f, 1f, 7f / 9f, 8f / 9f);
        buttons.add(solve);
        addButtonsRow(thridRow, 8f / 9f, 9f / 9f);
    }

    @Override
    public float getBaseButtonsPercent() {
        return 1/3f;
    }
}
