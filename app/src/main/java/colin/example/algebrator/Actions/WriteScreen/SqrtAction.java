package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.Equation;
import cube.d.n.commoncore.eq.WritingSqrtEquation;
import colin.example.algebrator.EmilyView;

/**
 * Created by Colin on 1/29/2015.
 */
public class SqrtAction extends MonaryAction {
    public SqrtAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    protected void privateAct() {
        Equation newEq = new WritingSqrtEquation(myView);
        monaryInsert(newEq);
    }


}
