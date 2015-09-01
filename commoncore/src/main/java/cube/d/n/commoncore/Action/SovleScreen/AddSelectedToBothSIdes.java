package cube.d.n.commoncore.Action.SovleScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Action.ActionWithDisplay;
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
import cube.d.n.commoncore.eq.any.SignEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin on 8/31/2015.
 */
public class AddSelectedToBothSIdes extends SelectedOpAction {
    public AddSelectedToBothSIdes(AlgebraLine line) {
        super(line);
    }

    @Override
    protected void privateAct() {
        EqualsEquation myStup = getResultEq();

        setNewStupid(myStup);
    }

    private EqualsEquation getResultEq() {
        Equation sel = getSel();
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
        return myStup;
    }


    @Override
    public boolean canAct() {
        Equation sel = getSel();
        Equation stup = ((AlgebraLine)owner).stupid.get();
        return canAct(sel, stup);
    }

    public static boolean canAct(Equation sel, Equation stup) {
        if (sel != null){
            if (stup instanceof EqualsEquation && stup.addContain(sel)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Equation getDisplay() {
        Equation res = new WritingEquation(owner);
        res.add(new VarEquation("Add ",owner));
        res.add(getSel().copy().negate());
        res.add(new VarEquation(" to both sides ",owner));
        return res;
    }
}
