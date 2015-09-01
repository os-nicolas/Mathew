package cube.d.n.commoncore.Action.SovleScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Action.ActionWithDisplay;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MultiDivSuperEquation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.SignEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 8/31/2015.
 */
public class MultiBySelected extends SelectedOpAction {
    public MultiBySelected(AlgebraLine owner) {
        super(owner);
    }

    @Override
    public Equation getDisplay() {

        Equation res = new WritingEquation(owner);
        res.add(new VarEquation("Multiply both sides by ",owner));
        res.add(getSel().copy());
        return res;
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

        if (myStup.get(side) instanceof MultiEquation){
            myStup.get(side).add(mySel);
        }else{

            Equation old = myStup.get(side);

            MultiEquation empty = new MultiEquation(owner);
                old.replace(empty);
                empty.add(old);
                empty.add(mySel);
        }
        return myStup;
    }



    @Override
    public boolean canAct() {
        Equation sel = getSel();
        Equation stup = ((AlgebraLine)owner).stupid.get();
        return canAct(stup,sel);
    }

    public static boolean canAct(Equation stup, Equation sel) {
        if (sel != null && stup instanceof EqualsEquation){
            while (sel.parent instanceof SignEquation){
                sel = sel.parent;
            }

                // sadly we are not done.
                // we need find the multiDivEqaution
                // that is closest to root on sel side

                // the only case we want to hande is :
                // a/5 = 100 where 5 is selected
                int side = ((EqualsEquation)stup).side(sel);
                Equation sideRoot = stup.get(side);
                while (sideRoot instanceof SignEquation){
                    sideRoot = sideRoot.get(0);
                }
                return sideRoot instanceof DivEquation && sideRoot.get(1).equals(sel);

                // and see if sel is on top
                // if it's not on top return true
                //return !((MultiDivSuperEquation)sideRoot).onTop(sel);

        }
        return false;
    }

}
