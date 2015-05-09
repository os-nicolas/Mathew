package colin.example.algebrator.tuts;

import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
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
        if (
                // they can hit solve
                view instanceof EmilyView && view.getStupid() instanceof WritingEquation && ((WritingEquation)view.getStupid()).deepLegal()
                && (view.selected == null || !(view.selected.left() instanceof WritingLeafEquation && ((WritingLeafEquation)view.selected.left()).getDisplay(-1).equals("=")))
                // they are inactive
                && !view.active()
                // the message bar is very inactive
                && view.message.veryOpen()
                // they have entered something
                &&  !(view.getStupid().size() ==1 &&  view.getStupid().get(0) instanceof PlaceholderEquation)
                ){
            return true;
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_hitsolve));
    }
}
