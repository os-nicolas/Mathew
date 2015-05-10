package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import cube.d.n.commoncore.v2.lines.InputLine;

/**
 * Created by Colin on 2/22/2015.
 */
public abstract class InlineOpAction extends Action {
    public InlineOpAction(InputLine own) {
        super(own);
    }

    @Override
    public boolean canAct() {
        Equation l = ((InputLine) owner).left();
        boolean can = true;
        if (l instanceof WritingLeafEquation) {
            can = !((WritingLeafEquation) l).isOpLeft();
        }
        if (l instanceof WritingPraEquation && ((WritingPraEquation) l).left) {
            can = false;
        }

        if (l == null) {
            can = false;
        }

        return can;
    }

    protected void inlineInsert(Equation newEq) {
        ((InputLine) owner).getSelected().goDark();
        ((InputLine) owner).insert(newEq);
    }
}
