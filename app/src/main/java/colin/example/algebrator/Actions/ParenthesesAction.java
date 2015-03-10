package colin.example.algebrator.Actions;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.algebrator.eq.WritingPraEquation;
import colin.example.algebrator.EmilyView;

public class ParenthesesAction extends Action {

    private boolean left;

    public ParenthesesAction(EmilyView emilyView, boolean left) {
        super(emilyView);
        this.left = left;
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation) emilyView.selected).goDark();


            Equation newEq = new WritingPraEquation(left, emilyView);


            boolean can = true;
            boolean op = false;
            Equation next = (left ? emilyView.selected.right() : emilyView.left());


            if (next instanceof WritingLeafEquation) {
                if (left){
                    op = ((WritingLeafEquation) next).isOpRight();
                }else {
                    op = ((WritingLeafEquation) next).isOpLeft();
                }
            }
            can &= !op;

            if (can) {
                if (!(emilyView.selected.parent instanceof BinaryEquation)) {
                    emilyView.insert(newEq);
                } else {
                    Equation oldEq = emilyView.selected;
                    Equation holder = new WritingEquation(emilyView);
                    oldEq.replace(holder);
                    holder.add(newEq);
                    holder.add(oldEq);
                    oldEq.setSelected(true);
                }
            }
        }
    }


}
