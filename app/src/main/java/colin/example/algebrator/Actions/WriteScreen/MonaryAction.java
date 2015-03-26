package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.Actions.Action;

/**
 * Created by Colin on 2/22/2015.
 */
public abstract class MonaryAction extends Action<EmilyView> {

    public MonaryAction(EmilyView emilyView) {
        super(emilyView);
    }

    protected void monaryInsert(Equation newEq) {
        if (myView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation) myView.selected).goDark();

            if (myView.selected.parent instanceof WritingEquation){
                myView.insert(newEq);
            }else{
                Equation oldEq = myView.selected;
                Equation holder = new WritingEquation(myView);
                oldEq.replace(holder);
                holder.add(newEq);
                holder.add(oldEq);
                oldEq.setSelected(true);
            }

//            Equation l = myView.left();
//            if ((l==null || !(l.parent instanceof BinaryEquation)) && !(myView.selected.parent instanceof BinaryEquation)) {
//                myView.insert(newEq);
//            } else {
//                Equation oldEq = myView.selected;
//                Equation holder = new WritingEquation(myView);
//                oldEq.replace(holder);
//                holder.add(newEq);
//                holder.add(oldEq);
//                oldEq.setSelected(true);
//            }
        }
    }
}
