package colin.example.algebrator.Actions.BothSides;

import android.app.Activity;

import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.BothSidesView;
import colin.example.algebrator.ColinView;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class CancelAction extends Action<BothSidesView>{

    @Override
    public boolean canAct(){
        return myView.myActivity != null;
    }

    @Override
    protected void privateAct() {
        myView.myActivity.finish();
    }

    public CancelAction(BothSidesView myView) {
        super(myView);
    }
}
