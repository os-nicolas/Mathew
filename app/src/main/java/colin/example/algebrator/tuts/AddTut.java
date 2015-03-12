package colin.example.algebrator.tuts;

import colin.algebrator.eq.AddEquation;
import colin.algebrator.eq.DivEquation;
import colin.algebrator.eq.Operations;
import colin.algebrator.eq.VarEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 3/11/2015.
 */
public class AddTut extends TutMessage {
    @Override
    protected String getSp_key() {
        return "add";
    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        if (view instanceof ColinView){
            // is one of the roots an add?
            if ((view.stupid.get(0) instanceof AddEquation || view.stupid.get(1) instanceof AddEquation) && !view.active() && view.message.isOpen()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_add));
    }
}
