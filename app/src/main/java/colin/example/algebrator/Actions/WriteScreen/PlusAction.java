package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
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
