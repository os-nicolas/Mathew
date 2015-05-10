package cube.d.n.commoncore.Action.SovleScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.v2.lines.AlgebraLine;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class SqrtBothSides extends Action {

    public SqrtBothSides(AlgebraLine line) {
        super(line);
    }

    @Override
    protected void privateAct() {
        Equation myStupid =  owner.stupid.get().copy();
        if (myStupid instanceof EqualsEquation) {
            for (Equation e : myStupid) {
                Equation old = e;
                Equation newEq = new PowerEquation(owner);
                old.replace(newEq);
                newEq.add(old);
                newEq.add(NumConstEquation.create(.5, owner));
            }
            owner.stupid.set(myStupid);
        }else{
            Equation newEq = new PowerEquation(owner);
            newEq.add(myStupid);
            newEq.add(NumConstEquation.create(.5, owner));
            owner.stupid.set(newEq);
        }
        ((AlgebraLine)owner).changed();
        ((AlgebraLine)owner).updateHistory();
    }
}
