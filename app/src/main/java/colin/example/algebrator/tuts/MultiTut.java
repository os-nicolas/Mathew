package colin.example.algebrator.tuts;

import colin.algebrator.eq.DivEquation;
import colin.algebrator.eq.MultiEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 3/11/2015.
 */
public class MultiTut extends WriteMessage {
    @Override
    protected String getSp_key() {
        return "multi";
    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        if (view instanceof ColinView){
            // is one of the roots an add?
            if ((view.stupid.get(0) instanceof MultiEquation || view.stupid.get(1) instanceof MultiEquation) && !view.active() && view.message.isOpen()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_multi));
    }
}
