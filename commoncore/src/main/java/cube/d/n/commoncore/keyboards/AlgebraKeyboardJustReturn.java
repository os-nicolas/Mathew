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
 * Created by Colin_000 on 9/13/2015.
 */
public class AlgebraKeyboardJustReturn extends KeyBoard {
    public AlgebraKeyboardJustReturn(Main main, AlgebraLine algebraLine) {
        super(main,algebraLine);
    }


    @Override
    protected void addButtons() {
        Button solve =new Button( BaseApp.getApp().getResources().getString(R.string.retrn), owner.modeController.getDone(line)).withColor(BaseApp.getApp().darkDarkColor).withTextColor(Color.WHITE);
        solve.setLocation(0f, 1f, 8f / 9f, 9f / 9f);
        buttons.add(solve);

    }



    @Override
    public float getBaseButtonsPercent() {
        return 1/9f;
    }
}
