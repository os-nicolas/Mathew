package cube.d.n.commoncore.eq.any;

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
