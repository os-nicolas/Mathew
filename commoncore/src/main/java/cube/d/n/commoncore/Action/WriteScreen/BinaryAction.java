package cube.d.n.commoncore.Action.WriteScreen;


import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import cube.d.n.commoncore.v2.lines.InputLine;

/**
 * Created by Colin on 1/13/2015.
 */
public abstract class BinaryAction extends Action {

    public BinaryAction(InputLine emilyView) {
        super(emilyView);
    }

    @Override
    public boolean canAct() {
            Equation l = ((InputLine)owner).left();
            boolean can = l != null;
            if (can && (l instanceof WritingLeafEquation || l instanceof VarEquation || l instanceof NumConstEquation)) {
                can = !l.isOpLeft();
            }
            if (can && l instanceof WritingPraEquation) {
                if (((WritingPraEquation) l).left) {
                    can = false;
                } else {
                    // we need to select the hole ( .. )
                    can = (((WritingPraEquation) l).getMatch() != null) ;
                }
            }
            return can;
    }

    protected void privateAct(Equation newEq) {
        ((InputLine)owner).getSelected().goDark();
        Equation l = ((InputLine)owner).left();
        boolean can = true;

        if (can && l instanceof WritingPraEquation) {
            if (((WritingPraEquation) l).left) {
                can = false;
            } else {
                // we need to select the hole ( .. )
                can = false;
            }
        }
        if (can) {
            ((InputLine)owner).getSelected().justRemove();
            Equation oldEq = l;

            oldEq.replace(newEq);
            newEq.add(oldEq);
            newEq.add(((InputLine)owner).getSelected());
        }
    }
}
