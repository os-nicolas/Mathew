package colin.algebrator.eq;

import colin.example.algebrator.SuperView;

import java.util.ArrayList;

/**
 * Created by Colin on 1/4/2015.
 */
public class MinusEquation extends MonaryEquation {

    public MinusEquation(SuperView owner2) {
        super(owner2);
        init();
    }

    @Override
    public Equation negate() {
        return this.get(0).copy();
    }

    @Override
    protected Equation plusMinus() {
        return this.get(0).plusMinus();
    }

    private void init() {
        display ="-";
    }

    public MinusEquation(SuperView owner, MinusEquation minEq){
        super (owner,minEq);
        init();
    }

    @Override
    public Equation copy() {
        Equation result = new MinusEquation(this.owner,this);
        return result;
    }

    @Override
    public void tryOperator(ArrayList<Equation> eqs) {
        if (parent instanceof MinusEquation){
            parent.replace(get(0));
        }else
        if (get(0) instanceof MinusEquation){
            replace(get(0).get(0));
        }else if (get(0) instanceof PlusMinusEquation){
            replace(get(0));
        }else if (get(0) instanceof AddEquation){
            if (get(0).size()>1){
                for (Equation e: get(0)){
                    e.replace(e.negate());
                }
                if (this.parent instanceof AddEquation){
                    int at = parent.indexOf(this);
                    this.justRemove();
                    for (Equation e: get(0)){
                        parent.add(at,e);
                        at++;
                    }
                }else{
                    replace(get(0));
                }
            }
        }
    }
}
