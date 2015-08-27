package cube.d.n.commoncore.eq.Pro;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class SineEquation extends TrigEquation<ASineEquation> {

    public  SineEquation(EquationLine owner) {
        super(owner);
        display = "Sin";
    }

    protected   SineEquation(EquationLine owner, TrigEquation toCopy) {
        super(owner,toCopy);
    }

    @Override
    protected Equation protectedOperate(Equation equation) {

        double input = Operations.getValue(equation).doubleValue();

        double output = Math.sin(input);
        return NumConstEquation.create(output,owner);
    }

    @Override
    protected boolean isInverse(Equation equation) {
        return equation instanceof  ASineEquation;
    }

    @Override
    public Equation copy() {
        return new SineEquation(owner,this);
    }


}
