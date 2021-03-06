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
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.SignEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 8/31/2015.
 */
public class DivBySelected extends SelectedOpAction {
    public DivBySelected(AlgebraLine owner) {
        super(owner);
    }

    @Override
    public Equation getDisplay(boolean shorten) {
        Equation res = new WritingEquation(owner);
        if (!shorten) {
            res.add(new VarEquation("Divide both sides by ", owner));
        }else{
            res.add(new VarEquation("Divide by ", owner));
        }
        res.add(getSel().copy());
        return res;
    }

    @Override
    protected void privateAct() {
        ((AlgebraLine) owner).tryWarn(getSel().copy());
        setNewStupid((EqualsEquation)getResultEq());
    }

    @Override
    public boolean canAct() {
        Equation sel = getSel();

        Equation stup = ((AlgebraLine)owner).stupid.get();
        return canAct(stup,sel);
    }

    public Equation getResultEq() {
        Equation sel = getSel();
        EqualsEquation stup = (EqualsEquation)(((AlgebraLine) owner).stupid.get());
        EqualsEquation myStup = (EqualsEquation)stup.copy();

        Equation mySel = Util.getSimilarEquation(stup, sel, myStup);
        int side = (myStup.side(mySel)==0?1:0);
        if (mySel.parent.equals(myStup)){
            mySel.replace(NumConstEquation.create(1.0,mySel.owner));
        }else {
            mySel.remove();
        }
        //hmm here is a Question
        // 4*a = 12/5 do they want it to go to 4*a = (12/5)/4 or 4*a = (12/(5*4))
        // probably (12/5)/4

        Equation old = myStup.get(side);

        DivEquation empty = new DivEquation(owner);
        old.replace(empty);
        empty.add(old);
        empty.add(mySel);

        return  myStup;
    }

    public static boolean canAct(Equation stup, Equation sel) {
        // we don't want to divide both sides by 0
        if (sel!= null && Operations.sortaNumber(sel) && Operations.getValue(sel).doubleValue() ==0){
            return false;
        }

        if (sel != null  && stup instanceof EqualsEquation){
            while (sel.parent instanceof SignEquation){
                sel = sel.parent;
            }
            if (stup instanceof EqualsEquation && stup.DivMultiContain(sel)){

                // sadly we are not done.
                // we need find the multiDivEqaution
                // that is closest to root on sel side


                // we actually don't want to go so deep
                // the only cases we want to handle are like
                // 4*a = 55 where 4 is selected
                // 4 = 55 where 4 is selected
                int side = ((EqualsEquation)stup).side(sel);
                Equation sideRoot = stup.get(side);
                if (sideRoot.equals(sel)){
                    return true;
                }
                while ((sideRoot instanceof SignEquation ) && !sideRoot.equals(sel)){
                    sideRoot = sideRoot.get(0);
                }
                return (sideRoot.contains(sel)&& sideRoot instanceof MultiEquation) ||
                        (sideRoot instanceof DivEquation && sideRoot.get(0).equals(sel)) ;


                // and see if sel is on top
                // if it's on top return true

                //return ((MultiDivSuperEquation)sideRoot).onTop(sel);

            }
        }
        return false;
    }
}
