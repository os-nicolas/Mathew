package colin.algebrator.eq.Pro;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.NumConstEquation;
import colin.algebrator.eq.Operations;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class ASineEquation extends TrigEquation<SineEquation> {

    public  ASineEquation(SuperView owner) {
        super(owner);
    }

    protected   ASineEquation(SuperView owner, TrigEquation toCopy) {
        super(owner,toCopy);
    }

    @Override
    protected void protectedOperate(Equation equation) {

        double input = Operations.getValue(equation).doubleValue();

        double output = Math.asin(input);
        this.replace(NumConstEquation.create(output, owner));
    }

    @Override
    public Equation copy() {
        return new ASineEquation(owner,this);
    }


}

