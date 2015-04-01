package colin.example.algebrator.tuts;

import colin.algebrator.eq.AddEquation;
import colin.algebrator.eq.DivEquation;
import colin.algebrator.eq.EqualsEquation;
import colin.algebrator.eq.Operations;
import colin.algebrator.eq.VarEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 3/11/2015.
 */
public class AddTut extends TutMessage {
    @Override
    protected String getSp_key() {
        return "add";
    }

    protected AddTut(){}
//    private static AddTut instance;
//    public static AddTut getInstance(){
//        if (instance == null){
//            instance = new AddTut();
//        }
//        return instance;
//    }


    @Override
    protected boolean privateShouldShow(SuperView view) {
        if (view instanceof ColinView){
            // is one of the roots an add?
            if ((view.getStupid().get(0) instanceof AddEquation || view.getStupid().get(1) instanceof AddEquation)
                    && !view.active()
                    && view.getStupid() instanceof EqualsEquation
                    && !((ColinView)view).isSolved()
                    && view.message.isOpen()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void privateShow(SuperView view) {
        //view.message.enQue(longTime, Algebrator.getAlgebrator().getResources().getStringArray(R.array.tut_add));


//        <string-array name="tut_add">
//        <item>To add/subtract a term from both sides, </item>
//        <item>drag the term to the other side.</item>
//        </string-array>

    }
}
