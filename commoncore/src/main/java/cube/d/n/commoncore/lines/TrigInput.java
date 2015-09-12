package cube.d.n.commoncore.lines;

import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.keyboards.SimpleCalcKeyboard;
import cube.d.n.commoncore.keyboards.TrigInputKeyboard;

/**
 * Created by Colin_000 on 8/25/2015.
 */
public class TrigInput extends InputLine {
    public TrigInput(Main main,App app) {
        super(main,app);
    }

    @Override
    public KeyBoard getKeyboad() {
        if (myKeyBoard == null){
            myKeyBoard = new TrigInputKeyboard(owner,this);
        }
        return myKeyBoard;
    }
}
