package colin.example.algebrator.Actions;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.NumConstEquation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.EmilyView;

public class NumberAction extends Action {

    public String num;

    public NumberAction(EmilyView emilyView, String num) {
        super(emilyView);
        this.num = num;
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation)emilyView.selected).goDark();

            Equation l = emilyView.left();
            Equation r = emilyView.selected.right();
            if (l != null) {
                if (!(l.parent instanceof BinaryEquation)) {
                    if ((l instanceof NumConstEquation) && (l.parent.equals(emilyView.selected.parent))) {
                        if (l instanceof NumConstEquation && !l.getDisplay(-1).equals("0")) {
                            l.setDisplay(((NumConstEquation) l).getDisplaySimple() + num);
                        } else if (l instanceof NumConstEquation && l.getDisplay(-1).equals("0")) {
                            l.setDisplay(num);
                        }
                    } else if (emilyView.selected.parent instanceof BinaryEquation) {
                        Equation oldEq = emilyView.selected;
                        Equation holder = new WritingEquation(emilyView);
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), emilyView);
                        oldEq.replace(holder);
                        holder.add(newEq);
                        holder.add(oldEq);
                        oldEq.setSelected(true);
                    } else {
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), emilyView);
                        emilyView.insert(newEq);
                    }
                } else {
                    if ((r instanceof NumConstEquation) && (emilyView.selected.parent.equals(r.parent)) && !(emilyView.selected.parent instanceof BinaryEquation)) {
                        if (!r.getDisplay(-1).equals("0")) {
                            r.setDisplay(num + ((NumConstEquation) r).getDisplaySimple());
                        } else {
                            r.setDisplay(num);
                        }
                    } else if (emilyView.selected.parent instanceof BinaryEquation) {
                        Equation oldEq = emilyView.selected;
                        Equation holder = new WritingEquation(emilyView);
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), emilyView);
                        oldEq.replace(holder);
                        holder.add(newEq);
                        holder.add(oldEq);
                        oldEq.setSelected(true);
                    } else {
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), emilyView);
                        emilyView.insert(newEq);
                    }
                }
            } else {
                if (emilyView.selected.parent instanceof BinaryEquation) {
                    Equation oldEq = emilyView.selected;
                    Equation holder = new WritingEquation(emilyView);
                    Equation newEq = new NumConstEquation(Integer.parseInt(num), emilyView);
                    oldEq.replace(holder);
                    holder.add(newEq);
                    holder.add(oldEq);
                    oldEq.setSelected(true);
                } else if (r instanceof NumConstEquation) {
                    if (!r.getDisplay(-1).equals("0")) {
                        r.setDisplay(num + ((NumConstEquation) r).getDisplaySimple());
                    } else {
                        r.setDisplay(num);
                    }
                } else {
                    Equation newEq = new NumConstEquation(Integer.parseInt(num), emilyView);
                    emilyView.insert(newEq);
                }
            }
        }
    }

}
