package colin.algebrator.eq.Pro;

import java.util.ArrayList;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.MonaryEquation;
import colin.algebrator.eq.Operations;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin_000 on 3/30/2015.
 */
public abstract class TrigEquation<Inverse extends Equation> extends MonaryEquation {


    public TrigEquation(SuperView owner) {
        super(owner);
    }

    protected  TrigEquation(SuperView owner, TrigEquation toCopy) {
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
