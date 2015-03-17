package colin.example.algebrator.tuts;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.Operations;
import colin.algebrator.eq.PowerEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public class PowerTut extends TutMessage {
    @Override
    protected String getSp_key() {
        return "power";
    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        Equation power = getPower(view.stupid);
        if (view instanceof ColinView
                && power!= null
                && ((PowerEquation)power).isSqrt() == false
                && !view.active()
                && !((ColinView)view).isSolved()
                && view.message.isOpen()){
            return true;
        }
        return false;
    }

    protected Equation getPower(Equation eq) {
        if (eq instanceof PowerEquation){//&& Operations.sortaNumber(eq.get(1)
            return eq;
        }
        for (Equation e:eq){
            Equation result = getPower(e);
            if (result != null){
                return result;
            }
        }

        return null;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_power));
    }
}
