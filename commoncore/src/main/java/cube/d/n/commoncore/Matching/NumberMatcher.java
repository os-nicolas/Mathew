package cube.d.n.commoncore.Matching;

import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.LeafEquation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 10/24/2015.
 * A little class used for match that matches any num or -num
 */
public class NumberMatcher   extends EquationMatcher{

    public NumberMatcher(EquationLine owner) {
        super(owner);
    }

    @Override
    public Equation copy() {
        // all constantEquations are the same
        return new NumberMatcher(owner);
    }

    @Override
    public boolean same(Equation other){
        return other.removeNeg() instanceof NumConstEquation;
    }
}
