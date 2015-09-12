package cube.d.n.commoncore;

import android.util.Log;

import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.keyboards.CalcInputKeyboard;
import cube.d.n.commoncore.keyboards.HelperInputKeyboard;
import cube.d.n.commoncore.keyboards.InputKeyboard;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.keyboards.PracInputKeyboard;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 9/12/2015.
 */
public class ModeController {
    public KeyBoard getAlgebraKeyboard(AlgebraLine algebraLine) {
        return null;
    }

    public boolean DoneCanAct() {
        return false;
        return ColinAct.getInstance() !=null;

    }

    public void DoneAct() {
        //DO NOTHING
        ColinAct.getInstance().finish();
    }

    public InputKeyboard getInputKeyboard(InputLine inputLine) {
        if (app == InputLine.App.CALC){
            return new CalcInputKeyboard(owner,this);
        }else if (app == InputLine.App.HELP){
            return new HelperInputKeyboard(owner,this);
        }else if (app == InputLine.App.PRAC){
            return new PracInputKeyboard(owner,this);
        }else{
            Log.e("InputLine.getKeyboad", "value out of enum range");
            return new CalcInputKeyboard(owner,this);
        }
    }

    public abstract SuperAction getEnter(InputLine line);
}
