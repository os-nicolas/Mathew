package cube.d.n.commoncore.eq.Pro;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.lines.Line;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class CosEquation extends TrigEquation<ACosEquation> {

    public CosEquation(Line owner) {
        super(owner);
    }

    protected CosEquation(Line owner, TrigEquation toCopy) {
        super(owner, toCopy);
    }

    @Override
    protected void protectedOperate(Equation equation) {

        double input = Operations.getValue(equation).doubleValue();

        double output = Math.cos(input);
        this.replace(NumConstEquation.create(output, owner));
    }

    @Override
    public Equation copy() {
        return new CosEquation(owner, this);
    }
}