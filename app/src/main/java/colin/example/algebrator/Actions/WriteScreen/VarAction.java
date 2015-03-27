package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.VarEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.EmilyView;

public class VarAction extends Action<EmilyView> {


    public String var;

    public VarAction(EmilyView emilyView, String var) {
        super(emilyView);
        this.var = var;
    }

    @Override
    public boolean canAct() {
        return myView.selected instanceof PlaceholderEquation;
    }

    @Override
    protected void privateAct() {
        ((PlaceholderEquation) myView.selected).goDark();

        if (!(myView.selected.parent instanceof BinaryEquation)) {
            Equation newEq = new VarEquation(var, myView);
            myView.insert(newEq);
        } else {
            Equation oldEq = myView.selected;
            Equation holder = new WritingEquation(myView);
            Equation newEq = new VarEquation(var, myView);
            oldEq.replace(holder);
            holder.add(newEq);
            holder.add(oldEq);
            oldEq.setSelected(true);
        }
    }


}
