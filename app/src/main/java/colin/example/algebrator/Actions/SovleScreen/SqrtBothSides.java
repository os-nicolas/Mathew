package colin.example.algebrator.Actions.SovleScreen;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.NumConstEquation;
import colin.algebrator.eq.PowerEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.SolveScreen;

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
        for (Equation e: myStupid){
            Equation old = e;
            Equation newEq = new PowerEquation(myView);
            old.replace(newEq);
            newEq.add(old);
            newEq.add(NumConstEquation.create(.5,myView));
        }
        myView.setStupid(myStupid);
        myView.changed = true;

    }
}