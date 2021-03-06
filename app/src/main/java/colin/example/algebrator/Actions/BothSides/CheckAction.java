package colin.example.algebrator.Actions.BothSides;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.BothSidesView;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class CheckAction extends Action<BothSidesView> {

    public static Equation mine;

    @Override
    public boolean canAct(){
        // if stupid is legal
        // we need to copy stupid
        mine = myView.getStupid().copy();

        // we want to remove the place holder
        if (myView.selected instanceof PlaceholderEquation) {
            Equation at = myView.getStupid();
            Equation myAt = mine;
            while (!at.equals(myView.selected)) {
                int index = at.deepIndexOf(myView.selected);
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
        Algebrator.getAlgebrator().solveView.changed();

        ArrayList<Equation> converted = new ArrayList<>();
        converted.add(((WritingEquation)mine.copy()).convert());
        converted.add(((WritingEquation)mine.copy()).convert());

        Equation newStupid =  myView.makeModie(converted);

        Algebrator.getAlgebrator().solveView.setStupid(newStupid);

        myView.myActivity.finish();
    }

    public CheckAction(BothSidesView myView) {
        super(myView);
    }
}
