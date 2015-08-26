package cube.d.n.commoncore.Action.TrigAction;

import cube.d.n.commoncore.eq.Pro.SineEquation;
import cube.d.n.commoncore.eq.Pro.TanEquation;
import cube.d.n.commoncore.eq.Pro.TrigEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 8/25/2015.
 */
public class SineAction extends TrigAction {
    public SineAction(InputLine owner) {
        super(owner);
    }

    @Override
    public TrigEquation getEquation() {
        return new SineEquation(owner);
    }
}
