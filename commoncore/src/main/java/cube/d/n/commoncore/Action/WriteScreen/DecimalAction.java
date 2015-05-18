package cube.d.n.commoncore.Action.WriteScreen;


import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.InputLine;

public class DecimalAction extends Action {

    public String dec;

    public DecimalAction(InputLine owner, String dec) {
        super(owner);
        this.dec = dec;
    }

    @Override
    public boolean canAct() {
        return true;
    }

    @Override
    protected void privateAct() {
        ((PlaceholderEquation) ((InputLine)owner).getSelected()).goDark();
        Equation l = ((InputLine)owner).left();
        if ((l != null) && (l instanceof NumConstEquation && l.getDisplay(-1).contains(".") == false)) {
            if (!(l.parent instanceof BinaryEquation && l.parent.indexOf(l) == 0)) {

                String toSet = ((NumConstEquation) l).getDisplaySimple() + dec;
                l.setDisplay(toSet);
            } else {
                Equation oldEq = ((InputLine)owner).getSelected();
                Equation holder = new WritingEquation(((InputLine)owner));
                Equation newEq = getEq();
                oldEq.replace(holder);
                holder.add(newEq);
                holder.add(oldEq);
                oldEq.setSelected(true);
            }
        } else {
            if (l == null || !(l.parent instanceof BinaryEquation && l.parent.indexOf(l) == 0)) {
                ((InputLine)owner).insert(getEq());
            } else {
                Equation oldEq = ((InputLine)owner).getSelected();
                Equation holder = new WritingEquation(((InputLine)owner));
                Equation newEq = getEq();
                oldEq.replace(holder);
                holder.add(newEq);
                holder.add(oldEq);
                oldEq.setSelected(true);
            }
        }
        updateOffset();
    }

    private Equation getEq() {
        NumConstEquation numEq = new NumConstEquation(0, owner);
        numEq.setDisplay(numEq.getDisplay(0) + ".");
        return numEq;
    }
}

