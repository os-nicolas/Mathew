package cube.d.n.commoncore.lines;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.keyboards.EnptyKeyboard;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 7/5/2015.
 */
public class AlgebraLineNoKeyBoard extends AlgebraLine {

    public AlgebraLineNoKeyBoard(Main owner) {
        super(owner);
    }

    public AlgebraLineNoKeyBoard(Main owner, Equation newEq) {
        super(owner, newEq);
    }

    @Override
    public KeyBoard getKeyboad() {
        if (myKeyBoard == null){
            myKeyBoard = new EnptyKeyboard(owner);
        }
        return myKeyBoard;
    }

}
