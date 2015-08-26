package cube.d.n.commoncore.Action.TrigAction;

import cube.d.n.commoncore.eq.Pro.ASineEquation;
import cube.d.n.commoncore.eq.Pro.ATanEquation;
import cube.d.n.commoncore.eq.Pro.TrigEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 8/25/2015.
 */
public class AsinAction   extends TrigAction {
    public AsinAction(InputLine owner) {
        super(owner);
    }

    @Override
    public TrigEquation getEquation() {
        return new ASineEquation(owner);
    }
}
