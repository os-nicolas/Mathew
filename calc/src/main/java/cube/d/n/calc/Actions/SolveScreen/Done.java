package cube.d.n.calc.Actions.SolveScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Selects;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.Line;

/**
 * Created by Colin_000 on 5/10/2015.
 */
public class Done extends Action {
    public Done(Line owner) {
        super(owner);
    }

    @Override
    protected void privateAct() {
        ((Selects)owner.owner.lastLine()).setSelected(null);
        InputLine line = new InputLine(owner.owner);
        owner.owner.addLine(line);
    }
}
