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
    protected Equation negate() {
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

        myWidth = Algebrator.getAlgebrator().getDefaultSize();
        myHeight = Algebrator.getAlgebrator().getDefaultSize();
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
            mine.combine = true;
            result = mine.getEquation(owner);
        } else if (fullSet.size() > 1) {
            result = new AddEquation(owner);
            for (MultiCountData e : fullSet) {
                e.combine = true;
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


        add(at, result);
        if (this.size() == 1) {
            this.replace(this.get(0));
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
        return false;
    }
}

class MultiCountData {
    public ArrayList<Equation> key = new ArrayList<Equation>();
    public ArrayList<Equation> numbers = new ArrayList<Equation>();
    public MultiCountData under;
    public boolean plusMinus= false;
    public boolean combine = false;
    public boolean negative = false;

    public BigDecimal getValue(){
        BigDecimal sum = getNumbersValue();
        sum=(negative?sum.negate(): sum);
        return sum;
    }

    public BigDecimal getNumbersValue() {
        BigDecimal sum = BigDecimal.ONE;
        for (Equation number:numbers){
            boolean neg =false;
            while (number instanceof MinusEquation){
                number = number.get(0);
                neg = !neg;
            }
            sum = sum.multiply(((NumConstEquation)number).getValue());
            sum = (neg? sum.negate(): sum);
        }
        return sum;
    }

    public MultiCountData() {
    }

    public ArrayList<Equation> copyKey(){
        ArrayList<Equation> result = new ArrayList<Equation>();
        for (Equation e: key){
            result.add(e.copy());
        }
        return result;
    }

    public MultiCountData(MultiCountData mcd) {
        key = mcd.copyKey();
        this.numbers = mcd.copyNumbers();
        if (mcd.under != null) {
            this.under =new MultiCountData(mcd.under);
        }
        this.plusMinus= mcd.plusMinus;
        this.combine = mcd.combine;
        this.negative = mcd.negative;
    }

    public MultiCountData(Equation e) {
        update(e);
    }

    private void update(Equation e) {
        boolean startNeg = negative;
        if (e instanceof MinusEquation) {
            negative =! negative;
            e = e.get(0);
        }
        if (e instanceof PlusMinusEquation) {
            plusMinus = true;
            e = e.get(0);
        }
        if (e instanceof NumConstEquation) {
            // add back the - signs
            while (e.parent instanceof MinusEquation){
                e = e.parent;
            }
            negative = startNeg;
            numbers.add(e);
        } else if (e instanceof MultiEquation) {
            for (Equation ee : e) {
                update(ee);
            }
        } else if (e instanceof DivEquation) {
            update(e.get(0));
            if (under == null) {
                under = new MultiCountData(e.get(1));
            } else {
                under.update(e.get(1));
            }
        } else {
            key.add(e);
        }
    }

    public boolean matches(MultiCountData mcd) {
        if (mcd ==null){
            return false;
        }
        if (mcd.plusMinus != plusMinus){
            return false;
        }

        // if everything in this is the same as something in the other
        if (key.size() != mcd.key.size()) {
            return false;
        }
        for (Equation e : key) {
            boolean any = false;
            for (Equation ee : mcd.key) {
                if (ee.same(e)) {
                    any = true;
                    break;
                }
            }
            if (!any) {
                return false;
            }
        }



        // uncomment to match on numbers
//        if (numbers.size() != mcd.numbers.size()) {
//            return false;
//        }
//        for (Equation e : numbers) {
//            boolean any = false;
//            for (Equation ee : mcd.numbers) {
//                if (ee.same(e)) {
//                    any = true;
//                    break;
//                }
//            }
//            if (!any) {
//                return false;
//            }
//        }

        if (under != null) {
            return under.matches(mcd.under);
        }else if (mcd.under != null){
            return false;
        }
        return true;
    }

    public Equation getEquation(SuperView owner) {
        Equation top = null;
        Equation bot = null;
        Equation result = null;

        if (under != null){
            if (!(under.getValue().doubleValue() ==1 && under.key.size() ==0)) {
                bot = under.getEquation(owner);
            }
        }

        if (combine && getValue().doubleValue() == 0){
            top = new NumConstEquation(BigDecimal.ZERO,owner);
            if (under != null){
                if (!(under.getValue().doubleValue()==0.0)){
                    bot =null;
                }
            }
        }else if (key.size() == 0) {
            if (combine) {
                top = NumConstEquation.create(getValue(),owner);
            }else if (numbers.size() == 1 ){
                top = numbers.get(0);
            }else if (numbers.size() > 1){
                top = new MultiEquation(owner);
                for (Equation e:numbers){
                    top.add(e);
                }
            }else{
                top = new NumConstEquation(BigDecimal.ONE, owner);
            }
        } else {
            if (key.size() == 1 && getValue().abs().doubleValue()==1.0) {
                if (combine) {
                    if (getValue().doubleValue()==1) {
                        top = key.get(0);
                    } else {
                        top= key.get(0).negate();
                    }
                }else{
                    if (numbers.size() < 2){
                        top = key.get(0);
                    }else{
                        top = new MultiEquation(owner);
                        for (Equation e:numbers){
                            top.add(e);
                        }
                        top.add(key.get(0));
                    }
                }
            }else {
                top = new MultiEquation(owner);

                if (combine){
                    if (!(getValue().abs().doubleValue() == 1)) {
                        top.add(NumConstEquation.create(getValue(),owner));
                    }
                }else{
                    for (Equation e:numbers){
                        top.add(e);
                    }
                }
                ArrayList<EquationCounts> ecs = new ArrayList<EquationCounts>();
                for (Equation e :key) {
                    boolean match = false;
                    for (EquationCounts ec:ecs) {
                        if (ec.add(e)){
                            match = true;
                        }
                    }
                    if (!match){
                        ecs.add(new EquationCounts(e));
                    }
                }
                // add all the equation
                for (EquationCounts ec:ecs){
                    top.add(ec.getEquation());
                }
                if (top.size() ==1){
                    top = top.get(0);
                }

            }
        }

        if (bot!=null)
        {
            result = new DivEquation(owner);
            result.add(top);
            result.add(bot);
        }else{
            result =top;
        }

        if (negative && !plusMinus){
            result = result.negate();
        }

        if (plusMinus){
            result = result.plusMinus();
        }

        return result;
    }

    public ArrayList<Equation> copyNumbers() {
        ArrayList<Equation> result = new ArrayList<Equation>();
        for (Equation e : numbers) {
            result.add(e.copy());
        }
        return result;
    }

    // warning this is not a deep call it just checks if this hold any data
    public boolean isEmpty() {
        if (this.plusMinus!= false ||
                this.negative!= false ||
                !this.numbers.isEmpty() ||
                !this.key.isEmpty() ){
            return false;
        }
        return true;
    }

    public boolean addToKey(Equation copy) {


        if (copy instanceof PowerEquation) {
            for (Equation k : key) {
                if (k instanceof PowerEquation && k.get(1).same(copy.get(1))){
                    if (!(k.get(0) instanceof MultiEquation)){
                        Equation oldEq = k.get(0);
                        k.set(0,new MultiEquation(k.owner));
                        k.get(0).add(oldEq);
                    }
                    if (copy.get(0) instanceof MultiEquation){
                        for (Equation e: copy.get(0)){
                            k.get(0).add(e);
                        }
                    }else{
                        k.get(0).add(copy.get(0));
                    }
                    return true;
                }
            }
        }
        return key.add(copy);
    }

    public boolean notOne() {
        return key.size() != 0 || (numbers.size() != 0 && !(numbers.size() == 1 && getValue().doubleValue() == 1));
    }
}
