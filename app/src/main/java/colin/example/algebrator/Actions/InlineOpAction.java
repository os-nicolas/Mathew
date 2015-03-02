package colin.example.algebrator.Actions;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.algebrator.eq.WritingPraEquation;
import colin.example.algebrator.EmilyView;

/**
 * Created by Colin on 2/22/2015.
 */
public abstract class InlineOpAction extends Action {
    public InlineOpAction(EmilyView emilyView) {
        super(emilyView);
    }

    protected void inlineInsert(Equation newEq) {
        if (emilyView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation)emilyView.selected).goDark();

            Equation l = emilyView.left();
            boolean can = true;
            if (l instanceof WritingLeafEquation) {
                can = !((WritingLeafEquation) l).isOpLeft();
            }
            if (l instanceof WritingPraEquation && ((WritingPraEquation) l).left){
                can = false;
            }

            if (can) {

                emilyView.insert(newEq);
            }
        }
    }
}
