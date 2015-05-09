package cube.d.n.commoncore.Action.WriteScreen;


import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import cube.d.n.commoncore.v2.InputLine;

public class ParenthesesAction extends Action {

    private boolean left;

    public ParenthesesAction(InputLine emilyView, boolean left) {
        super(emilyView);
        this.left = left;
    }

    @Override
    public boolean canAct() {
        if (((InputLine)owner).getSelected() instanceof PlaceholderEquation) {


            boolean can = true;
            boolean op = false;
            Equation next = (left ? ((InputLine)owner).getSelected().right() : ((InputLine)owner).left());


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


        Equation newEq = new WritingPraEquation(left, ((InputLine)owner));
        ((InputLine)owner).getSelected().goDark();

        if (!(((InputLine)owner).getSelected().parent instanceof BinaryEquation)) {
            ((InputLine)owner).insert(newEq);
        } else {
            Equation oldEq = ((InputLine)owner).getSelected();
            Equation holder = new WritingEquation(((InputLine)owner));
            oldEq.replace(holder);
            holder.add(newEq);
            holder.add(oldEq);
            oldEq.setSelected(true);
        }
    }


}
