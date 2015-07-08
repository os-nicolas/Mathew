package cube.d.n.commoncore.keyboards;

import android.graphics.Color;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.SovleScreen.BothSides;
import cube.d.n.commoncore.Action.SovleScreen.BothSidesMode;
import cube.d.n.commoncore.Action.SovleScreen.SqrtBothSides;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.R;
import cube.d.n.commoncore.lines.AlgebraLine;

/**
 * Created by Colin_000 on 7/7/2015.
 */
public class AlgebraKeyboardNoReturn extends KeyBoard {

    public AlgebraKeyboardNoReturn(Main owner, AlgebraLine algebraLine) {
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

        addButtonsRow(firstRow, 0f, 1f,8f / 9f, 9f / 9f);

    }

    @Override
    public float getBaseButtonsPercent() {
        return 1/9f;
    }
}