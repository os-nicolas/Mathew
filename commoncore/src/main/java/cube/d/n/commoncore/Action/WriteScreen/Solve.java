package cube.d.n.commoncore.Action.WriteScreen;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.Selects;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.Line;
import cube.d.n.commoncore.lines.OutputLine;


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
        ArrayList<String> vars = getVars(newEq);
        ((InputLine)owner).deActivate();
        Line line;
        if (vars.size() != 0 ||  countEquals(((WritingEquation) Solve.mine))==1){
            line = new AlgebraLine(owner.owner,newEq);

        }else{
            line = new OutputLine(owner.owner,newEq);
        }
        newEq.updateOwner(line);
        owner.owner.addLine(line);
    }

    private ArrayList<String> getVars(Equation stupid) {
        ArrayList<String> result = new ArrayList<>();
        if (stupid instanceof VarEquation){
            result.add(stupid.getDisplay(-1));
        }
        for (Equation e: stupid){
            ArrayList<String> innerResult = getVars(e);
            for (String s: innerResult){
                if (!result.contains(s)){
                    result.add(s);
                }
            }
        }
        return result;
    }
}