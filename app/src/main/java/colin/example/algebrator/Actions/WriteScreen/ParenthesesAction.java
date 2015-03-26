package colin.example.algebrator.Actions.WriteScreen;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.algebrator.eq.WritingPraEquation;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.Actions.Action;

public class ParenthesesAction extends Action<EmilyView> {

    private boolean left;

    public ParenthesesAction(EmilyView emilyView, boolean left) {
        super(emilyView);
        this.left = left;
    }

    @Override
    protected void privateAct() {
        if (myView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation) myView.selected).goDark();


            Equation newEq = new WritingPraEquation(left, myView);


            boolean can = true;
            boolean op = false;
            Equation next = (left ? myView.selected.right() : myView.left());


            if (next instanceof WritingLeafEquation) {
                if (left){
                    op = ((WritingLeafEquation) next).isOpRight();
                }else {
                    op = ((WritingLeafEquation) next).isOpLeft();
                }
            }
            can &= !op;

            if (can) {
                if (!(myView.selected.parent instanceof BinaryEquation)) {
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
    }


}
