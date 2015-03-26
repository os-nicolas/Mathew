package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.VarEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.Actions.Action;

public class VarAction extends Action<EmilyView> {


    public String var;

    public VarAction(EmilyView emilyView, String var) {
        super(emilyView);
        this.var = var;
    }

    @Override
    protected void privateAct() {
        if (myView.selected instanceof PlaceholderEquation) {
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
//        else if (myView.selected != null) {
//            Equation numEq = new VarEquation(var, myView);
//            addToBlock(numEq);
//        }
//        if (! (myView.selected instanceof WritingEquation) ){
//            if (myView.selected instanceof PlaceholderEquation) {
//                Equation newEq = new VarEquation(var, myView);
//                myView.selected.replace(newEq);
//                newEq.setSelected(true);
//            }else{
//                Equation newEq = new VarEquation(var, myView);
//                int at = myView.selected.parent.indexOf(myView.selected);
//                myView.selected.parent.add(at+1,newEq);
//                newEq.setSelected(true);
//            }
//        }else {
//            // TODO what happens here?
//        }

    }



}
