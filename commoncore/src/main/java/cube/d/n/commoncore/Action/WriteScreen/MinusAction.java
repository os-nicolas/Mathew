package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.lines.InputLine;

public class MinusAction extends MonaryAction {

    public MinusAction(InputLine emilyView) {
        super(emilyView);
    }

    @Override
    protected void privateAct() {

        Equation newEq = new WritingLeafEquation("-", (InputLine)owner);
        ((InputLine)owner).getSelected().goDark();
        monaryInsert(newEq);

    }

}

