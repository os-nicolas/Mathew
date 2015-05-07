package colin.example.algebrator.tuts;

import colin.example.algebrator.Algebrator;
import colin.example.algebrator.R;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 3/19/2015.
 */
public class TapToSkip extends WriteMessage {
    @Override
    protected String getSp_key() {
        return "writeSkip";
    }

    protected TapToSkip(){}

    @Override
    protected void privateShow(SuperView view) {
//        view.message.enQue(aveTime, Algebrator.getAlgebrator().getResources().getString(R.string.tut_writemessage_4));
    }
}
