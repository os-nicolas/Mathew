package dash.dev.mathilda.helper.tuts;

import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.Action.helper.HelperCancelAction;
import cube.d.n.commoncore.Action.helper.HelperDoneAction;
import cube.d.n.commoncore.Action.helper.HelperEnterAction;
import cube.d.n.commoncore.Action.helper.HelperOkAction;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.ModeController;
import cube.d.n.commoncore.keyboards.AlgebraKeyboard;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InlineInputLine;
import cube.d.n.commoncore.lines.InputLine;
import dash.dev.mathilda.helper.ColinAct;

/**
 * Created by Colin_000 on 9/12/2015.
 */
public class HelperModeController extends ModeController {
    @Override
    public SuperAction getEnter(InputLine line) {
        return new HelperEnterAction(line);
    }

    @Override
    public boolean allowsSub(){
        return true;
    }

    public boolean DoneCanAct() {
        return ColinAct.getInstance() !=null;
    }

    public void DoneAct() {
        ColinAct.getInstance().finish();
    }

    @Override
    public SuperAction getCancel(InlineInputLine line) {
        return new HelperCancelAction(line);
    }

    @Override
    public SuperAction getOk(InlineInputLine line) {
        return new HelperOkAction(line);
    }

    @Override
    public SuperAction getDone(EquationLine line) {
        return new HelperDoneAction(line);
    }

    @Override
    public KeyBoard getAlgebraKeyboard(Main main, AlgebraLine algebraLine) {
        return new AlgebraKeyboard(main,algebraLine);
    }
}
