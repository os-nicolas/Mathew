package cube.d.n.commoncore.Action.TrigAction;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.Pro.TrigEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 8/25/2015.
 */
public abstract class TrigAction extends Action {

    public TrigAction(InputLine owner) {
        super(owner);
    }

    public abstract TrigEquation getEquation();

    @Override
    public boolean canAct(){
        return true;
    }

    @Override
    protected void privateAct() {
        ((InputLine)owner).getSelected().goDark();

        Equation newEq = getEquation();
        Equation oldEq = ((InputLine)owner).getSelected();

        if (((InputLine)owner).getSelected().parent instanceof WritingEquation) {
            oldEq.replace(newEq);
        } else {
            Equation holder = new WritingEquation(((InputLine)owner));
            oldEq.replace(holder);
            holder.add(newEq);
        }
        newEq.add(oldEq);
        updateOffset();
    }
}
