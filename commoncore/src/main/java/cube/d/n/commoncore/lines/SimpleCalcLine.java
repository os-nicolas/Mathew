package cube.d.n.commoncore.lines;

import cube.d.n.commoncore.HasHeaderLine;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.keyboards.BothSidesKeyBoard;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.keyboards.SimpleCalcKeyboard;

/**
 * Created by Colin_000 on 5/21/2015.
 */
public class SimpleCalcLine extends InputLine  implements HasHeaderLine {
    public SimpleCalcLine(Main owner, App app) {
        super(owner,app);
    }

    private KeyBoard myKeyBoard = null;
    @Override
    public KeyBoard getKeyboad() {
        if (myKeyBoard == null){
            myKeyBoard = new SimpleCalcKeyboard(owner,this);
        }
        return myKeyBoard;
    }

    @Override
    public void setKeyBoard(KeyBoard k) {
        myKeyBoard = (SimpleCalcKeyboard)k;
    }
}
