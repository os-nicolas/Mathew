package cube.d.n.commoncore.v2.keyboards;

import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.BothSides.CancelAction;
import cube.d.n.commoncore.Action.BothSides.CheckAction;
import cube.d.n.commoncore.Action.WriteScreen.DecimalAction;
import cube.d.n.commoncore.Action.WriteScreen.DeleteAction;
import cube.d.n.commoncore.Action.WriteScreen.DivAction;
import cube.d.n.commoncore.Action.WriteScreen.MinusAction;
import cube.d.n.commoncore.Action.WriteScreen.NumberAction;
import cube.d.n.commoncore.Action.WriteScreen.ParenthesesAction;
import cube.d.n.commoncore.Action.WriteScreen.PlusAction;
import cube.d.n.commoncore.Action.WriteScreen.PowerAction;
import cube.d.n.commoncore.Action.WriteScreen.SqrtAction;
import cube.d.n.commoncore.Action.WriteScreen.TimesAction;
import cube.d.n.commoncore.Action.WriteScreen.VarAction;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.R;
import cube.d.n.commoncore.v2.Main;
import cube.d.n.commoncore.v2.lines.BothSidesLine;
import cube.d.n.commoncore.v2.lines.Line;

/**
 * Created by Colin_000 on 5/11/2015.
 */
public class BothSidesKeyBoard extends KeyBoard {
    public BothSidesKeyBoard(Main owner, BothSidesLine line) {
        super(owner, line);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        boolean result = super.onTouch(event);
        ((BothSidesLine)line).updateModie();
        return result;
    }



    @Override
    protected void addButtons() {
        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button( "7", new NumberAction((BothSidesLine)line, "7")));
        firstRow.add(new Button( "8", new NumberAction((BothSidesLine)line, "8")));
        firstRow.add(new Button( "9", new NumberAction((BothSidesLine)line, "9")));
        firstRow.add(new Button( "a", new VarAction((BothSidesLine)line, "a")));
        firstRow.add(new Button( "b", new VarAction((BothSidesLine)line, "b")));
        firstRow.add(new Button( "+", new PlusAction((BothSidesLine)line)));
        firstRow.add(new Button( "-", new MinusAction((BothSidesLine)line)));

        //TODO this does not work since my font does not support this


        ArrayList<Button> secondRow = new ArrayList<Button>();
        secondRow.add(new Button( "4", new NumberAction((BothSidesLine)line, "4")));
        secondRow.add(new Button( "5", new NumberAction((BothSidesLine)line, "5")));
        secondRow.add(new Button( "6", new NumberAction((BothSidesLine)line, "6")));
        secondRow.add(new Button( "(", new ParenthesesAction((BothSidesLine)line, true)));
        secondRow.add(new Button( ")", new ParenthesesAction((BothSidesLine)line, false)));
        char[] timesUnicode = {'\u00D7'};
        secondRow.add(new Button( new String(timesUnicode), new TimesAction((BothSidesLine)line)));
        char[] divisionUnicode = {'\u00F7'};
        secondRow.add(new Button( new String(divisionUnicode), new DivAction((BothSidesLine)line)));


        ArrayList<Button> thridRow = new ArrayList<Button>();
        thridRow.add(new Button( "1", new NumberAction((BothSidesLine)line, "1")));
        thridRow.add(new Button( "2", new NumberAction((BothSidesLine)line, "2")));
        thridRow.add(new Button( "3", new NumberAction((BothSidesLine)line, "3")));
        thridRow.add(new Button( "0", new NumberAction((BothSidesLine)line, "0")));
        thridRow.add(new Button( ".", new DecimalAction((BothSidesLine)line, ".")));
        thridRow.add(new Button( "cⁿ", new PowerAction((BothSidesLine)line)));
        char[] sqrtUnicode = {'\u221A'};
        thridRow.add(new Button( new String(sqrtUnicode), new SqrtAction((BothSidesLine)line)));




        addButtonsRow(firstRow, 0f, 7f / 9f,6f / 9f, 7f / 9f);
        char[] backSpaceUnicode = {'\u232B'};
        Button del = new Button( new String(backSpaceUnicode), new DeleteAction((BothSidesLine)line));
        Typeface myTypeface = Typeface.createFromAsset(BaseApp.getApp().getAssets(), "fonts/DejaVuSans.ttf");
        del.textPaint.setTypeface(myTypeface);
        del.setLocation(7f / 9f, 1f, 6f / 9f, 7f / 9f);
        buttons.add(del);

        addButtonsRow(secondRow, 0f, 7f / 9f, 7f / 9f, 8f / 9f);
        Button solve = new Button( BaseApp.getApp().getResources().getString(R.string.ok), new CheckAction((BothSidesLine)line));
        solve.setLocation(7f / 9f, 1f, 7f / 9f, 8f / 9f);
        buttons.add(solve);
        addButtonsRow(thridRow, 0f, 7f / 9f,8f / 9f, 9f / 9f);

        Button cancel =new Button( BaseApp.getApp().getResources().getString(R.string.cancel), new CancelAction((BothSidesLine)line));
        cancel.setLocation(7f / 9f, 1f, 8f / 9f, 9f / 9f);
        buttons.add(cancel);
    }

    @Override
    public float getBaseButtonsPercent() {
        return 1/3f;
    }
}
