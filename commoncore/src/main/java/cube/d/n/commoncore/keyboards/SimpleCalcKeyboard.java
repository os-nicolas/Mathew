package cube.d.n.commoncore.keyboards;

import android.graphics.Typeface;

import java.util.ArrayList;

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
import cube.d.n.commoncore.Action.WriteScreen.SqrtAction;
import cube.d.n.commoncore.Action.WriteScreen.TimesAction;
import cube.d.n.commoncore.Action.WriteScreen.VarAction;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.EmptyButton;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.R;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.CalcLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 5/21/2015.
 */
public class SimpleCalcKeyboard extends KeyBoard {
    public SimpleCalcKeyboard(Main owner, CalcLine calcLine) {
        super(owner,calcLine);
    }

    @Override
    protected void addButtons() {
//popUpButtons.add(new PopUpButton("clear" ,new ClearAction((InputLine)line)));

        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button("7", new NumberAction((InputLine)line, "7")));
        firstRow.add(new Button("8", new NumberAction((InputLine)line, "8")));
        firstRow.add(new Button("9", new NumberAction((InputLine)line, "9")));
        firstRow.add(new EmptyButton());
        firstRow.add(new EmptyButton());
        firstRow.add(new Button("+", new PlusAction((InputLine)line)));
        firstRow.add(new Button("-", new MinusAction((InputLine)line)));

        addButtonsRow(firstRow, 0f, 7f / 9f,6f / 9f, 7f / 9f);
        char[] backSpaceUnicode = {'\u232B'};
        Button del = new Button( new String(backSpaceUnicode), new DeleteAction((CalcLine)line));
        Typeface myTypeface = Typeface.createFromAsset(BaseApp.getApp().getAssets(), "fonts/DejaVuSans.ttf");
        del.textPaint.setTypeface(myTypeface);
        del.setLocation(7f / 9f, 1f, 6f / 9f, 7f / 9f);
        buttons.add(del);


        ArrayList<Button> secondRow = new ArrayList<Button>();
        secondRow.add(new Button("4", new NumberAction((InputLine)line, "4")));
        secondRow.add(new Button("5", new NumberAction((InputLine)line, "5")));
        secondRow.add(new Button("6", new NumberAction((InputLine)line, "6")));
        secondRow.add(new Button("(", new ParenthesesAction((InputLine)line, true)));
        secondRow.add(new Button(")", new ParenthesesAction((InputLine)line, false)));
        char[] timesUnicode = {'\u00D7'};
        secondRow.add(new Button(new String(timesUnicode), new TimesAction((InputLine)line)));
        char[] divisionUnicode = {'\u00F7'};
        secondRow.add(new Button(new String(divisionUnicode), new DivAction((InputLine)line)));
        char[] leftUnicode = {'\u2190'};
        secondRow.add(new Button(new String(leftUnicode), new LeftAction((InputLine)line)));
        char[] rightUnicode = {'\u2192'};
        secondRow.add(new Button(new String(rightUnicode), new RightAction((InputLine)line)));

        addButtonsRow(secondRow, 7f / 9f, 8f / 9f);

        ArrayList<Button> thridRow = new ArrayList<Button>();
        thridRow.add(new Button("1", new NumberAction((InputLine)line, "1")));
        thridRow.add(new Button("2", new NumberAction((InputLine)line, "2")));
        thridRow.add(new Button("3", new NumberAction((InputLine)line, "3")));
        thridRow.add(new Button("0", new NumberAction((InputLine)line, "0")));
        thridRow.add(new Button(".", new DecimalAction((InputLine)line, ".")));
        thridRow.add(new Button("cⁿ", new PowerAction((InputLine)line)));
        char[] sqrtUnicode = {'\u221A'};
        thridRow.add(new Button(new String(sqrtUnicode), new SqrtAction((InputLine)line)));

        addButtonsRow(thridRow,0f, 7f / 9f,  8f / 9f, 9f / 9f);
        Button solve = new Button(BaseApp.getApp().getResources().getString(R.string.enter), BaseApp.getApp().getEnter((InputLine) line)  );
        solve.setLocation(7f / 9f, 1f, 8f / 9f, 9f / 9f);
        buttons.add(solve);
    }

    @Override
    public float getBaseButtonsPercent() {
        return 1/3f;
    }
}
