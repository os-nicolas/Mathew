package cube.d.n.commoncore.Action.helper;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 5/20/2015.
 */
public class HelperDoneAction extends Action {
    public HelperDoneAction(EquationLine line) {
        super(line);
    }

    @Override
    public boolean canAct(){
        return owner.modeController().DoneCanAct();
    }

    @Override
    protected void privateAct() {
        owner.modeController().DoneAct();
    }
}
