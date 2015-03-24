package colin.example.algebrator.tuts;

import colin.algebrator.eq.EqualsEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PowerEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 3/5/2015.
 */
public class RootTut  extends PowerTut {

    @Override
    protected String getSp_key() {
        return "root";
    }

    protected RootTut(){};
//    private static RootTut instance;
//    public static RootTut getInstance(){
//        if (instance == null){
//            instance = new RootTut();
//        }
//        return instance;
//    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        Equation power=  getPower(view.stupid);
        if (view instanceof ColinView
                && power!= null
                && ((PowerEquation)power).isSqrt() == true
                && !view.active()
                && !((ColinView)view).isSolved()
                && view.message.isOpen()){
            return true;
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_root));
    }
}