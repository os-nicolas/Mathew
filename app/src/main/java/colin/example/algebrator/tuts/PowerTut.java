package colin.example.algebrator.tuts;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.Operations;
import colin.algebrator.eq.PowerEquation;
import colin.example.algebrator.ColinView;
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
        if (view instanceof ColinView && getPower(view.stupid)!= null){
            return true;
        }
        return false;
    }

    private Equation getPower(Equation eq) {
        if (eq instanceof PowerEquation && Operations.sortaNumber(eq.get(1))){
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
        view.message.enQue(1000,"Nice work!");
        view.message.enQue(1000,"hit the back button to enter another equation");
    }
}
