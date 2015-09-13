package cube.d.n.commoncore;

import cube.d.n.commoncore.Action.BothSides.CancelAction;
import cube.d.n.commoncore.Action.BothSides.CheckAction;
import cube.d.n.commoncore.Action.SovleScreen.Done;
import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.Action.WriteScreen.CalcEnterAction;
import cube.d.n.commoncore.keyboards.AlgebraKeyboard;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InlineInputLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 9/12/2015.
 */
public class CalcModeController extends ModeController {
    @Override
    public KeyBoard getAlgebraKeyboard(Main main,AlgebraLine algebraLine) {
        return new AlgebraKeyboard(main,algebraLine);
    }

    @Override
    public SuperAction getEnter(InputLine line) {
        return new CalcEnterAction(line);
    }

    @Override
    public SuperAction getCancel(InlineInputLine line) {
        return new CancelAction(line);
    }

    @Override
    public SuperAction getOk(InlineInputLine line) {
        return new CheckAction(line);
    }

    @Override
    public SuperAction getDone(EquationLine line) {
        return new Done(line);
    }

    @Override
    public boolean bothSidesPopUps() {
        return false;
    }
    @Override
    public String getMultiSymbol() {
        return "*";
    }
    @Override
    public String getVar1() {
        return "x";
    }
    @Override
    public String getVar2() {
        return "y";
    }
    @Override
    public boolean allowsSub(){
        return true;
    }
}
