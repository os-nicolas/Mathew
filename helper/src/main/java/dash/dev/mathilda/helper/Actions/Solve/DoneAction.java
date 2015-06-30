package dash.dev.mathilda.helper.Actions.Solve;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.lines.EquationLine;
import dash.dev.mathilda.helper.ColinAct;

/**
 * Created by Colin_000 on 5/20/2015.
 */
public class DoneAction extends Action {
    public DoneAction(EquationLine line) {
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
