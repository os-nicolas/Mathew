package colin.example.algebrator.tuts;

import colin.algebrator.eq.AddEquation;
import colin.algebrator.eq.DivEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 3/11/2015.
 */
public class DivTut extends WriteMessage {
    @Override
    protected String getSp_key() {
        return "div";
    }

    protected DivTut(){}
//    private static DivTut instance;
//    public static DivTut getInstance(){
//        if (instance == null){
//            instance = new DivTut();
//        }
//        return instance;
//    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        if (view instanceof ColinView){
            // is one of the roots an add?
            if ((view.stupid.get(0) instanceof DivEquation || view.stupid.get(1) instanceof DivEquation)
                    && !view.active()
                    && !((ColinView)view).isSolved()
                    && view.message.isOpen()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_div));
    }
}
