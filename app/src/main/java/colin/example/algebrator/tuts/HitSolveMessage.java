package colin.example.algebrator.tuts;

import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public class HitSolveMessage extends TutMessage {
    @Override
    protected String getSp_key() {
        return "hitSolved";
    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        if (view instanceof EmilyView && view.stupid instanceof WritingEquation && ((WritingEquation)view.stupid).deepLegal() && Action.countEquals(view.stupid) == 1 &&
                (view.selected == null || !(view.selected.left() instanceof WritingLeafEquation && ((WritingLeafEquation)view.selected.left()).getDisplay(-1).equals("=")))
                && !view.active()){
            return true;
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(4000,new String[]{"Once you have the equation you want","hit SOLVE to start manipulating it"});
    }
}
