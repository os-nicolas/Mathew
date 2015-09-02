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
import cube.d.n.commoncore.lines.AlgebraLine;

/**
 * Created by Colin_000 on 7/7/2015.
 */
public class AlgebraKeyboardNoReturn extends AlgebraKeyboard {

    public AlgebraKeyboardNoReturn(Main owner, AlgebraLine algebraLine) {
        super(owner,algebraLine);
    }

    @Override
    protected void addButtons() {

//        popUpButtons.add(new PopUpEquationDisplay((AlgebraLine)line,new AddSelectedToBothSIdes(((AlgebraLine)line))));
//        popUpButtons.add(new PopUpEquationDisplay((AlgebraLine)line,new MultiBySelected(((AlgebraLine)line))));
//        popUpButtons.add(new PopUpEquationDisplay((AlgebraLine)line,new DivBySelected(((AlgebraLine)line))));

//        ArrayList<Button> firstRow = new ArrayList<Button>();
//        firstRow.add(new Button( "+", new BothSides((AlgebraLine)line, BothSidesMode.ADD)));
//        firstRow.add(new Button( "-", new BothSides((AlgebraLine)line,BothSidesMode.SUB)));
//        firstRow.add(new Button( BaseApp.getApp().getMultiSymbol(), new BothSides((AlgebraLine)line,BothSidesMode.MULTI)));
//        char[] divisionUnicode = {'\u00F7'};
//        firstRow.add(new Button( new String(divisionUnicode), new BothSides((AlgebraLine)line,BothSidesMode.DIV)));
//        firstRow.add(new Button( "cⁿ", new BothSides((AlgebraLine)line,BothSidesMode.POWER)));
//        char[] sqrtUnicode = {'\u221A'};
//        firstRow.add(new Button( new String(sqrtUnicode), new SqrtBothSides((AlgebraLine)line)));
//
//        addButtonsRow(firstRow, 0f, 1f,8f / 9f, 9f / 9f);

    }

    @Override
    public float getBaseButtonsPercent() {
        return 0f;
    }
}
