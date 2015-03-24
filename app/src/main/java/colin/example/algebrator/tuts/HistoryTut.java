package colin.example.algebrator.tuts;

import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public class HistoryTut extends TutMessage {
    @Override
    protected String getSp_key() {
        return "history";
    }

    protected HistoryTut(){}
//    private static HistoryTut instance;
//    public static HistoryTut getInstance(){
//        if (instance == null){
//            instance = new HistoryTut();
//        }
//        return instance;
//    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        if (view instanceof ColinView && ((ColinView)view).history.size() >1){
            return true;
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_history));
    }
}
