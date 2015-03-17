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
        if (alreadyShown(view)){
            return false;
        }
        if (view instanceof ColinView){
            // is one side a VarEq?
            if (view.stupid.get(0) instanceof VarEquation){
                return Operations.sortaNumber(view.stupid.get(1)) || isNumDiv(view.stupid.get(1));
            }else if(view.stupid.get(1) instanceof VarEquation){
                return Operations.sortaNumber(view.stupid.get(0)) || isNumDiv(view.stupid.get(0));
            }
        }
        return false;
    }


    private boolean isNumDiv(Equation eq){
        return (eq instanceof DivEquation && Operations.sortaNumber(eq.get(0)) && Operations.sortaNumber(eq.get(1)));
    }

    @Override
    protected void privateAlwaysShow(SuperView view){
        Random  r = new Random();
        int i = r.nextInt()%4;
        if (i==0) {
            view.message.enQue(shortTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_1_0));
        }else if (i==1){
            view.message.enQue(shortTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_1_1));
        }else if (i==2){
            view.message.enQue(shortTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_1_2));
        }else if (i==3){
            view.message.enQue(shortTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_1_3));
        }
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime,Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_2));
    }
}
