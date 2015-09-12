package cube.d.n.commoncore.Action.helper;

import android.content.Intent;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Selects;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 5/20/2015.
 */
public class HelperEnterAction extends Action {
    public HelperEnterAction(InputLine line) {
        super(line);
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
        Main main = new  Main(owner.owner.getContext());
        // main come with an input line, we just need to give it an equation
       Equation lastLineEq =  owner.stupid.get().copy();

        Equation at = owner.stupid.get();
        Equation myAt = lastLineEq;
        while (!at.equals(((Selects)owner).getSelected())) {
            int index = at.deepIndexOf(((Selects)owner).getSelected());
            at = at.get(index);
            myAt = myAt.get(index);
        }
        myAt.replace(((InputLine)main.lastLine()).getSelected());

        ((InputLine)main.lastLine()).stupid.set(lastLineEq);
        ((InputLine)main.lastLine()).deActivate();

        Equation newEq = ((WritingEquation) mine).convert();

        AlgebraLine line = new AlgebraLine(main,newEq);
        newEq.updateOwner(line);

        main.addLine(line);

        BaseApp.getApp().stupidLittleBackDoor(main,  owner.owner.getContext());
    }
}
