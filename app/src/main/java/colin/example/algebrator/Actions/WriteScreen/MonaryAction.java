package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.EmilyView;

/**
 * Created by Colin on 2/22/2015.
 */
public abstract class MonaryAction extends Action<EmilyView> {

    public MonaryAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public boolean canAct() {
        return myView.selected instanceof PlaceholderEquation;
    }

    protected void monaryInsert(Equation newEq) {
        ((PlaceholderEquation) myView.selected).goDark();

        if (myView.selected.parent instanceof WritingEquation) {
            myView.insert(newEq);
        } else {
            Equation oldEq = myView.selected;
            Equation holder = new WritingEquation(myView);
            oldEq.replace(holder);
            holder.add(newEq);
            holder.add(oldEq);
            oldEq.setSelected(true);
        }
    }
}
