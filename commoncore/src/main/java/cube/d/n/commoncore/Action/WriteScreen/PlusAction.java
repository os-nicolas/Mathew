package cube.d.n.commoncore.Action.WriteScreen;


import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.v2.InputLine;

public class PlusAction extends InlineOpAction {


    public PlusAction(InputLine emilyView) {
        super(emilyView);
    }

    @Override
    protected void privateAct() {
        Equation newEq = new WritingLeafEquation("+", ((InputLine)owner));

        inlineInsert(newEq);
    }
}
