package cube.d.n.commoncore.keyboards;

import android.graphics.Color;

import java.util.ArrayList;


import cube.d.n.commoncore.Action.SovleScreen.AddSelectedToBothSIdes;
import cube.d.n.commoncore.Action.SovleScreen.BothSides;
import cube.d.n.commoncore.Action.SovleScreen.BothSidesMode;
import cube.d.n.commoncore.Action.SovleScreen.DivBySelected;
import cube.d.n.commoncore.Action.SovleScreen.MultiBySelected;
import cube.d.n.commoncore.Action.SovleScreen.SqrtBothSides;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.PopUpButton;
import cube.d.n.commoncore.PopUpEquationDisplay;
import cube.d.n.commoncore.R;
import cube.d.n.commoncore.SelectedRow;
import cube.d.n.commoncore.SelectedRowButtons;
import cube.d.n.commoncore.SeletedRowEquationButton;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.AlgebraLine;

/**
 * Created by Colin_000 on 5/9/2015.
 */
public class AlgebraKeyboard extends KeyBoard {
    public AlgebraKeyboard(Main owner, AlgebraLine algebraLine) {
        super(owner,algebraLine);
    }

    @Override
    protected void addButtons() {



        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button( "+", new BothSides((AlgebraLine)line, BothSidesMode.ADD)));
        firstRow.add(new Button( "-", new BothSides((AlgebraLine)line,BothSidesMode.SUB)));
        char[] timesUnicode = {'\u00D7'};
        firstRow.add(new Button( new String(timesUnicode), new BothSides((AlgebraLine)line,BothSidesMode.MULTI)));
        char[] divisionUnicode = {'\u00F7'};
        firstRow.add(new Button( new String(divisionUnicode), new BothSides((AlgebraLine)line,BothSidesMode.DIV)));
        firstRow.add(new Button( "cⁿ", new BothSides((AlgebraLine)line,BothSidesMode.POWER)));
        char[] sqrtUnicode = {'\u221A'};
        firstRow.add(new Button( new String(sqrtUnicode), new SqrtBothSides((AlgebraLine)line)));

        addButtonsRow(firstRow, 0f, 7f / 9f,8f / 9f, 9f / 9f);
        Button solve =new Button( BaseApp.getApp().getResources().getString(R.string.retrn), BaseApp.getApp().getDone(line)).withColor(BaseApp.getApp().darkDarkColor).withTextColor(Color.WHITE);
        solve.setLocation(7f / 9f, 1f, 8f / 9f, 9f / 9f);
        buttons.add(solve);

    }

    @Override
    public float getBaseButtonsPercent() {
        return 1/9f;
    }
}
