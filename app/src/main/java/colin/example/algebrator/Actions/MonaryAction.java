package colin.example.algebrator.Actions;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.EmilyView;

/**
 * Created by Colin on 2/22/2015.
 */
public abstract class MonaryAction extends Action {

    public MonaryAction(EmilyView emilyView) {
        super(emilyView);
    }

    protected void monaryInsert(Equation newEq) {
        if (emilyView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation)emilyView.selected).goDark();

            if (emilyView.selected.parent instanceof WritingEquation){
                emilyView.insert(newEq);
            }else{
                Equation oldEq = emilyView.selected;
                Equation holder = new WritingEquation(emilyView);
                oldEq.replace(holder);
                holder.add(newEq);
                holder.add(oldEq);
                oldEq.setSelected(true);
            }

//            Equation l = emilyView.left();
//            if ((l==null || !(l.parent instanceof BinaryEquation)) && !(emilyView.selected.parent instanceof BinaryEquation)) {
//                emilyView.insert(newEq);
//            } else {
//                Equation oldEq = emilyView.selected;
//                Equation holder = new WritingEquation(emilyView);
//                oldEq.replace(holder);
//                holder.add(newEq);
//                holder.add(oldEq);
//                oldEq.setSelected(true);
//            }
        }
    }
}
