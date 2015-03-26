package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.algebrator.eq.WritingPraEquation;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.Actions.Action;

/**
 * Created by Colin on 1/7/2015.
 */
public class EqualsAction extends Action<EmilyView> {
    public EqualsAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    protected void privateAct() {
        if (myView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation) myView.selected).goDark();

            Equation l = myView.left();
            // we can't add it if there is nothing to the left
            boolean can = l != null;
            // we can't add if the last char was an op
            if (l instanceof WritingLeafEquation) {
                can = can && !((WritingLeafEquation) l).isOpLeft();
            }
            // if the root equation only hold one
            can = can && countEquals(myView.stupid) == 0;

            // we can't add if we have (|
            can = can && !(l instanceof WritingPraEquation && ((WritingPraEquation) l).left);
            // we can't add if we are not adding to the rootWriteEquation
            if (can) {
                can = can && l.parent.parent == null;
                // but we can move out
                if (myView.selected.right() == null) {
                    can = true;
                    while (canMoveRight()) {
                        tryMoveRight();
                    }
                }
            }

            // TODO we should moveright if that would help

            if (can) {
                Equation newEq = new WritingLeafEquation("=", myView);
                myView.insert(newEq);
            }
        }

        //TODO this probably want's an else
    }


}
