package cube.d.n.commoncore.Action.BothSides;


import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.InlineInputLine;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class CancelAction extends Action {

    @Override
    public boolean canAct(){
        return true;
    }

    @Override
    protected void privateAct() {
        owner.owner.revert();

    }

    public CancelAction(InlineInputLine myView) {
        super(myView);
    }
}
