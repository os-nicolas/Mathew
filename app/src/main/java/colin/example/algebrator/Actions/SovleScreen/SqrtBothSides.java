package colin.example.algebrator.Actions.SovleScreen;

import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.ColinView;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class SqrtBothSides extends Action<ColinView> {

    public SqrtBothSides(ColinView colinView) {
        super(colinView);
    }

    @Override
    protected void privateAct() {
        Equation myStupid =  myView.getStupid().copy();
        if (myStupid instanceof EqualsEquation) {
            for (Equation e : myStupid) {
                Equation old = e;
                Equation newEq = new PowerEquation(myView);
                old.replace(newEq);
                newEq.add(old);
                newEq.add(NumConstEquation.create(.5, myView));
            }
            myView.setStupid(myStupid);
        }else{
            Equation newEq = new PowerEquation(myView);
            newEq.add(myStupid);
            newEq.add(NumConstEquation.create(.5, myView));
            myView.setStupid(newEq);
        }
        myView.changed();
    }
}
