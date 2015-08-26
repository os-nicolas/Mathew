package cube.d.n.commoncore.Action.WriteScreen;


import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.InputLine;

public class VarAction extends Action {


    public String var;

    public VarAction(InputLine emilyView, String var) {
        super(emilyView);
        this.var = var;
    }

    @Override
    public boolean canAct() {
        return true;
    }

    @Override
    protected void privateAct() {
        ((InputLine)owner).getSelected().goDark();

        Equation newEq = new VarEquation(var, owner);
        ((InputLine)owner).insert(newEq);

        updateOffset();
    }
}
