package colin.example.algebrator.tuts;

import colin.example.algebrator.Algebrator;
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

    protected SolvedTut(){};
//    private static SolvedTut instance;
//    public static SolvedTut getInstance(){
//        if (instance == null){
//            instance = new SolvedTut();
//        }
//        return instance;
//    }

    public boolean okToShow= false;

    @Override
    protected boolean privateShouldShow(SuperView view) {
        return okToShow;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime,Algebrator.getAlgebrator().getResources().getString(R.string.tut_solved_2));
    }
}
