package cube.d.n.commoncore.Action;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 8/31/2015.
 */
public abstract class ActionWithDisplay extends Action {


    public ActionWithDisplay(EquationLine owner) {
        super(owner);
    }

    public abstract Equation getDisplay();
}
