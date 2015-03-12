package colin.example.algebrator.tuts;

import colin.example.algebrator.Algebrator;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public class WriteMessage extends TutMessage {
    @Override
    protected String getSp_key() {
        return "write";
    }

    @Override
    protected boolean privateShouldShow(SuperView view) {
        return view instanceof EmilyView;
    }

    @Override
    protected void privateShow(SuperView view) {
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_writemessage_1));
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_writemessage_2));
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_writemessage_3));
        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_writemessage_4));
    }
}
