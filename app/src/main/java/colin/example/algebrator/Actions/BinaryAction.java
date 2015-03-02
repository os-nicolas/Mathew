package colin.example.algebrator.Actions;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.NumConstEquation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.PowerEquation;
import colin.algebrator.eq.VarEquation;
import colin.algebrator.eq.WritingEquation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.algebrator.eq.WritingPraEquation;
import colin.example.algebrator.EmilyView;

/**
 * Created by Colin on 1/13/2015.
 */
public abstract class BinaryAction extends Action {

    public BinaryAction(EmilyView emilyView) {
        super(emilyView);
    }

    protected void act(Equation newEq) {
        if (emilyView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation) emilyView.selected).goDark();
            Equation l = emilyView.left();
            boolean can = l != null;
            if (can && (l instanceof WritingLeafEquation || l instanceof VarEquation || l instanceof NumConstEquation)) {
                can = !l.isOpLeft();
            }

            if (can && l instanceof WritingPraEquation) {
                if (((WritingPraEquation) l).left) {
                    can = false;
                } else {
                    // we need to select the hole ( .. )
                    can = false;
                    if (((WritingPraEquation) l).getMatch() != null) {
                        ((WritingPraEquation) l).selectBlock();
                    }
                }
            }
            if (can) {
                emilyView.selected.justRemove();
                Equation oldEq = l;

                oldEq.replace(newEq);
                newEq.add(oldEq);
                newEq.add(emilyView.selected);
            }
        }

        if (!(emilyView.selected instanceof PlaceholderEquation)) {
            if (emilyView.selected != null) {
                boolean can = countEquals(emilyView.selected) == 0;
                if (emilyView.selected instanceof WritingEquation) {
                    Equation eq = emilyView.selected.get(0);
                    if (eq instanceof WritingLeafEquation || eq instanceof NumConstEquation || eq instanceof VarEquation) {
                        can = can && !eq.isOpRight();
                    }
                }
                if (emilyView.selected instanceof WritingEquation) {
                    Equation eq = emilyView.selected.get(emilyView.selected.size() - 1);
                    if (eq instanceof WritingLeafEquation || eq instanceof NumConstEquation || eq instanceof VarEquation) {
                        can = can && !eq.isOpLeft();
                    }
                }

                if (can) {

                    Equation oldEq = emilyView.selected;
                    if (newEq instanceof PowerEquation) {
                        oldEq.remove(0);
                        Equation swap = oldEq;
                        if (oldEq.size() == 2) {
                            swap = oldEq.get(0);
                        }
                        oldEq.remove(oldEq.size() - 1);
                        oldEq = swap;
                    }

                    if (oldEq.parent == null) {
                        Equation writeEq = new WritingEquation(emilyView);
                        writeEq.add(newEq);
                        oldEq.replace(writeEq);
                    } else {
                        oldEq.replace(newEq);
                    }
                    newEq.add(oldEq);
                    Equation placeHolder = new PlaceholderEquation(emilyView);
                    newEq.add(placeHolder);
                    placeHolder.setSelected(true);
                }
            }
        }
    }
}
