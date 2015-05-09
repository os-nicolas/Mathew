package cube.d.n.commoncore.eq.Pro;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MonaryEquation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.v2.Line;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public abstract class TrigEquation<Inverse extends Equation> extends MonaryEquation {


    public TrigEquation(Line owner) {
        super(owner);
    }

    protected  TrigEquation(Line owner, TrigEquation toCopy) {
        super(owner,toCopy);
    }

    public boolean canOperate(){
        try{
            Inverse test = ((Inverse)get(0));
            return true;
        }catch(ClassCastException e){
            // hush it up
        }
        return Operations.sortaNumber(get(0));
    }

    @Override
    public void tryOperator(ArrayList<Equation> equation) {
        if (canOperate()){
            protectedOperate(equation.get(0));
        }
    }

    protected abstract void protectedOperate(Equation equation);
}
