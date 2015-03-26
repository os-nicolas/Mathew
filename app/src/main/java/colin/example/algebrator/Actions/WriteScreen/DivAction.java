package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.DivEquation;
import colin.algebrator.eq.Equation;
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
