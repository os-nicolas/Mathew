package cube.d.n.practice;

import cube.d.n.commoncore.CalcModeController;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.ModeController;
import cube.d.n.commoncore.keyboards.AlgebraKeyboard;
import cube.d.n.commoncore.keyboards.AlgebraKeyboardJustReturn;
import cube.d.n.commoncore.keyboards.AlgebraKeyboardNoReturn;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.lines.AlgebraLine;

/**
 * Created by Colin_000 on 9/13/2015.
 */
public class PracCalcModeController extends CalcModeController {

    @Override
    public KeyBoard getAlgebraKeyboard(Main main,AlgebraLine algebraLine) {
        return new AlgebraKeyboardJustReturn(main,algebraLine);
    }

    public String getVar1(){
        return "a";
    }

    public boolean hasB() {
        return true;
    }

    public String getVar2() {
        return "b";
    }
    //
    public String getMultiSymbol() {
        char[] timesUnicode = { '\u00D7'};
        return new String(timesUnicode);
    }

    public boolean showReduce(){
        return false;
    }

    public boolean bothSidesPopUps(){
        return true;
    };

    @Override
    public boolean allowsSub(){
        return false;
    }
}
