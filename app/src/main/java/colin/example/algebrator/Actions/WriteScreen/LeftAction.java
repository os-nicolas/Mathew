package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.Actions.Action;

/**
 * Created by Colin on 2/2/2015.
 */
public class LeftAction extends Action<EmilyView> {
    public LeftAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public boolean canAct(){
        return canMoveLeft() || myView.left() != null;
    }

    @Override
    protected void privateAct() {
        ((PlaceholderEquation) myView.selected).goDark();
        tryMoveLeft();
    }
}
