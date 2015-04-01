package colin.example.algebrator.tuts;

import colin.algebrator.eq.WritingEquation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.Actions.WriteScreen.VarAction;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public class HitSolveMessage extends TutMessage {
    @Override
    protected String getSp_key() {
        return "hitSolved";
    }

    protected HitSolveMessage(){};
//    private static HitSolveMessage instance;
//    public static HitSolveMessage getInstance(){
//        if (instance == null){
//            instance = new HitSolveMessage();
//        }
//        return instance;
//    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        if (view instanceof EmilyView && view.getStupid() instanceof WritingEquation && ((WritingEquation)view.getStupid()).deepLegal() &&
                Action.countEquals(view.getStupid()) == 1 &&
                (view.selected == null || !(view.selected.left() instanceof WritingLeafEquation && ((WritingLeafEquation)view.selected.left()).getDisplay(-1).equals("=")))
                && !view.active()){
            return true;
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_hitsolve));
    }
}
