package colin.algebrator.eq;


import android.util.Log;

import colin.example.algebrator.Algebrator;
import colin.example.algebrator.SuperView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

public class MultiEquation extends FlexOperation implements MultiDivSuperEquation {

    public MultiEquation(SuperView owner, MultiEquation equations) {
        super(owner, equations);
        init();
    }

    @Override
    public Equation negate() {
        Equation result = this.copy();
        result.set(0,result.get(0).negate());
        return result;
    }

    @Override
    protected Equation plusMinus() {
        Equation result = this.copy();
        result.set(0,result.get(0).plusMinus());
        return result;
    }

    @Override
    public void integrityCheck() {
        super.integrityCheck();
        if (size() < 2) {
            Log.e("ic", "this should be at least size 2");
        }
    }

    @Override
    public Equation copy() {
        Equation result = new MultiEquation(this.owner,this);
        return result;
    }

    /**
     * returns true is the given equation could be "on top" if this MultiEquation was written as A/B
     *
     * @param equation - a child of this
     * @return true if the child would be on top false if the child would be on bottom or if the equation is not a child
     */
    @Override
    public boolean onTop(Equation equation) {
        boolean currentTop = true;
        Equation current = equation;
        String debug = current.hashCode() + ", ";
        while (true) {

            if (current.equals(this)) {
                return currentTop;
            }
            if (current.parent instanceof DivEquation) {
                currentTop = ((DivEquation) current.parent).onTop(current) == currentTop;
            }
            current = current.parent;
            if (current == null) {
                Log.i("bad", debug + " this is: " + this.hashCode());
            }
            debug += current.hashCode() + ",";
        }
    }

    public MultiEquation(SuperView owner) {
        super(owner);
        init();
    }

    private void init() {
        char[] timesUnicode = { '\u00D7'};
        display = new String(timesUnicode);
    }

    public void tryOperator(ArrayList<Equation> eqs) {
        //TODO handle inbeddedness
        String db ="";
        for (Equation e:eqs){
            db +=e.toString();
        }
        Log.i("",db);


        int at = Math.min(indexOf(eqs.get(0)), indexOf(eqs.get(1)));


        Equation result = null;

        operateRemove(eqs);
        // for the bottom and the top
            // find all the equations on each side


        MultiCountDatas left= new MultiCountDatas(eqs.get(0));

        MultiCountDatas right= new MultiCountDatas(eqs.get(1));
            // multiply && combine like terms
        MultiCountDatas fullSet = Operations.Multiply(left, right);

        if (fullSet.size() == 1) {
            MultiCountData mine =  fullSet.get(0);
            //mine.combine = true;
            result = mine.getEquation(owner);
        } else if (fullSet.size() > 1) {
            result = new AddEquation(owner);
            for (MultiCountData e : fullSet) {
                //e.combine = true;
                result.add(e.getEquation(owner));
            }
        }

        if (fullSet.neg){
            if (result instanceof MinusEquation){
                result = result.get(0);
            }else{
                result = result.negate();
            }
        }

        Equation noChangeResult = new MultiEquation(owner);
        noChangeResult.addAll(eqs);

        if (result.same(noChangeResult)){
            result = noChangeResult;
        }

        if (result instanceof MultiEquation){
            addAll(at,result);
        }else {
            add(at, result);
            if (this.size() == 1) {
                this.replace(this.get(0));
            }
        }
    }


    public boolean hasSign(int i) {
        Equation left = get(i);
        while (left instanceof MultiEquation){
            left = left.get(left.size()-1);
        }

//        if (left.parenthesis()){
//            return false;
//        }
        Equation right =get(i+1);// left.right();
        while (right instanceof MultiEquation){
            right = right.get(0);
        }
//        if (right.parenthesis()){
//            return false;
//        }
        if ((left instanceof NumConstEquation || left instanceof MonaryEquation) && (right instanceof NumConstEquation || right instanceof MonaryEquation)){
            return true;
        }
        if (right instanceof MonaryEquation){
            return true;
        }
        if (left instanceof PowerEquation && Operations.sortaNumber(left.get(0))){
            return true;
        }
        if (right instanceof PowerEquation && Operations.sortaNumber(right.get(0))){
            return true;
        }
        if (right instanceof PlusMinusEquation){
            return true;
        }

        return false;
    }
}

