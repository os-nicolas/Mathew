package colin.example.algebrator.tuts;

import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public class SolveMessage extends TutMessage{
    @Override
    protected String getSp_key() {
        return "solve";
    }

    protected SolveMessage(){};
//    private static SolveMessage instance;
//    public static SolveMessage getInstance(){
//        if (instance == null){
//            instance = new SolveMessage();
//        }
//        return instance;
//    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        return view instanceof ColinView;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_solvemessage_1));
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_solvemessage_2));
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_solvemessage_3));
    }
}
