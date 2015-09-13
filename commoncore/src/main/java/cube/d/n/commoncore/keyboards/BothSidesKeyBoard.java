package cube.d.n.commoncore.keyboards;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.ArrayList;


import cube.d.n.commoncore.Action.BothSides.CancelAction;
import cube.d.n.commoncore.Action.BothSides.CheckAction;
import cube.d.n.commoncore.Action.SuperAction;
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
import cube.d.n.commoncore.EmptyButton;
import cube.d.n.commoncore.R;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.InlineInputLine;

/**
 * Created by Colin_000 on 5/11/2015.
 */
public class BothSidesKeyBoard extends KeyBoard {
    public BothSidesKeyBoard(Main owner, InlineInputLine line) {
        super(owner, line);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        boolean result = super.onTouch(event);
        ((InlineInputLine)line).updateModie();
        return result;
    }



    @Override
    protected void addButtons() {
        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button( "7", new NumberAction((InlineInputLine)line, "7")));
        firstRow.add(new Button( "8", new NumberAction((InlineInputLine)line, "8")));
        firstRow.add(new Button( "9", new NumberAction((InlineInputLine)line, "9")));
        firstRow.add(new Button(owner.modeController.getVar1(), new VarAction((InlineInputLine)line, owner.modeController.getVar1())));
        firstRow.add((BaseApp.getApp().hasB()?
                new Button( owner.modeController.getVar2(), new VarAction((InlineInputLine)line, owner.modeController.getVar2())):
                new EmptyButton()
        ));
        firstRow.add(new Button( "+", new PlusAction((InlineInputLine)line)).withColor(BaseApp.getApp().darkLightColor));
        firstRow.add(new Button( "-", new MinusAction((InlineInputLine)line)).withColor(BaseApp.getApp().darkLightColor));

        //TODO this does not work since my font does not support this


        ArrayList<Button> secondRow = new ArrayList<Button>();
        secondRow.add(new Button( "4", new NumberAction((InlineInputLine)line, "4")));
        secondRow.add(new Button( "5", new NumberAction((InlineInputLine)line, "5")));
        secondRow.add(new Button( "6", new NumberAction((InlineInputLine)line, "6")));
        secondRow.add(new Button( "(", new ParenthesesAction((InlineInputLine)line, true)));
        secondRow.add(new Button( ")", new ParenthesesAction((InlineInputLine)line, false)));
        secondRow.add(new Button( owner.modeController.getMultiSymbol(), new TimesAction((InlineInputLine)line)).withColor(BaseApp.getApp().darkLightColor));
        char[] divisionUnicode = {'\u00F7'};
        secondRow.add(new Button( new String(divisionUnicode), new DivAction((InlineInputLine)line)).withColor(BaseApp.getApp().darkLightColor));


        ArrayList<Button> thridRow = new ArrayList<Button>();
        thridRow.add(new Button( "1", new NumberAction((InlineInputLine)line, "1")));
        thridRow.add(new Button( "2", new NumberAction((InlineInputLine)line, "2")));
        thridRow.add(new Button( "3", new NumberAction((InlineInputLine)line, "3")));
        thridRow.add(new Button( "0", new NumberAction((InlineInputLine)line, "0")));
        thridRow.add(new Button( ".", new DecimalAction((InlineInputLine)line, ".")));
        thridRow.add(new Button( "^", new PowerAction((InlineInputLine)line)).withColor(BaseApp.getApp().darkLightColor));
        char[] sqrtUnicode = {'\u221A'};
        thridRow.add(new Button( new String(sqrtUnicode), new SqrtAction((InlineInputLine)line)).withColor(BaseApp.getApp().darkLightColor));




        addButtonsRow(firstRow, 0f, 7f / 9f,6f / 9f, 7f / 9f);
        char[] backSpaceUnicode = {'\u232B'};
        Button del = new Button( new String(backSpaceUnicode), new DeleteAction((InlineInputLine)line)).withColor(BaseApp.getApp().darkDarkColor).withTextColor(Color.WHITE);
        Typeface myTypeface = Typeface.createFromAsset(BaseApp.getApp().getAssets(), "fonts/DejaVuSans.ttf");
        del.textPaint.setTypeface(myTypeface);
        del.setLocation(7f / 9f, 1f, 6f / 9f, 7f / 9f);
        buttons.add(del);

        addButtonsRow(secondRow, 0f, 7f / 9f, 7f / 9f, 8f / 9f);
        Button cancel = new Button(BaseApp.getApp().getResources().getString(R.string.cancel),  getCancel((InlineInputLine) line)).withColor(BaseApp.getApp().darkDarkColor).withTextColor(Color.WHITE);
        cancel.setLocation(7f / 9f, 1f, 7f / 9f, 8f / 9f);
        buttons.add(cancel);

        addButtonsRow(thridRow, 0f, 7f / 9f,8f / 9f, 9f / 9f);
        Button solve =new Button( BaseApp.getApp().getResources().getString(R.string.ok), getOk((InlineInputLine) line)).withColor(BaseApp.getApp().darkDarkColor).withTextColor(Color.WHITE);
        solve.setLocation(7f / 9f, 1f, 8f / 9f, 9f / 9f);
        buttons.add(solve);
    }

    private SuperAction getCancel(InlineInputLine line) {

        return owner.modeController.getCancel(line);

    }

    private SuperAction getOk(InlineInputLine line) {
        return owner.modeController.getOk(line);

    }

    @Override
    public float getBaseButtonsPercent() {
        return 1/3f;
    }
}
