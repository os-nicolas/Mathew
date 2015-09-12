package cube.d.n.commoncore.keyboards;

import android.graphics.Color;
import android.graphics.Typeface;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.TrigAction.AcosAction;
import cube.d.n.commoncore.Action.TrigAction.AsinAction;
import cube.d.n.commoncore.Action.TrigAction.AtanAction;
import cube.d.n.commoncore.Action.TrigAction.CosAction;
import cube.d.n.commoncore.Action.TrigAction.SineAction;
import cube.d.n.commoncore.Action.TrigAction.TanAction;
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
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 8/25/2015.
 */
public abstract class TrigInputKeyboard extends InputKeyboard {
    public TrigInputKeyboard(Main owner, InputLine line) {
        super(owner, line);
    }

    @Override
    protected void addButtons() {
        super.addButtons();


        ArrayList<Button> trigRow = new ArrayList<Button>();
        trigRow.add(new Button("Sin", new SineAction((InputLine)line)));
        trigRow.add(new Button("ASin", new AsinAction((InputLine)line)));
        trigRow.add(new Button("Cos", new CosAction((InputLine)line)));
        trigRow.add(new Button("Acos", new AcosAction((InputLine)line)));
        trigRow.add(new Button("Tan", new TanAction((InputLine)line)));
        trigRow.add(new Button("Atan", new AtanAction((InputLine)line)));

        addButtonsRow(trigRow, 5f / 9f, 6f / 9f);

    }

    @Override
    public float getBaseButtonsPercent() {
        return 4/9f;
    }
}
