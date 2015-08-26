package cube.d.n.commoncore.Action.TrigAction;

import cube.d.n.commoncore.eq.Pro.ATanEquation;
import cube.d.n.commoncore.eq.Pro.CosEquation;
import cube.d.n.commoncore.eq.Pro.TrigEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 8/25/2015.
 */
public class AtanAction  extends TrigAction {
    public AtanAction(InputLine owner) {
        super(owner);
    }

    @Override
    public TrigEquation getEquation() {
        return new ATanEquation(owner);
    }
}
