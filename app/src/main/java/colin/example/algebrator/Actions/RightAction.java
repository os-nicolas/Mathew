package colin.example.algebrator.Actions;

import colin.algebrator.eq.PlaceholderEquation;
import colin.example.algebrator.EmilyView;

/**
 * Created by Colin on 2/2/2015.
 */
public class RightAction extends Action {
    public RightAction(EmilyView emilyView) {
        super(emilyView);
    }
    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation)emilyView.selected).goDark();
            tryMoveRight();
        }
    }
}
