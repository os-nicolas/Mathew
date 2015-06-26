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
 * Created by Colin_000 on 6/26/2015.
 */
public class ReturnKeyBoard extends KeyBoard {
    public ReturnKeyBoard(Main owner, AlgebraLine algebraLine) {
        super(owner,algebraLine);
    }

    @Override
    protected void addButtons() {

        ArrayList<Button> firstRow = new ArrayList<Button>();
        firstRow.add(new Button( BaseApp.getApp().getResources().getString(R.string.retrn), BaseApp.getApp().getDone(line)).withColor(BaseApp.getApp().darkDarkColor).withTextColor(Color.WHITE));

        addButtonsRow(firstRow, 0f, 1f,8f / 9f, 9f / 9f);
    }

    @Override
    public float getBaseButtonsPercent() {
        return 1/9f;
    }
}

