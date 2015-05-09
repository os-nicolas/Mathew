package colin.example.algebrator.tuts;

import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 3/5/2015.
 */
public class RootDragTut  extends PowerTut {

    @Override
    protected String getSp_key() {
        return "rootDrag";
    }

    protected RootDragTut(){};
//    private static RootDragTut instance;
//    public static RootDragTut getInstance(){
//        if (instance == null){
//            instance = new RootDragTut();
//        }
//        return instance;
//    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        Equation power=  getPower(view.getStupid());
        if (view instanceof ColinView && power!= null
                && ((PowerEquation)power).isSqrt() == true
                && power.parent instanceof EqualsEquation
                && view.getStupid() instanceof EqualsEquation
                && !view.active()
                && !((ColinView)view).isSolved()
                && view.message.isOpen()){
            return true;
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_rootdrag));
    }
}