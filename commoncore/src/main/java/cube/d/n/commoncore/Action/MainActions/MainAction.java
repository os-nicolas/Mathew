package cube.d.n.commoncore.Action.MainActions;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 9/4/2015.
 */
public abstract class MainAction extends SuperAction {
    Main main;

    public MainAction(Main main) {
        this.main = main;
    }
}
