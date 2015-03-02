package colin.example.algebrator.tuts;

import colin.example.algebrator.ColinView;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public class HistoryTut extends TutMessage {
    @Override
    protected String getSp_key() {
        return "history";
    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        if (view instanceof ColinView && ((ColinView)view).history.size() >1){
            return true;
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(4000,new String[]{"to revert to a previous step","tap it and hold"});
    }
}
