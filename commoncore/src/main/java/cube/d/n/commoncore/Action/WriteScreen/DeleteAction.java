package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.InputLine;

public class DeleteAction extends Action {

    public String num;

    public DeleteAction(InputLine owner) {
        super(owner);
    }

    @Override
    public boolean canAct() {

            Equation l = ((InputLine)owner).left();
            if (l != null) {
                return true;
            }
        return false;
    }

    @Override
    protected void privateAct() {
        //TODO

        ((InputLine)owner).getSelected().goDark();
        Equation l = ((InputLine)owner).left();
        if (l.parent instanceof BinaryEquation) {
            l.parent.replace(l);
            // didn't we just check this? confusing i know but since the replace our l.parent has changed
            if (!(l.parent instanceof BinaryEquation)) {
                int pos = l.parent.indexOf(l);
                l.parent.add(pos + 1, ((InputLine)owner).getSelected());
            } else {
                Equation write = new WritingEquation(((InputLine)owner));
                l.replace(write);
                write.add(l);
                write.add(((InputLine)owner).getSelected());
            }
        } else if (l instanceof NumConstEquation) {
            if (((NumConstEquation) l).getDisplaySimple().length() != 0) {
                String display = ((NumConstEquation) l).getDisplaySimple();
                String toSet = (String) display.subSequence(0, display.length() - 1);
                if (toSet.length() != 0 && toSet.charAt(0) == '-') {
                    toSet = toSet.substring(1, toSet.length());
                }
                ((NumConstEquation) l).setDisplay(toSet);
            }
            if (((NumConstEquation) l).getDisplaySimple().length() == 0) {
                l.remove();
            }
        } else {
            l.remove();
        }
        updateOffset();
    }

}
