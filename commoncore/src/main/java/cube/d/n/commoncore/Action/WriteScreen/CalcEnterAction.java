package cube.d.n.commoncore.Action.WriteScreen;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.Selects;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.SimpleCalcLine;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.OutputLine;


public class CalcEnterAction extends Action {

    public CalcEnterAction(InputLine emilyView) {
        super(emilyView);
    }

    public static Equation mine;

    @Override
    public boolean canAct() {
        // we need to copy stupid
        mine = owner.stupid.get().copy();

        // we want to remove the place holder
            Equation at = owner.stupid.get();
            Equation myAt = mine;
            while (!at.equals(((Selects)owner).getSelected())) {
                int index = at.deepIndexOf(((Selects)owner).getSelected());
                at = at.get(index);
                myAt = myAt.get(index);
            }
            if (myAt.parent != null && myAt.parent.size() != 1) {
                myAt.remove();
            } else {
                return false;
            }

        if (failsAdditionalConditions(mine)){
            return false;
        }

        // we need to follow the path to selected
        // and remove it from mine
        if (mine instanceof WritingEquation) {

            if (((WritingEquation) mine).deepLegal() ) {//&& countEquals(mine) == 1
                return true;
            }
        }
        return false;
    }

    protected boolean failsAdditionalConditions(Equation mine) {
        return false;
    }

    @Override
    protected void privateAct() {


        Equation newEq = ((WritingEquation) CalcEnterAction.mine).convert();
        if (passes(newEq.copy())) {

            ArrayList<String> vars = Util.getVars(newEq);
            ((InputLine) owner).deActivate();
            EquationLine line;
            if (vars.size() != 0 || countEquals(((WritingEquation) CalcEnterAction.mine)) == 1) {

                line = getAlgebraLine(((InputLine) owner).owner,newEq);

            } else {
                line = new OutputLine(owner.owner, newEq, getInputLine(((InputLine) owner).owner));
            }
            newEq.updateOwner(line);
            owner.owner.addLine(line);
            }else{
            planB();
        }
    }

    protected EquationLine getAlgebraLine(Main main, Equation newEq) {
        return new AlgebraLine(main, newEq);
    }

    protected InputLine getInputLine(Main main){
        return new InputLine(main);
    }

    protected boolean passes(Equation equation) {
        return true;
    }

    protected void planB() {
    }
}