package cube.d.n.commoncore.eq.Pro;

import cube.d.n.commoncore.BaseView;
import cube.d.n.commoncore.eq.Equation;
import cube.d.n.commoncore.eq.NumConstEquation;
import cube.d.n.commoncore.eq.Operations;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class ATanEquation extends TrigEquation<TanEquation> {

    public ATanEquation(BaseView owner) {
        super(owner);
    }

    protected ATanEquation(BaseView owner, TrigEquation toCopy) {
        super(owner, toCopy);
    }

    @Override
    protected void protectedOperate(Equation equation) {

        double input = Operations.getValue(equation).doubleValue();

        double output = Math.atan(input);
        this.replace(NumConstEquation.create(output, owner));
    }

    @Override
    public Equation copy() {
        return new ATanEquation(owner, this);
    }
}