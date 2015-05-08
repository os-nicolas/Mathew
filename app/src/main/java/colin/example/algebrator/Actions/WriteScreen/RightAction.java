package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.PlaceholderEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.EmilyView;

/**
 * Created by Colin on 2/2/2015.
 */
public class RightAction extends Action<EmilyView> {
    public RightAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public boolean canAct() {
        return canMoveRight()|| myView.selected.right() != null;

    }

    @Override
    protected void privateAct() {
        ((PlaceholderEquation) myView.selected).goDark();
        tryMoveRight();
    }
}
