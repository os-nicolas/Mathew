package cube.d.n.commoncore.Action.SovleScreen;

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
        InputLine line = new InputLine(owner.owner, InputLine.App.CALC);
        owner.owner.addLine(line);
        line.updateOffset();
    }
}
