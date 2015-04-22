package colin.example.algebrator.tuts;

import cube.d.n.commoncore.eq.Equation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.PowerEquation;
import cube.d.n.commoncore.eq.VarEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public class PowerTut extends TutMessage {
    @Override
    protected String getSp_key() {
        return "power";
    }

    protected PowerTut(){};
//    private static PowerTut instance;
//    public static PowerTut getInstance(){
//        if (instance == null){
//            instance = new PowerTut();
//        }
//        return instance;
//    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        PowerEquation power =  getPower(view.getStupid());
        if (view instanceof ColinView
                && power!= null
                && power.isSqrt() == false
                && !(power.get(0) instanceof VarEquation)
                && !view.active()
                && !((ColinView)view).isSolved()
                && view.message.isOpen()){
            return true;
        }
        return false;
    }

    protected PowerEquation getPower(Equation eq) {
        if (eq instanceof PowerEquation){//&& Operations.sortaNumber(eq.get(1)
            return (PowerEquation)eq;
        }
        for (Equation e:eq){
            PowerEquation result = getPower(e);
            if (result != null){
                return result;
            }
        }

        return null;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_power));
    }
}
