package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.example.algebrator.EmilyView;

public class PlusAction extends InlineOpAction {


    public PlusAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    protected void privateAct() {
        Equation newEq = new WritingLeafEquation("+", myView);

        inlineInsert(newEq);
    }



}
