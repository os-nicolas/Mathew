package cube.d.n.commoncore.Action.SovleScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.BothSidesLine;

/**
 * Created by Colin_000 on 3/28/2015.
 */
public class BothSides extends Action {


    private final BothSidesMode myBothSidesMode;

    public BothSides(AlgebraLine line,BothSidesMode myBothSidesMode) {
        super(line);
        this.myBothSidesMode = myBothSidesMode;
    }


    @Override
    protected void privateAct() {


        BothSidesLine line = new BothSidesLine(owner.owner);
        line.setUp(myBothSidesMode,owner.stupid.get().copy());
        owner.owner.addLine(line);


    }

    @Override
    public boolean canAct() {
        return true;
    }
}
