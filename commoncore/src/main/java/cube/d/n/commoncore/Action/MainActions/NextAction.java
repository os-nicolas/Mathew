package cube.d.n.commoncore.Action.MainActions;

import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 9/4/2015.
 */
public class NextAction extends MainAction {
    public NextAction(Main owner) {
        super(owner);
    }

    @Override
    public boolean canAct(){
        return main.next.hasNext();
    }

    @Override
    protected void privateAct() {
        main.next.next();
    }

}
