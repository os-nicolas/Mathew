package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin on 2/2/2015.
 */
public class LeftAction extends Action {
    public LeftAction(InputLine emilyView) {
        super(emilyView);
    }

    @Override
    public boolean canAct(){
        return canMoveLeft(((InputLine) owner).getSelected()) || ((InputLine) owner).left() != null;
    }

    @Override
    protected void privateAct() {
        ((InputLine) owner).getSelected().goDark();
        tryMoveLeft(((InputLine) owner).getSelected());
    }
}
