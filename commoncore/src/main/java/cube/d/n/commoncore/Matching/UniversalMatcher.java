package cube.d.n.commoncore.Matching;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 10/24/2015.
 */
public class UniversalMatcher extends EquationMatcher {

    public UniversalMatcher(EquationLine owner) {
        super(owner);
    }

    @Override
    public Equation copy() {
        // all constantEquations are the same
        return new UniversalMatcher(owner);
    }

    @Override
    public boolean same(Equation other){
        return true;
    }
}
