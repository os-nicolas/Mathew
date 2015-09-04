package cube.d.n.commoncore;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Action.ActionWithDisplay;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 8/31/2015.
 */
public class PopUpEquationDisplay extends PopUpEquationButton {
    EquationLine al;
    private Equation lastSelect= null;

    public PopUpEquationDisplay(AlgebraLine owner,ActionWithDisplay myAction) {

        super(new VarEquation("",owner), myAction);
        al = owner;
    }

    public void updateCanAct() {
        super.updateCanAct();
        if (can && !((AlgebraLine)al).getSelected().equals(lastSelect)){
            myEq = ((ActionWithDisplay)myAction).getDisplay();
            lastSelect = ((AlgebraLine)al).getSelected();
        }
    }
}
