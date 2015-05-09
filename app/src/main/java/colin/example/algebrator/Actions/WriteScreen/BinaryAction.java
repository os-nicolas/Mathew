package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import cube.d.n.commoncore.eq.write.WritingSqrtEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.EmilyView;

/**
 * Created by Colin on 1/13/2015.
 */
public abstract class BinaryAction extends Action<EmilyView> {

    public BinaryAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public boolean canAct() {
        if (myView.selected instanceof PlaceholderEquation) {
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
                    can = (((WritingPraEquation) l).getMatch() != null) ;
                }
            }
            return can;
        }
        return false;
    }

    protected void privateAct(Equation newEq) {
        ((PlaceholderEquation) myView.selected).goDark();
        Equation l = myView.left();
        boolean can = true;

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

        // I am not using an else here for good reason
        if (!(myView.selected instanceof PlaceholderEquation)) {
            if (myView.selected != null) {
                can = countEquals(myView.selected) == 0;
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
