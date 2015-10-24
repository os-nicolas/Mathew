package cube.d.n.commoncore.Matching;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.LeafEquation;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 10/24/2015.
 *
 */
public abstract class EquationMatcher extends LeafEquation {
    public EquationMatcher(EquationLine owner) {
        super(owner);
    }
}
