package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.EmilyView;

/**
 * Created by Colin on 2/22/2015.
 */
public abstract class InlineOpAction extends Action<EmilyView> {
    public InlineOpAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public boolean canAct(){
        if (myView.selected instanceof PlaceholderEquation) {


            Equation l = myView.left();
            boolean can = true;
            if (l instanceof WritingLeafEquation) {
                can = !((WritingLeafEquation) l).isOpLeft();
            }
            if (l instanceof WritingPraEquation && ((WritingPraEquation) l).left) {
                can = false;
            }

            if (l==null){
                can = false;
            }

            return can;
        }
        return false;
    }

    protected void inlineInsert(Equation newEq) {
        ((PlaceholderEquation) myView.selected).goDark();
        myView.insert(newEq);
    }
}
