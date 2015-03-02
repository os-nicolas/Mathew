package colin.example.algebrator.Actions;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.MonaryEquation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.EmilyView;

public class MinusAction extends MonaryAction {

    public MinusAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {

        Equation newEq = new WritingLeafEquation("-", emilyView);
        ((PlaceholderEquation)emilyView.selected).goDark();
        monaryInsert(newEq);

    }

}

