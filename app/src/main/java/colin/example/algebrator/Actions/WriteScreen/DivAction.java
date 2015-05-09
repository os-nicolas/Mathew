package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.Equation;
import colin.example.algebrator.EmilyView;

public class DivAction extends BinaryAction {

    public DivAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    protected void privateAct() {
        Equation newEq = new DivEquation(myView);

        super.privateAct(newEq);
    }

}
