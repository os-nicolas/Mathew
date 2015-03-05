package colin.example.algebrator.tuts;

import colin.example.algebrator.EmilyView;
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
        view.message.enQue(4000, "Hi!");
        view.message.enQue(3000, "thanks for installing Algebrator");
        view.message.enQue(3000, "go ahead and type an equation");
    }
}
