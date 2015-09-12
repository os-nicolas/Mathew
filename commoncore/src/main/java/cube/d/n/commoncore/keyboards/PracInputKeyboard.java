package cube.d.n.commoncore.keyboards;

import cube.d.n.commoncore.Action.SolvableEnterAction;
import cube.d.n.commoncore.Action.SuperAction;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 9/11/2015.
 */
public class PracInputKeyboard extends InputKeyboard {
    public PracInputKeyboard(Main owner, InputLine inputLine) {
        super(owner,inputLine);
    }

    @Override
    protected SuperAction getEnter(InputLine line) {
        return new SolvableEnterAction(line);
    }
}
