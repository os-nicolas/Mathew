package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.NumConstEquation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.PowerEquation;
import colin.algebrator.eq.VarEquation;
import colin.algebrator.eq.WritingEquation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.algebrator.eq.WritingPraEquation;
import colin.algebrator.eq.WritingSqrtEquation;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.Actions.Action;

/**
 * Created by Colin on 1/13/2015.
 */
public abstract class BinaryAction extends Action<EmilyView> {

    public BinaryAction(EmilyView emilyView) {
        super(emilyView);
    }

    protected void privateAct(Equation newEq) {
        if (myView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation) myView.selected).goDark();
            Equation l = myView.left();
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
                myView.selected.justRemove();
                Equation oldEq = l;

                oldEq.replace(newEq);
                newEq.add(oldEq);
                newEq.add(myView.selected);
            }
        }

        if (!(myView.selected instanceof PlaceholderEquation)) {
            if (myView.selected != null) {
                boolean can = countEquals(myView.selected) == 0;
                if (myView.selected instanceof WritingEquation) {
                    Equation eq = myView.selected.get(0);
                    if (eq instanceof WritingLeafEquation || eq instanceof NumConstEquation || eq instanceof VarEquation) {
                        can = can && !eq.isOpRight();
                    }
                }
                if (myView.selected instanceof WritingEquation) {
                    Equation eq = myView.selected.get(myView.selected.size() - 1);
                    if (eq instanceof WritingLeafEquation || eq instanceof NumConstEquation || eq instanceof VarEquation) {
                        can = can && !eq.isOpLeft();
                    }
                }

                if (can) {

                    Equation oldEq = myView.selected;
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
                        Equation writeEq = new WritingEquation(myView);
                        writeEq.add(newEq);
                        oldEq.replace(writeEq);
                    } else {
                        oldEq.replace(newEq);
                    }
                    newEq.add(oldEq);
                    Equation placeHolder = new PlaceholderEquation(myView);
                    newEq.add(placeHolder);
                    placeHolder.setSelected(true);
                }
            }
        }
    }
}
