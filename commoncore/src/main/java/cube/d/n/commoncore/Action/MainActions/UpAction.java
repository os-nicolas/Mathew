package cube.d.n.commoncore.Action.MainActions;

import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 9/4/2015.
 */
public class UpAction extends MainAction {
    public UpAction(Main owner) {
        super(owner);
    }

    @Override
    protected void privateAct() {
        main.next.finish();
    }
}
