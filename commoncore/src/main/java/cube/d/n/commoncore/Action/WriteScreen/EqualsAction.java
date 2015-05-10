package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import cube.d.n.commoncore.v2.lines.InputLine;

/**
 * Created by Colin on 1/7/2015.
 */
public class EqualsAction extends Action {
    public EqualsAction(InputLine owner) {
        super(owner);
    }

    @Override
    public boolean canAct() {

            Equation l = ((InputLine)owner).left();
            // we can't add it if there is nothing to the left
            boolean can = l != null;
            // we can't add if the last char was an op
            if (l instanceof WritingLeafEquation) {
                can = can && !((WritingLeafEquation) l).isOpLeft();
            }
            // if the root equation only hold one
            can = can && countEquals(((InputLine)owner).stupid.get()) == 0;

            // we can't add if we have (|
            can = can && !(l instanceof WritingPraEquation && ((WritingPraEquation) l).left);
            // we can't add if we are not adding to the rootWriteEquation
            if (can) {
                can = l.parent.parent == null;
                // but we can move out
                if (((InputLine)owner).getSelected().right() == null) {
                    can = true;
                }
            }
            return can;


    }

    @Override
    protected void privateAct() {
        ((InputLine)owner).getSelected().goDark();
        if (((InputLine)owner).getSelected().right() == null) {
            while (canMoveRight(((InputLine)owner).getSelected())) {
                tryMoveRight(((InputLine)owner).getSelected());
            }
        }

        Equation newEq = new WritingLeafEquation("=",(InputLine)owner);
        ((InputLine)owner).insert(newEq);
    }
}


