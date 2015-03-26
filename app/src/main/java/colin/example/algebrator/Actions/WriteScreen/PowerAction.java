package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PowerEquation;
import colin.example.algebrator.EmilyView;

/**
 * Created by Colin on 1/13/2015.
 */
public class PowerAction extends BinaryAction {
    public PowerAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    protected void privateAct() {
        Equation newEq = new PowerEquation(myView);

        privateAct(newEq);
    }
}
