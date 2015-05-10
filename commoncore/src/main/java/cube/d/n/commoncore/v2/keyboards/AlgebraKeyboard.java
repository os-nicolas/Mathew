package cube.d.n.commoncore.v2.keyboards;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.SovleScreen.BothSides;
import cube.d.n.commoncore.Action.SovleScreen.SqrtBothSides;
import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.v2.Main;
import cube.d.n.commoncore.v2.keyboards.KeyBoard;
import cube.d.n.commoncore.v2.lines.AlgebraLine;

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
        firstRow.add(new Button( "+", new BothSides((AlgebraLine)line)));
        firstRow.add(new Button( "-", new BothSides((AlgebraLine)line)));
        char[] timesUnicode = {'\u00D7'};
        firstRow.add(new Button( new String(timesUnicode), new BothSides((AlgebraLine)line)));
        char[] divisionUnicode = {'\u00F7'};
        firstRow.add(new Button( new String(divisionUnicode), new BothSides((AlgebraLine)line)));
        firstRow.add(new Button( "cⁿ", new BothSides((AlgebraLine)line)));
        char[] sqrtUnicode = {'\u221A'};
        firstRow.add(new Button( new String(sqrtUnicode), new SqrtBothSides((AlgebraLine)line)));
        addButtonsRow(firstRow, 8f / 9f, 9f / 9f);

    }

    @Override
    public float getBaseButtonsPercent() {
        return 1/9f;
    }
}
