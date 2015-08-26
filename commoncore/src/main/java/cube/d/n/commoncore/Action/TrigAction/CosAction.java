package cube.d.n.commoncore.Action.TrigAction;

import cube.d.n.commoncore.eq.Pro.CosEquation;
import cube.d.n.commoncore.eq.Pro.SineEquation;
import cube.d.n.commoncore.eq.Pro.TrigEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 8/25/2015.
 */
public class CosAction extends TrigAction {
    public CosAction(InputLine owner) {
        super(owner);
    }

    @Override
    public TrigEquation getEquation() {
        return new CosEquation(owner);
    }
}
