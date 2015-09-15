package cube.d.n.practice;

import cube.d.n.commoncore.Action.BothSides.CancelAction;
import cube.d.n.commoncore.Action.BothSides.CheckAction;
import cube.d.n.commoncore.Action.SolvableEnterAction;
import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.CalcModeController;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.ModeController;
import cube.d.n.commoncore.keyboards.AlgebraKeyboard;
import cube.d.n.commoncore.keyboards.AlgebraKeyboardNoReturn;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.InlineInputLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 9/13/2015.
 */
public class ProblemModeController extends CalcModeController {

    @Override
    public KeyBoard getAlgebraKeyboard(Main main,AlgebraLine algebraLine) {
        return new AlgebraKeyboardNoReturn(main,algebraLine);
    }

    @Override
    public SuperAction getEnter(InputLine line) {
        return new SolvableEnterAction(line);
    }

    public boolean bothSidesPopUps(){
        return true;
    };

//    @Override
//    public SuperAction getCancel(InlineInputLine line) {
//        return new CancelAction(line);
//    }
//
//    @Override
//    public SuperAction getOk(InlineInputLine line) {
//        return new CheckAction(line);
//    }
}
