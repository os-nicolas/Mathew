package cube.d.n.commoncore.Action.helper;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.Selects;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.InlineInputLine;

/**
 * Created by Colin_000 on 5/20/2015.
 */
public class HelperOkAction extends Action {
    public HelperOkAction(InlineInputLine line) {
        super(line);
    }

    public static Equation mine;

    @Override
    public boolean canAct(){
        // if stupid is legal
        // we need to copy stupid
        mine = owner.stupid.get().copy();

        // we want to remove the place holder
        if (((Selects)owner).getSelected() instanceof PlaceholderEquation) {
            Equation at = owner.stupid.get();
            Equation myAt = mine;
            while (!at.equals(((Selects) owner).getSelected())) {
                int index = at.deepIndexOf(((Selects)owner).getSelected());
                at = at.get(index);
                myAt = myAt.get(index);
            }
            if (myAt.parent != null && myAt.parent.size() != 1) {
                myAt.remove();
            } else {
                return false;
            }
        }

        // we need to follow the path to selected
        // and remove it from mine
        if (mine instanceof WritingEquation) {
            if (((WritingEquation) mine).deepLegal()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void privateAct() {

        ArrayList<Equation> converted = new ArrayList<>();
        converted.add(((WritingEquation)mine.copy()).convert());
        converted.add(((WritingEquation)mine.copy()).convert());

        Equation newStupid =  ((InlineInputLine)owner).makeModie(converted);

        AlgebraLine daLine = ((AlgebraLine)owner.owner.getLine(owner.owner.getLinesSize()-2));
        daLine.changed();
        newStupid.updateOwner(daLine);
        daLine.stupid.set(newStupid);

        daLine.updateHistory();

        owner.owner.revert();
    }
}
