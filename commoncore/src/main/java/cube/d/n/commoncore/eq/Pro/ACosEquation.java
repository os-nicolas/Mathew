package cube.d.n.commoncore.eq.Pro;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class ACosEquation extends TrigEquation<CosEquation> {

    public ACosEquation(EquationLine owner) {
        super(owner);
        display = "Arccos";
    }

    protected ACosEquation(EquationLine owner, TrigEquation toCopy) {
        super(owner, toCopy);
    }

    @Override
    protected boolean inDomain(double value) {
        return value <=1 && value >= -1;
    }

    @Override
    protected Equation protectedOperate(Equation equation) {

        double input = Operations.getValue(equation).doubleValue();

        double output = Math.acos(input);
        return NumConstEquation.create(output, owner);
    }

    @Override
    protected boolean isInverse(Equation equation) {
        return equation instanceof  CosEquation;
    }

    @Override
    public Equation copy() {
        return new ACosEquation(owner, this);
    }
}
