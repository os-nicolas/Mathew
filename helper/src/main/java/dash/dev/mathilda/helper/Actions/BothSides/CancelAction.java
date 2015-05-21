package dash.dev.mathilda.helper.Actions.BothSides;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.lines.BothSidesLine;

/**
 * Created by Colin_000 on 5/20/2015.
 */
public class CancelAction extends Action {
    public CancelAction(BothSidesLine line) {
        super(line);
    }

    @Override
    public boolean canAct(){
        return true;
    }

    @Override
    protected void privateAct() {
        owner.owner.revert();

    }
}
