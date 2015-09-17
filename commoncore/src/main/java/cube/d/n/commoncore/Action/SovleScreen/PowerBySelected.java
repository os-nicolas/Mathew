package cube.d.n.commoncore.Action.SovleScreen;

import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.SignEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.AlgebraLine;

/**
 * Created by Colin_000 on 9/16/2015.
 */
public class PowerBySelected  extends SelectedOpAction {
    public PowerBySelected(AlgebraLine owner) {
        super(owner);
    }

    @Override
    public Equation getDisplay(boolean shorten) {

        Equation res = new WritingEquation(owner);
        if (!shorten) {
            res.add(new VarEquation("Raise both sides to ", owner));
        }else {
            res.add(new VarEquation("Raise to ", owner));
        }
        res.add(Operations.flip(getSel().copy()));
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

        int startSide = myStup.side(mySel);
        int otherSide = (startSide+1)%2;//1->0 and 0->1

        myStup.set(startSide,mySel.parent.get(0));

        PowerEquation pe = new PowerEquation(owner);
        pe.add(myStup.get(otherSide));
        pe.add(Operations.flip(mySel));
        myStup.set(otherSide,pe);

        return myStup;
    }



    @Override
    public boolean canAct() {
        Equation sel = getSel();
        Equation stup = ((AlgebraLine)owner).stupid.get();
        return canAct(stup,sel);
    }

    public static boolean canAct(Equation stup, Equation sel) {
        // if we have a^0 to get ride of it we would take both sides to the 1/0 not good
        if (sel!= null && Operations.sortaNumber(sel) && Operations.getValue(sel).doubleValue() ==0){
            return false;
        }

        if (sel != null && stup instanceof EqualsEquation){
            while (sel.parent instanceof SignEquation){
                sel = sel.parent;
            }

            int side = ((EqualsEquation)stup).side(sel);
            Equation sideRoot = stup.get(side);

            return sideRoot instanceof PowerEquation && sideRoot.get(1).equals(sel);
        }
        return false;
    }

}
