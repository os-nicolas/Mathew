package colin.example.algebrator.tuts;

import colin.example.algebrator.ColinView;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public class SolveMessage extends TutMessage{
    @Override
    protected String getSp_key() {
        return "solve";
    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        return view instanceof ColinView;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(2000, "this is the solve screen");
        view.message.enQue(4000, new String[]{"to move numbers of variables around"," simply drag and drop them"});
        view.message.enQue(4000, new String[]{"to add, subtract, mulipy or divide","double tap on +, -, \u00D7 or \u00F7"});
    }
}
