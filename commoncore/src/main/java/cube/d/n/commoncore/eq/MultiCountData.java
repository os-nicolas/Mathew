package cube.d.n.commoncore.eq;

import java.math.BigDecimal;
import java.util.ArrayList;


import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MinusEquation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.PlusMinusEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 3/25/2015.
 */
public class MultiCountData {
    public ArrayList<Equation> key = new ArrayList<Equation>();
    public ArrayList<Equation> numbers = new ArrayList<Equation>();
    public MultiCountData under;
    public boolean plusMinus= false;
    public int myPlusMinusId=-1;
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
        this.myPlusMinusId = mcd.myPlusMinusId;
        this.combine = mcd.combine;
        this.negative = mcd.negative;
    }

    public MultiCountData(Equation e) {
        update(e.copy());
    }

    private void update(Equation e) {
        boolean startNeg = negative;
        if (e instanceof MinusEquation) {
            negative =! negative;
            e = e.get(0);
        }
        if (e instanceof PlusMinusEquation) {
            plusMinus = true;
            myPlusMinusId = ((PlusMinusEquation) e).myPlusMinusId;
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

    @Override
    public String toString(){
        String nums = "numbers: ";
        for (Equation e:this.numbers){
            nums +=e.toString() +",";
        }
        String ks = "key: ";
        for (Equation e:this.key){
            ks +=e.toString() +",";
        }
        return nums + " " + ks;
    }

    public Equation getEquation(EquationLine owner) {
        Equation top = null;
        Equation bot = null;
        Equation result = null;

        if (under != null){
            if (!(under.getNumbersValue().doubleValue() ==1 && under.key.size() ==0)) {
                bot = under.getEquation(owner);
            }
        }

        if (getNumbersValue().doubleValue() == 0){
            top = NumConstEquation.create(BigDecimal.ZERO,owner);
            if (under != null){
                if (!(under.getNumbersValue().doubleValue()==0.0)){
                    bot =null;
                }
            }
        }else if (key.size() == 0) {
            if (combine) {
                top = NumConstEquation.create(getNumbersValue(),owner);
            }else if (numbers.size() == 1 ){
                top = numbers.get(0);
            }else if (numbers.size() > 1){
                top = new MultiEquation(owner);
                for (Equation e:numbers){
                    top.add(e);
                }
            }else{
                top = NumConstEquation.create(BigDecimal.ONE, owner);
            }
        } else {
            if (key.size() == 1 && getNumbersValue().abs().doubleValue()==1.0) {
                if (combine) {
                    // we just use getNumbersValue since negetive get included at the very end
                    if (getNumbersValue().doubleValue()==1) {
                        top = key.get(0);
                    } else {
                        top= key.get(0).negate();
                    }
                }else{
                    // we just use getNumbersValue since negetive get included at the very end
                    if (numbers.size() < 2){
                        if (getNumbersValue().doubleValue() ==-1){
                            top = key.get(0).negate();
                        }else{
                            top = key.get(0);
                        }
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
                    if (!(getNumbersValue().abs().doubleValue() == 1)) {
                        top.add(NumConstEquation.create(getNumbersValue(),owner));
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

                if (!combine) {
                    for (Equation e : numbers) {
                        boolean match = false;
                        for (EquationCounts ec : ecs) {
                            if (ec.add(e)) {
                                match = true;
                            }
                        }
                        if (!match) {
                            top.add(e);
                        }
                    }
                }

                // add all the equation
                for (EquationCounts ec:ecs){
                    top.add(ec.getEquation());
                }
                if (top.size() ==1){
                    top = top.get(0);
                    top.parent = null;
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
                if (k instanceof PowerEquation && k.get(1).same(copy.get(1)) && !(k.get(0).same(copy.get(0)))){
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

    public boolean sortaNumber() {
        return under == null && key.size() ==0;
    }
}
