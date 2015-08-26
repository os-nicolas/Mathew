package cube.d.n.commoncore.Action.TrigAction;

import cube.d.n.commoncore.eq.Pro.ACosEquation;
import cube.d.n.commoncore.eq.Pro.ASineEquation;
import cube.d.n.commoncore.eq.Pro.TrigEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 8/25/2015.
 */
public class AcosAction   extends TrigAction {
    public AcosAction(InputLine owner) {
        super(owner);
    }

    @Override
    public TrigEquation getEquation() {
        return new ACosEquation(owner);
    }
}
