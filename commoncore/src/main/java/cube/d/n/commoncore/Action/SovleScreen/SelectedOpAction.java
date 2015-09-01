package cube.d.n.commoncore.Action.SovleScreen;

import cube.d.n.commoncore.Action.ActionWithDisplay;
import cube.d.n.commoncore.PopUpEquationDisplay;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.SignEquation;
import cube.d.n.commoncore.lines.AlgebraLine;

/**
 * Created by Colin_000 on 8/31/2015.
 */
public abstract class SelectedOpAction extends ActionWithDisplay {

    public SelectedOpAction(AlgebraLine owner) {
        super(owner);
    }

    protected Equation getSel() {
        Equation sel = ((AlgebraLine)owner).getSelected();
        if (sel == null){
            return null;
        }
        while (sel.parent instanceof SignEquation){
            sel = sel.parent;
        }
        return sel;
    }
}
