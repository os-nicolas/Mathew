package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.example.algebrator.EmilyView;

public class MinusAction extends MonaryAction {

    public MinusAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    protected void privateAct() {

        Equation newEq = new WritingLeafEquation("-", myView);
        ((PlaceholderEquation) myView.selected).goDark();
        monaryInsert(newEq);

    }

}

