package cube.d.n.commoncore;

import android.util.Log;

import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.keyboards.InputKeyboard;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InlineInputLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 9/12/2015.
 */
public abstract class ModeController {

    public abstract KeyBoard getAlgebraKeyboard(Main main,AlgebraLine algebraLine);

    public boolean DoneCanAct() {
        return false;

    }

    public void DoneAct() {
        //DO NOTHING
    }

//    public InputKeyboard getInputKeyboard(InputLine inputLine) {
//        if (app == InputLine.App.CALC){
//            return new CalcInputKeyboard(owner,this);
//        }else if (app == InputLine.App.HELP){
//            return new HelperInputKeyboard(owner,this);
//        }else if (app == InputLine.App.PRAC){
//            return new PracInputKeyboard(owner,this);
//        }else{
//            Log.e("InputLine.getKeyboad", "value out of enum range");
//            return new CalcInputKeyboard(owner,this);
//        }
//    }

    public abstract SuperAction getEnter(InputLine line);

    public abstract SuperAction getCancel(InlineInputLine line);

    public abstract SuperAction getOk(InlineInputLine line);

    public abstract SuperAction getDone(EquationLine line);

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
        return true;
    }

    public boolean bothSidesPopUps(){
        return false;
    };

    public boolean allowsSub(){
        return false;
    }

    public boolean hasClear(){
        return false;
    }
}
