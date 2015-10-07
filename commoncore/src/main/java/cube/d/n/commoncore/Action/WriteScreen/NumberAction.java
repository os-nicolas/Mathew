package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.lines.InputLine;

public class NumberAction extends Action {

    public String num;

    public NumberAction(InputLine emilyView, String num) {
        super(emilyView);
        this.num = num;
    }

    @Override
    public boolean canAct() {
        return true;
    }

    @Override
    protected void privateAct() {
        ((InputLine) owner).getSelected().goDark();

        Equation l = ((InputLine) owner).imedateLeft();
        Equation r = ((InputLine) owner).imedateRight();
        if (l != null) {
            if ((l instanceof NumConstEquation) && (l.parent.equals(((InputLine) owner).getSelected().parent))) {
                ((NumConstEquation) l).showAll=true;
                if (l instanceof NumConstEquation && !l.getDisplay(-1).equals("0")) {
                    l.setDisplay(((NumConstEquation) l).getDisplaySimple() + num);
                } else if (l instanceof NumConstEquation && l.getDisplay(-1).equals("0")) {
                    l.setDisplay(num);
                }
            } else {
                Equation newEq = NumConstEquation.create(Integer.parseInt(num), ((InputLine) owner));
                ((InputLine) owner).insert(newEq);
            }
        } else {
            if (r instanceof NumConstEquation) {
                ((NumConstEquation) r).showAll=true;
                if (!r.getDisplay(-1).equals("0")) {
                    r.setDisplay(num + ((NumConstEquation) r).getDisplaySimple());
                } else {
                    r.setDisplay(num);
                }
            } else {
                Equation newEq = NumConstEquation.create(Integer.parseInt(num), ((InputLine) owner));
                ((InputLine) owner).insert(newEq);
            }
        }
        updateOffset();
    }

}
