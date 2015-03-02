package colin.example.algebrator.Actions;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.example.algebrator.EmilyView;

public class PlusAction extends InlineOpAction {


    public PlusAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        Equation newEq = new WritingLeafEquation("+", emilyView);

        inlineInsert(newEq);
    }



}
