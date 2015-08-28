package cube.d.n.commoncore.eq.Pro;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class ATanEquation extends TrigEquation<TanEquation> {

    public ATanEquation(EquationLine owner) {
        super(owner);
        display = "Arctan";
    }

    @Override
    protected boolean inDomain(double value) {
        return true;
    }

    protected ATanEquation(EquationLine owner, TrigEquation toCopy) {
        super(owner, toCopy);
    }

    @Override
    protected Equation protectedOperate(Equation equation) {

        double input = Operations.getValue(equation).doubleValue();

        double output = Math.atan(input);
        return NumConstEquation.create(output, owner);
    }

    @Override
    protected boolean isInverse(Equation equation) {
        return equation instanceof  TanEquation;
    }

    @Override
    public Equation copy() {
        return new ATanEquation(owner, this);
    }
}