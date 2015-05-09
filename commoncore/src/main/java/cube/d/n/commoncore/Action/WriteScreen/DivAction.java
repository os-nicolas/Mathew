package cube.d.n.commoncore.Action.WriteScreen;


import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.v2.InputLine;

public class DivAction extends BinaryAction {

    public DivAction(InputLine owner) {
        super(owner);
    }

    @Override
    protected void privateAct() {
        Equation newEq = new DivEquation(owner);

        super.privateAct(newEq);
    }

}
