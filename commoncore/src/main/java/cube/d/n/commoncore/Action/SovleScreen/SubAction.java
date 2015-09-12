package cube.d.n.commoncore.Action.SovleScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.SubLine;

/**
 * Created by Colin_000 on 9/11/2015.
 */
public class SubAction extends Action {


    private VarEquation toReplace;
    boolean hasActed = false;

    public SubAction(AlgebraLine line, VarEquation toReplace) {
            super(line);
         this.toReplace  = toReplace;
        }


        @Override
        protected void privateAct() {

            ((AlgebraLine)owner).setSelected(null);

            SubLine line = new SubLine(owner.owner);
            line.setUp(owner.stupid.get().copy(),(VarEquation)toReplace.copy());
            owner.owner.addLine(line);
            hasActed = true;
            ((AlgebraLine) owner).updatePopUpButtons();


        }

        @Override
        public boolean canAct() {
            return !hasActed;
        }
}
