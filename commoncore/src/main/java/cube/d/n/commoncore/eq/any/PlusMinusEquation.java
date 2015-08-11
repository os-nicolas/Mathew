package cube.d.n.commoncore.eq.any;

import android.util.Log;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin on 1/28/2015.
 */
public class PlusMinusEquation extends MonaryEquation implements SignEquation {

    public static int plusMinusId = 0;
    public int myPlusMinusId;

    public PlusMinusEquation(EquationLine owner2) {
        super(owner2);
        myPlusMinusId = plusMinusId++;
        init();
    }

    public PlusMinusEquation(EquationLine owner, PlusMinusEquation equations) {
        super(owner, equations);
        myPlusMinusId = equations.myPlusMinusId;
        init();
    }

    @Override
    public void tryOperator(ArrayList<Equation> equation) {
        if (equation.size() == 1){
            Equation eq= equation.get(0);
            if (Operations.sortaNumber(eq)){
                replace(NumConstEquation.create(Operations.getValue(eq),owner));
            }
        }else {
            // it should really always be zero
            Log.e("MonaryEquation","should always have one element");

        }

    }

    @Override
    public Equation negate() {
        return copy();
    }

    @Override
    public Equation plusMinus() {
        return copy();
    }

    private void init() {
        display ="\u00B1";
    }

    @Override
    public Equation copy() {
        Equation result = new PlusMinusEquation(this.owner,this);

        return result;
    }
}
