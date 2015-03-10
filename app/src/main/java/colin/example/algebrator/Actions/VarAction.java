package colin.example.algebrator.Actions;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.VarEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.EmilyView;

public class VarAction extends Action {


    public String var;

    public VarAction(EmilyView emilyView, String var) {
        super(emilyView);
        this.var = var;
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation)emilyView.selected).goDark();

            if (!(emilyView.selected.parent instanceof BinaryEquation)) {
                Equation newEq = new VarEquation(var, emilyView);
                emilyView.insert(newEq);
            } else {
                Equation oldEq = emilyView.selected;
                Equation holder = new WritingEquation(emilyView);
                Equation newEq = new VarEquation(var, emilyView);
                oldEq.replace(holder);
                holder.add(newEq);
                holder.add(oldEq);
                oldEq.setSelected(true);
            }
        }
//        else if (emilyView.selected != null) {
//            Equation numEq = new VarEquation(var, emilyView);
//            addToBlock(numEq);
//        }
//        if (! (emilyView.selected instanceof WritingEquation) ){
//            if (emilyView.selected instanceof PlaceholderEquation) {
//                Equation newEq = new VarEquation(var, emilyView);
//                emilyView.selected.replace(newEq);
//                newEq.setSelected(true);
//            }else{
//                Equation newEq = new VarEquation(var, emilyView);
//                int at = emilyView.selected.parent.indexOf(emilyView.selected);
//                emilyView.selected.parent.add(at+1,newEq);
//                newEq.setSelected(true);
//            }
//        }else {
//            // TODO what happens here?
//        }

    }


}
