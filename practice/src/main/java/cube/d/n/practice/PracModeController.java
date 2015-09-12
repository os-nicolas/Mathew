package cube.d.n.practice;

import cube.d.n.commoncore.Action.BothSides.CancelAction;
import cube.d.n.commoncore.Action.BothSides.CheckAction;
import cube.d.n.commoncore.Action.SolvableEnterAction;
import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.Action.WriteScreen.CalcEnterAction;
import cube.d.n.commoncore.ModeController;
import cube.d.n.commoncore.lines.InlineInputLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 9/12/2015.
 */
public class PracModeController extends ModeController {
    @Override
    public SuperAction getEnter(InputLine line) {
        return new SolvableEnterAction(line);
    }

    @Override
    public SuperAction getCancel(InlineInputLine line) {
        return new CancelAction(line);
    }

    @Override
    public SuperAction getOk(InlineInputLine line) {
        return new CheckAction(line);
    }
}
