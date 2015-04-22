package cube.d.n.commoncore.eq.Pro;

import cube.d.n.commoncore.eq.Equation;
import cube.d.n.commoncore.eq.NumConstEquation;
import cube.d.n.commoncore.eq.Operations;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public class SineEquation extends TrigEquation<ASineEquation> {

    public  SineEquation(SuperView owner) {
        super(owner);
    }

    protected   SineEquation(SuperView owner, TrigEquation toCopy) {
        super(owner,toCopy);
    }

    @Override
    protected void protectedOperate(Equation equation) {

        double input = Operations.getValue(equation).doubleValue();

        double output = Math.sin(input);
        this.replace(NumConstEquation.create(output,owner));


    }

    @Override
    public Equation copy() {
        return new SineEquation(owner,this);
    }


}
