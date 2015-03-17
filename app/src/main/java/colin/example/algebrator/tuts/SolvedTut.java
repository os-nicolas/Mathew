package colin.example.algebrator.tuts;

import java.util.Random;

import colin.algebrator.eq.DivEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.Operations;
import colin.algebrator.eq.VarEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public class SolvedTut extends TutMessage {

    @Override
    protected String getSp_key() {
        return "solved";
    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
//        if (alreadyShown(view)){
//            return false;
//        }
        if (view instanceof ColinView){
            // is one side a VarEq?
            return ((ColinView)view).isSolved();

        }
        return false;
    }

//    @Override
//    protected void privateAlwaysShow(SuperView view){
//    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime,Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_2));
    }
}
