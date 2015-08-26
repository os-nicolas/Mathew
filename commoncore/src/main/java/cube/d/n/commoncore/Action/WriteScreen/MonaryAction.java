package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin on 2/22/2015.
 */
public abstract class MonaryAction extends Action {

    public MonaryAction(InputLine emilyView) {
        super(emilyView);
    }

    @Override
    public boolean canAct() {
        return ((InputLine)owner).getSelected() instanceof PlaceholderEquation;
    }

    protected void monaryInsert(Equation newEq) {
        ((InputLine)owner).getSelected().goDark();

        ((InputLine)owner).insert(newEq);

        updateOffset();
    }
}
