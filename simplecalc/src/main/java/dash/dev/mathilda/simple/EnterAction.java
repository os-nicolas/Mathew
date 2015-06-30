package dash.dev.mathilda.simple;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Selects;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.OutputLine;

/**
 * Created by Colin_000 on 5/22/2015.
 */
public class EnterAction extends Action {

    public EnterAction(InputLine line) {
        super(line);
    }

    public static Equation mine;

    @Override
    public boolean canAct() {
        // we need to copy stupid
        mine = owner.stupid.get().copy();

        // we want to remove the place holder
        Equation at = owner.stupid.get();
        Equation myAt = mine;
        while (!at.equals(((Selects)owner).getSelected())) {
            int index = at.deepIndexOf(((Selects)owner).getSelected());
            at = at.get(index);
            myAt = myAt.get(index);
        }
        if (myAt.parent != null && myAt.parent.size() != 1) {
            myAt.remove();
        } else {
            return false;
        }

        // we need to follow the path to selected
        // and remove it from mine
        if (mine instanceof WritingEquation) {

            if (((WritingEquation) mine).deepLegal() ) {//&& countEquals(mine) == 1
                return true;
            }
        }
        return false;
    }

    @Override
    protected void privateAct() {

        Equation newEq = ((WritingEquation) EnterAction.mine).convert();
        ((InputLine)owner).deActivate();
        EquationLine line = new OutputLine(owner.owner,newEq);
        newEq.updateOwner(line);
        owner.owner.addLine(line);
    }
}
