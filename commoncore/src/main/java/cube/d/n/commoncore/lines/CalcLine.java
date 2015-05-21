package cube.d.n.commoncore.lines;

import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.keyboards.SimpleCalcKeyboard;

/**
 * Created by Colin_000 on 5/21/2015.
 */
public class CalcLine extends InputLine {
    public CalcLine(Main owner) {
        super(owner);
    }

    private KeyBoard myKeyBoard = null;
    @Override
    public KeyBoard getKeyboad() {
        if (myKeyBoard == null){
            myKeyBoard = new SimpleCalcKeyboard(owner,this);
        }
        return myKeyBoard;
    }
}
