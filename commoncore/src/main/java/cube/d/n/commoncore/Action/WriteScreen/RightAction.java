package cube.d.n.commoncore.Action.WriteScreen;


import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin on 2/2/2015.
 */
public class RightAction extends Action {
    public RightAction(InputLine emilyView) {
        super(emilyView);
    }

    @Override
    public boolean canAct() {
        return canMoveRight(((InputLine)owner).getSelected())|| ((InputLine)owner).getSelected().right() != null;

    }

    @Override
    protected void privateAct() {
        ((PlaceholderEquation) ((InputLine)owner).getSelected()).goDark();
        tryMoveRight(((InputLine)owner).getSelected());
        updateOffsetX();
    }
}
