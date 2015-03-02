package colin.algebrator.eq;

import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 1/28/2015.
 */
public class PlusMinusEquation extends MonaryEquation {

    public PlusMinusEquation(SuperView owner2) {
        super(owner2);
        init();
    }

    public PlusMinusEquation(SuperView owner, PlusMinusEquation equations) {
        super(owner, equations);
        init();
    }

    @Override
    protected Equation negate() {
        return copy();
    }

    @Override
    protected Equation plusMinus() {
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
