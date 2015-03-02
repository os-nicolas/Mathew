package colin.example.algebrator.Actions;

import colin.algebrator.eq.DivEquation;
import colin.algebrator.eq.Equation;
import colin.example.algebrator.EmilyView;

public class DivAction extends BinaryAction {

    public DivAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        Equation newEq = new DivEquation(emilyView);

        act(newEq);
    }

}
