package cube.d.n.commoncore.Action.WriteScreen;


import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.v2.InputLine;

/**
 * Created by Colin on 1/13/2015.
 */
public class PowerAction extends BinaryAction {
    public PowerAction(InputLine emilyView) {
        super(emilyView);
    }

    @Override
    protected void privateAct() {
        Equation newEq = new PowerEquation(((InputLine)owner));

        privateAct(newEq);
    }
}
