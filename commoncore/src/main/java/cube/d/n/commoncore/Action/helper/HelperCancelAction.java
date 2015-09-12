package cube.d.n.commoncore.Action.helper;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.InlineInputLine;

/**
 * Created by Colin_000 on 5/20/2015.
 */
public class HelperCancelAction extends Action {
    public HelperCancelAction(InlineInputLine line) {
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
