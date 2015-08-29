package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin on 8/28/2015.
 */
public class LastAction extends Action {

    Equation last;
    private boolean done= false;

    public LastAction(EquationLine owner) {
        super(owner);
        last=owner.owner.getLast();
    }

    @Override
    public boolean canAct() {

        return last != null && !done;
    }

    @Override
    protected void privateAct() {
        ((InputLine)owner).getSelected().goDark();


        if (last instanceof WritingEquation){
            for (Equation e: last) {
                ((InputLine) owner).insert(e);
            }
        }else {
            ((InputLine) owner).insert(last);
        }

        updateOffset();
         done = true;
    }
}
