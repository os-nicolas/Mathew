package cube.d.n.commoncore.eq.Pro;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class TanEquation  extends TrigEquation<ATanEquation> {

    public TanEquation(EquationLine owner) {
        super(owner);
        display = "Tan";
    }

    @Override
    protected boolean inDomain(double value) {
        return !Double.isNaN(Math.tan(value));
    }

    protected TanEquation(EquationLine owner, TrigEquation toCopy) {
        super(owner, toCopy);
    }

    @Override
    protected Equation protectedOperate(Equation equation) {

        double input = Operations.getValue(equation).doubleValue();

        double output = Math.tan(input);
        return NumConstEquation.create(output, owner);
    }

    @Override
    protected boolean isInverse(Equation equation) {
        return equation instanceof  ATanEquation;
    }

    @Override
    public ATanEquation emptyInverse() {
        return new ATanEquation(owner);
    }

    @Override
    public Equation copy() {
        return new TanEquation(owner, this);
    }
}