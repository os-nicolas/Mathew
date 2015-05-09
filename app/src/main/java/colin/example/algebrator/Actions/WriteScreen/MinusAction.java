package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
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

