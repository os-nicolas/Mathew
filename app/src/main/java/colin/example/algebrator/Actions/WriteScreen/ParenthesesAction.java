package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.EmilyView;

public class ParenthesesAction extends Action<EmilyView> {

    private boolean left;

    public ParenthesesAction(EmilyView emilyView, boolean left) {
        super(emilyView);
        this.left = left;
    }

    @Override
    public boolean canAct() {
        if (myView.selected instanceof PlaceholderEquation) {


            boolean can = true;
            boolean op = false;
            Equation next = (left ? myView.selected.right() : myView.left());


            if (next instanceof WritingLeafEquation) {
                if (left) {
                    op = ((WritingLeafEquation) next).isOpRight();
                } else {
                    op = ((WritingLeafEquation) next).isOpLeft();
                }
            }
            can &= !op;

            return can;
        }
        return false;

    }

    @Override
    protected void privateAct() {


        Equation newEq = new WritingPraEquation(left, myView);
        ((PlaceholderEquation) myView.selected).goDark();

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
