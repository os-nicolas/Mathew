package cube.d.n.commoncore.Action.SolveScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Selects;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 5/10/2015.
 */
public class Done extends Action {
    public Done(EquationLine owner) {
        super(owner);
    }

    @Override
    protected void privateAct() {
        ((Selects)owner.owner.lastLine()).setSelected(null);
        InputLine line = (InputLine)BaseApp.getApp().getInputLine(owner.owner);
        owner.owner.addLine(line);
    }
}
