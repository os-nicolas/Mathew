package cube.d.n.commoncore.lines;

import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.keyboards.AlgebraKeyboardNoReturn;
import cube.d.n.commoncore.keyboards.EnptyKeyboard;
import cube.d.n.commoncore.keyboards.KeyBoard;

/**
 * Created by Colin_000 on 7/7/2015.
 */
public class AlgebraLineNoReturn extends AlgebraLine {

        private KeyBoard myKeyBoard = null;

        public AlgebraLineNoReturn(Main owner) {
            super(owner);
        }

        public AlgebraLineNoReturn(Main owner, Equation newEq) {
            super(owner, newEq);
        }

        @Override
        public KeyBoard getKeyboad() {
            if (myKeyBoard == null){
                myKeyBoard = new AlgebraKeyboardNoReturn(owner,this);
            }
            return myKeyBoard;
        }

}
