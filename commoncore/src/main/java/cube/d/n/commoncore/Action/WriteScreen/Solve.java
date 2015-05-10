package cube.d.n.commoncore.Action.WriteScreen;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.v2.Selects;
import cube.d.n.commoncore.v2.lines.AlgebraLine;
import cube.d.n.commoncore.v2.lines.InputLine;


public class Solve extends Action {

    public Solve(InputLine emilyView) {
        super(emilyView);
    }

    public static Equation mine;

    @Override
    public boolean canAct() {
        // we need to copy stupid
        mine = owner.stupid.get().copy();

        // we want to remove the place holder
            Equation at = owner.stupid.get();
            Equation myAt = mine;
            while (!at.equals(((Selects)owner).getSelected())) {
                int index = at.deepIndexOf(((Selects)owner).getSelected());
                at = at.get(index);
                myAt = myAt.get(index);
            }
            if (myAt.parent != null && myAt.parent.size() != 1) {
                myAt.remove();
            } else {
                return false;
            }

        // we need to follow the path to selected
        // and remove it from mine
        if (mine instanceof WritingEquation) {

            if (((WritingEquation) mine).deepLegal() ) {//&& countEquals(mine) == 1
                return true;
            }
        }
        return false;
    }

    @Override
    protected void privateAct() {


        Equation newEq = ((WritingEquation) Solve.mine).convert();
        ((InputLine)owner).deActivate();
        AlgebraLine line = new AlgebraLine(owner.owner,newEq);
        newEq.updateOwner(line);
        owner.owner.addLine(line);

    }
}