package cube.d.n.commoncore.Action.SovleScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.PopUpButton;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.Operation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.PlusMinusEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin on 8/31/2015.
 */
public class AddSelectedToBothSIdes extends Action {
    public AddSelectedToBothSIdes(AlgebraLine line) {
        super(line);
    }

    @Override
    protected void privateAct() {
        Equation sel = ((AlgebraLine)owner).getSelected();
        EqualsEquation stup = (EqualsEquation)(((AlgebraLine) owner).stupid.get());
        EqualsEquation myStup = (EqualsEquation)stup.copy();

        Equation mySel = Util.getSimilarEquation(stup, sel, myStup);
        int side = (myStup.side(mySel)==0?1:0);
        mySel.remove();

        if (myStup.get(side) instanceof AddEquation){
            myStup.get(side).add(mySel.negate());
        }else{

            Equation old = myStup.get(side);
            if (Operations.sortaNumber(old) && Operations.getValue(old).doubleValue() == 0){
                old.replace(mySel.negate());
            }else{
                AddEquation empty = new AddEquation(owner);
                old.replace(empty);
                empty.add(old);
                empty.add(mySel.negate());
            }
        }

        myStup.fixIntegrety();

        owner.stupid.set(myStup);
        ((AlgebraLine)owner).getSelected().setSelected(false);
        ((AlgebraLine)owner).changed();
        ((AlgebraLine)owner).updateHistory();

    }

    @Override
    public boolean canAct() {
        Equation sel = ((AlgebraLine)owner).getSelected();
        Equation stup = ((AlgebraLine)owner).stupid.get();
        if (sel != null){
            if (stup instanceof EqualsEquation && stup.addContain(sel)){
                return true;
            }
        }
        return false;
    }
}
