package cube.d.n.commoncore.keyboards;

import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.Action.helper.HelperDoneAction;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 9/12/2015.
 */
public class HelperAlgebraKeyboard extends AlgebraKeyboard {
    public HelperAlgebraKeyboard(Main owner, AlgebraLine algebraLine) {
        super(owner, algebraLine);
    }

    @Override
    protected SuperAction getDone(EquationLine line) {
        return new HelperDoneAction(line);
    }
}
