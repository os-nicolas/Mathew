package cube.d.n.commoncore.Action.WriteScreen;


import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import cube.d.n.commoncore.eq.write.WritingSqrtEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin on 1/13/2015.
 */
public abstract class BinaryAction extends Action {

    public BinaryAction(InputLine emilyView) {
        super(emilyView);
    }

    @Override
    public boolean canAct() {
        Equation l = ((InputLine) owner).left();
        boolean can = l != null;
        if (can && (l instanceof WritingLeafEquation || l instanceof VarEquation || l instanceof NumConstEquation)) {
            can = !l.isOpLeft();
        }
        if (can && l instanceof WritingPraEquation) {
            if (((WritingPraEquation) l).left) {
                can = false;
            } else {
                // we need to select the hole ( .. )
                can = canBlock();
            }
        }
        return can;
    }

    protected void privateAct(Equation newEq) {
        Equation l = ((InputLine) owner).left();
        boolean can = canAct();
        boolean block = can && canBlock();

        if (can && !block) {
            ((InputLine) owner).getSelected().justRemove();
            Equation oldEq = l;

            oldEq.replace(newEq);
            newEq.add(oldEq);
            newEq.add(((InputLine) owner).getSelected());
        }

        if (can && block) {
            Equation oldEq = ((WritingPraEquation) l).popBlock();
            if (newEq instanceof PowerEquation && !(oldEq.get(0) instanceof WritingSqrtEquation)) {
                oldEq.remove(0);
                Equation swap = oldEq;
                if (oldEq.size() == 2) {
                    swap = oldEq.get(0);
                }
                oldEq.remove(oldEq.size() - 1);
                oldEq = swap;
            }

            if (oldEq.parent == null) {
                Equation writeEq = new WritingEquation(owner);
                writeEq.add(newEq);
                oldEq.replace(writeEq);
            } else {
                oldEq.replace(newEq);
            }
            newEq.add(oldEq);
            Equation placeHolder = ((InputLine) owner).getSelected();
            ((InputLine) owner).getSelected().justRemove();
            newEq.add(placeHolder);
        }
        updateOffsetX();
    }

    private boolean canBlock() {
        Equation l = ((InputLine) owner).left();
        if (l instanceof WritingPraEquation) {
            if (!((WritingPraEquation) l).left) {

                boolean can = (((WritingPraEquation) l).getMatch() != null);

                return can;
            }
        }
        return false;
    }
}
