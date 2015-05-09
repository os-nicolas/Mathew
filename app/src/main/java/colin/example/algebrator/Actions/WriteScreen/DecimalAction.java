package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.EmilyView;

public class DecimalAction extends Action<EmilyView> {

    public String dec;

    public DecimalAction(EmilyView emilyView, String dec) {
        super(emilyView);
        this.dec = dec;
    }

    @Override
    public boolean canAct() {
        return myView.selected instanceof PlaceholderEquation;
    }

    @Override
    protected void privateAct() {
        ((PlaceholderEquation) myView.selected).goDark();
        Equation l = myView.left();
        if ((l != null) && (l instanceof NumConstEquation && l.getDisplay(-1).contains(".") == false)) {
            if (!(l.parent instanceof BinaryEquation && l.parent.indexOf(l) == 0)) {

                String toSet = ((NumConstEquation) l).getDisplaySimple() + dec;
                l.setDisplay(toSet);
            } else {
                Equation oldEq = myView.selected;
                Equation holder = new WritingEquation(myView);
                Equation newEq = getEq();
                oldEq.replace(holder);
                holder.add(newEq);
                holder.add(oldEq);
                oldEq.setSelected(true);
            }
        } else {
            if (l == null || !(l.parent instanceof BinaryEquation && l.parent.indexOf(l) == 0)) {
                myView.insert(getEq());
            } else {
                Equation oldEq = myView.selected;
                Equation holder = new WritingEquation(myView);
                Equation newEq = getEq();
                oldEq.replace(holder);
                holder.add(newEq);
                holder.add(oldEq);
                oldEq.setSelected(true);
            }
        }
    }

    private Equation getEq() {
        NumConstEquation numEq = new NumConstEquation(0, myView);
        numEq.setDisplay(numEq.getDisplay(0) + ".");
        return numEq;
    }
}

