package cube.d.n.commoncore.Action.MainActions;

import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 9/4/2015.
 */
public class LastAction extends MainAction {
    public LastAction(Main main) {
        super(main);
    }

    @Override
    public boolean canAct(){
        return main.next.hasLast();
    }

    @Override
    protected void privateAct() {
        main.next.last();
    }
}
