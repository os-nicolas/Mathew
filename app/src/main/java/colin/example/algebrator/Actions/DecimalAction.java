package colin.example.algebrator.Actions;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.NumConstEquation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.EmilyView;

public class DecimalAction extends Action {

    public String dec;

    public DecimalAction(EmilyView emilyView, String dec) {
        super(emilyView);
        this.dec = dec;
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation)emilyView.selected).goDark();
            Equation l = emilyView.left();
            if ((l != null) && (l instanceof NumConstEquation && l.getDisplay(-1).contains(".") == false)) {
                if (!(l.parent instanceof BinaryEquation && l.parent.indexOf(l) == 0)) {
                    String toSet = ((NumConstEquation)l).getDisplaySimple() + dec;
                    l.setDisplay(toSet);
                } else {
                    Equation oldEq = emilyView.selected;
                    Equation holder = new WritingEquation(emilyView);
                    Equation newEq = getEq();
                    oldEq.replace(holder);
                    holder.add(newEq);
                    holder.add(oldEq);
                    oldEq.setSelected(true);
                }
            } else {
                if (l == null || !(l.parent instanceof BinaryEquation && l.parent.indexOf(l) == 0)) {
                    emilyView.insert(getEq());
                }else{
                    Equation oldEq = emilyView.selected;
                    Equation holder = new WritingEquation(emilyView);
                    Equation newEq = getEq();
                    oldEq.replace(holder);
                    holder.add(newEq);
                    holder.add(oldEq);
                    oldEq.setSelected(true);
                }
            }
        } else {
            //if (emilyView.selected != null) {
            //    addToBlock(getEq());
            //}

        }
    }

    private Equation getEq() {
        NumConstEquation numEq = new NumConstEquation(0, emilyView);
        numEq.setDisplay(numEq.getDisplay(0) + ".");
        return numEq;
    }
}

