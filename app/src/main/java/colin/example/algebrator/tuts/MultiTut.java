package colin.example.algebrator.tuts;

import colin.algebrator.eq.DivEquation;
import colin.algebrator.eq.EqualsEquation;
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

    protected MultiTut(){};
//    private static MultiTut instance;
//    public static MultiTut getInstance(){
//        if (instance == null){
//            instance = new MultiTut();
//        }
//        return instance;
//    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        if (view instanceof ColinView){
            // is one of the roots an add?
            if ((view.getStupid().get(0) instanceof MultiEquation || view.getStupid().get(1) instanceof MultiEquation)
                    && !view.active()
                    && view.getStupid() instanceof EqualsEquation
                    && !((ColinView)view).isSolved()
                    && view.message.isOpen()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
//        view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_multi));
//
//
//        <string-array name="tut_multi">
//        <item>To divide both sides by a term</item>
//        <item>drag the term to the other side.</item>
//        </string-array>
    }
}
