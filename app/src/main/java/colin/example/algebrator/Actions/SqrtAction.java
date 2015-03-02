package colin.example.algebrator.Actions;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.algebrator.eq.WritingSqrtEquation;
import colin.example.algebrator.EmilyView;

/**
 * Created by Colin on 1/29/2015.
 */
public class SqrtAction extends MonaryAction {
    public SqrtAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        Equation newEq = new WritingSqrtEquation(emilyView);
        monaryInsert(newEq);
    }


}
