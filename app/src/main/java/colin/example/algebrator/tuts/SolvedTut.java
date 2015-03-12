package colin.example.algebrator.tuts;

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
        if (view instanceof ColinView){
            // is one side a VarEq?
            if (view.stupid.get(0) instanceof VarEquation){
                return Operations.sortaNumber(view.stupid.get(1));
            }else if(view.stupid.get(1) instanceof VarEquation){
                return Operations.sortaNumber(view.stupid.get(0));
            }
        }
        return false;
    }

    @Override
    protected void alwaysShow(SuperView view){
        view.message.enQue(shortTime,Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_1));
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime,Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_2));
    }
}
