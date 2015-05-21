package dash.dev.mathilda.helper.Actions.Solve;

import android.app.Activity;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Selects;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.Line;
import dash.dev.mathilda.helper.ColinAct;

/**
 * Created by Colin_000 on 5/20/2015.
 */
public class DoneAction extends Action {
    public DoneAction(Line line) {
        super(line);
    }

    @Override
    public boolean canAct(){
        return ColinAct.getInstance() !=null;
    }

    @Override
    protected void privateAct() {
        ColinAct.getInstance().finish();
    }
}
