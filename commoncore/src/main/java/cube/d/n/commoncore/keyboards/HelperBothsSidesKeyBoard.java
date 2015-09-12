package cube.d.n.commoncore.keyboards;

import cube.d.n.commoncore.Action.BothSides.CancelAction;
import cube.d.n.commoncore.Action.BothSides.CheckAction;
import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.Action.helper.HelperCancelAction;
import cube.d.n.commoncore.Action.helper.HelperOkAction;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.InlineInputLine;

/**
 * Created by Colin_000 on 9/11/2015.
 */
public class HelperBothsSidesKeyBoard extends  BothSidesKeyBoard {
    public HelperBothsSidesKeyBoard(Main owner, InlineInputLine line) {
        super(owner, line);
    }

    private SuperAction getCancel(InlineInputLine line) {
        return new HelperCancelAction(line);
    }

    private SuperAction getOk(InlineInputLine line) {
        return new HelperOkAction(line);
    }
}
