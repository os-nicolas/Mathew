package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingSqrtEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin on 1/29/2015.
 */
public class SqrtAction extends MonaryAction {
    public SqrtAction(InputLine emilyView) {
        super(emilyView);
    }

    @Override
    protected void privateAct() {
        Equation newEq = new WritingSqrtEquation(((InputLine)owner));
        monaryInsert(newEq);

    }


}
