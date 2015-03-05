package colin.example.algebrator.tuts;

import colin.algebrator.eq.EqualsEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PowerEquation;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 3/5/2015.
 */
public class PowerDragTut extends PowerTut {

    @Override
    protected String getSp_key() {
        return "powerDrag";
    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        Equation power=  getPower(view.stupid);
        if (view instanceof ColinView && power!= null
                && ((PowerEquation)power).isSqrt() == false
                && power.parent instanceof EqualsEquation
                && !view.active()){
            return true;
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(4000,new String[]{"to root both sides,", "drag an exponent to the other side"});
    }
}
