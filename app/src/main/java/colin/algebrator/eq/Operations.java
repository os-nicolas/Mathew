package colin.algebrator.eq;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 1/15/2015.
 */
public class Operations {

    // **************************** MULTIPLY *****************************************

    public static MultiCountDatas Multiply(MultiCountDatas left, MultiCountDatas right) {
        SuperView owner = Algebrator.getAlgebrator().solveView;

        MultiCountDatas result = new MultiCountDatas();
        for (MultiCountData a : right) {
            for (MultiCountData b : left) {
                MultiCountData toAdd = Multiply(a, b);
                boolean found = false;
                for (MultiCountData mcd : result) {
                    if (mcd.matches(toAdd)) {
                        found = true;
                        //this is bad:
                        BigDecimal value = mcd.getValue().add(toAdd.getValue());
                        mcd.numbers = new ArrayList<Equation>();
                        Equation newValue = NumConstEquation.create(value, owner);
                        mcd.numbers.add(newValue);
                        break;
                    }
                }
                if (!found) {
                    result.add(toAdd);
                }
            }
        }
        result.neg = (left.neg != right.neg);

        return result;
    }

    private static MultiCountData Multiply(MultiCountData a, MultiCountData b) {
        MultiCountData result = new MultiCountData();

        for (MultiCountData e : new MultiCountData[]{a, b}) {
            multiplyHelper(e, result);
        }
        return result;
    }

    private static void multiplyHelper(MultiCountData newMcd, MultiCountData result) {
        SuperView owner = Algebrator.getAlgebrator().solveView;

        MultiCountData at = newMcd;
        MultiCountData target = result;
        while (at != null) {
            if (target.plusMinus && at.plusMinus && target.myPlusMinusId == at.myPlusMinusId){
                target.plusMinus = false;
            }else if (at.plusMinus){
                target.plusMinus = true;
                target.myPlusMinusId = at.myPlusMinusId;
            }
            if (at.negative) {
                target.negative = !target.negative;
            }
            for (Equation e : at.key) {
                target.addToKey(e.copy());
            }
            // if both side have one number let's do it
            if (target.numbers.size() < 2 && at.numbers.size() < 2) {
                BigDecimal value = target.getNumbersValue().multiply(at.getNumbersValue());
                target.numbers = new ArrayList<>();
                if (value.doubleValue() != 1) {
                    target.numbers.add(NumConstEquation.create(value, owner));
                }
            } else {
                for (Equation e : at.numbers) {
                    target.numbers.add(e.copy());
                }
            }
            if (target.under == null) {
                if (at.under != null) {
                    target.under = new MultiCountData(at.under);
                }
                at = null;
            } else {
                at = at.under;
                target = target.under;
            }
        }
    }

    public static void findEquation(Equation e, ArrayList<MultiCountData> set) {
        if (e instanceof AddEquation) {
            for (Equation ee : e) {
                findEquation(ee, set);
            }
        } else {
            set.add(new MultiCountData(e.copy()));
        }
    }

    // **************************** ADD *****************************************

    public static Equation Add(MultiCountData left, MultiCountData right) {
        //
        MultiCountData under = null;
        if (left.under != null && right.under == null) {
            under = left.under;
            right = Multiply(right, under);
            left.under = null;
        } else if (left.under == null && right.under != null) {
            under = right.under;
            left = Multiply(left, under);
            right.under = null;
        } else if (left.under != null && right.under != null) {
            MultiCountData common = deepFindCommon(left.under, right.under);
            MultiCountData leftRem = remainder(left.under, common);
            MultiCountData rightRem = remainder(right.under, common);

            under = Multiply(right.under, leftRem);
            left.under = null;
            right.under = null;

            right = Multiply(right, leftRem);

            left = Multiply(left, rightRem);

        }

        //TODO get owner a different way?
        SuperView owner = Algebrator.getAlgebrator().solveView;

        //if under == null we actully add
        if (under == null && !(left.key.isEmpty() &&
                right.key.isEmpty() &&
                (right.numbers.size() > 1 || left.numbers.size() > 1))) {


            MultiCountData common = findCommon(left, right);
            if (!common.key.isEmpty() || !common.numbers.isEmpty()) {
                left = remainder(left, common);
                right = remainder(right, common);
            }
            Equation result = addHelper(left, right);
            // and multiply the result time common if there is any common
            if (result instanceof NumConstEquation && ((NumConstEquation) result).getValue().doubleValue() == 0) {
                return result;
            } else if (common.notOne()) {
                // handle 5A + 5A in this case we get
                // result = 2, commmon = 5,A
                // we want to clear out result and
                if (sortaNumber(result) && common.numbers.size() == 1) {
                    BigDecimal number = getValue(result).multiply(common.getValue());
                    number = (common.negative ? number.negate() : number);
                    common.numbers = new ArrayList<>();
                    common.numbers.add(NumConstEquation.create(number, owner));
                    return common.getEquation(owner);
                } else
                    // handle -A -A
                    if (common.numbers.size() == 1 && common.getValue().doubleValue() == -1) {
                        return result.negate();
                    } else
                        // handle 2A - A
                        if (sortaNumber(result) && getValue(result).doubleValue() == 1) {
                            return common.getEquation(owner);
                        } else {
                            // handle AB + AC
                            Equation holder = new MultiEquation(owner);
                            holder.add(result);
                            holder.add(common.getEquation(owner));
                            return holder;
                        }
            } else {
                return result;
            }
        }
        //otherwise we are done
        else {
            //return (left + right ) /under
            Equation lEq = left.getEquation(owner);
            Equation rEq = right.getEquation(owner);
            Equation top = new AddEquation(owner);
            for (Equation eq : new Equation[]{lEq, rEq}) {
                if (eq instanceof AddEquation) {
                    for (Equation e : eq) {
                        top.add(e);
                    }
                } else {
                    top.add(eq);
                }
            }
            if (under != null) {
                Log.d("bot", "combine:  " + under.combine + " numbers.size: " + under.numbers.size() + "under.getValue" + under.getValue().doubleValue() + " neg: " + under.negative);
                Equation bot = under.getEquation(owner);
                Equation result = new DivEquation(owner);
                result.add(top);
                result.add(bot);
                return result;
            }else{
                return top;
            }
        }
    }

    private static Equation addHelper(MultiCountData left, MultiCountData right) {
        SuperView owner = Algebrator.getAlgebrator().solveView;
        // if they are both just numbers make a NumConst
        if (left.key.size() == 0 && right.key.size() == 0 && right.numbers.size() <= 1 && left.numbers.size() <= 1 && !left.plusMinus && !right.plusMinus) {
            BigDecimal sum = right.getValue().add(left.getValue());
            return NumConstEquation.create(sum, owner);
        }
        // otherwise make an add equation and throw them both in it
        if (left.key.isEmpty() && left.numbers.size() == 1 && left.getValue().doubleValue() == 0
                && right.key.isEmpty() && right.numbers.size() == 1 && right.getValue().doubleValue() == 0) {
            return new NumConstEquation(0, owner);
        } else if (left.key.isEmpty() && left.numbers.size() == 1 && left.getValue().doubleValue() == 0) {
            return right.getEquation(owner);
        } else if (right.key.isEmpty() && right.numbers.size() == 1 && right.getValue().doubleValue() == 0) {
            return left.getEquation(owner);
        } else {
            Equation result = new AddEquation(owner);
            result.add(left.getEquation(owner));
            result.add(right.getEquation(owner));
            return result;
        }
    }


    public static MultiCountData remainder(MultiCountData left, MultiCountData common) {
        MultiCountData result = new MultiCountData();

        ArrayList<EquationCounts> leftCopy = new ArrayList<EquationCounts>();
        for (Equation e : left.copyKey()) {
            leftCopy.add(new EquationCounts(e));
        }
        ArrayList<EquationCounts> commonCopy = new ArrayList<EquationCounts>();
        for (Equation e : common.copyKey()) {
            commonCopy.add(new EquationCounts(e));
        }
//        for (EquationCounts lft : leftCopy) {
//            boolean match = false;
//            for (EquationCounts com : commonCopy) {
//
//
//                EquationCounts removed = removeCommon(lft, com);
//                if (!lft.equals(removed)) {
//                    match = true;
//                    Equation newEq = removed.getEquation();
//                    if (!(newEq instanceof NumConstEquation && ((NumConstEquation) newEq).getValue().doubleValue() == 1)) {
//                        result.addToKey(newEq);
//                    }
//                    commonCopy.remove(com);
//                    break;
//                }
//            }
//            if (!match) {
//                result.addToKey(lft.getEquation());
//            }
//        }
        for (EquationCounts e : leftCopy) {
            for (EquationCounts ee : commonCopy) {
                EquationCounts commonE = findCommon(e, ee);
                if (commonE != null) {
                    Equation newKey = commonE.getEquation();
                    if (!(sortaNumber(newKey) && getValue(newKey).doubleValue() == 1.0)) {

                        int index =commonCopy.indexOf(ee);

                        ee = ee.remainder(new EquationCounts(newKey));
                        commonCopy.set(index, ee);

                        index =leftCopy.indexOf(e);
                        e =e.remainder(new EquationCounts(newKey));
                        leftCopy.set(index, e);
                        // i want this break to break out of both ifs and the for
                        // does it?
                        if (e.isEmpty()) {
                            break;
                        }
                    }
                }
            }
        }

        Equation oneEq = NumConstEquation.create(1,Algebrator.getAlgebrator().solveView);
        for (EquationCounts ec:leftCopy){
            if (!ec.isEmpty()) {
                if (!ec.getEquation().same(oneEq)) {
                    result.addToKey(ec.getEquation());
                }
            }
        }

        ArrayList<Equation> leftCopyNum = left.copyNumbers();

        ArrayList<Equation> commonCopyNum = common.copyNumbers();

        boolean toFlip = false;

        // cancel any common numbers too
        for (int i = 0; i < leftCopyNum.size(); i++) {
            Equation e = leftCopyNum.get(i);
            boolean match = false;
            for (int ii = 0; ii < commonCopyNum.size(); ii++) {
                Equation ee = commonCopyNum.get(ii);
                if (e.removeNeg().same(ee.removeNeg())) {
                    if (e.isNeg() != ee.isNeg()){
                        toFlip = ! toFlip;
                    }
                    match = true;
                    leftCopyNum.remove(i);
                    commonCopyNum.remove(ii);
                    i--;
                    break;
                } else {
                    int eInt = (int) Math.floor(getValue(e).doubleValue());
                    int eeInt = (int) Math.floor(getValue(ee).doubleValue());
                    int myGcd = gcd(eInt, eeInt);
                    if (getValue(e).doubleValue() == eInt
                            && getValue(ee).doubleValue() == eeInt
                            && Math.abs(myGcd) != 1) {
                        match = true;
                        if (eInt == myGcd) {
                            leftCopyNum.remove(i);
                        } else {
                            leftCopyNum.set(i, NumConstEquation.create(new BigDecimal(eInt / myGcd), e.owner));
                        }
                        if (eeInt == myGcd) {
                            commonCopyNum.remove(ii);
                        } else {
                            commonCopyNum.set(ii, NumConstEquation.create(new BigDecimal(eeInt / myGcd), ee.owner));
                        }
                        i--;
                        break;
                    }

                }
            }
            if (!match) {
                result.numbers.add(e);
            }
        }


        result.negative = left.negative != common.negative;
        if (toFlip){
            result.negative = ! result.negative;
        }

        if (result.plusMinus && left.plusMinus && left.myPlusMinusId == result.myPlusMinusId) {
            result.plusMinus = false;
            result.myPlusMinusId = -1;
        }else {
            result.plusMinus = left.plusMinus;
            result.myPlusMinusId = left.myPlusMinusId;
        }

        if (common.under == null && left.under != null) {
            result.under = new MultiCountData(left.under);
        } else if (left.under != null) {
            result.under = remainder(left.under, common.under);
        }
        // what do we do with unders?
        // nothing for now

        return result;
    }

    private static MultiCountData deepFindCommon(MultiCountData left, MultiCountData right) {
        MultiCountData leftAt = left;
        MultiCountData rightAt = right;
        MultiCountData result = findCommon(leftAt, rightAt);
        MultiCountData resultAt = result;
        while (true) {
            if (leftAt.under == null && rightAt.under == null) {
                break;
            }
            if (leftAt.under == null) {
                leftAt.under = new MultiCountData();
            }
            if (rightAt.under == null) {
                rightAt.under = new MultiCountData();
            }
            resultAt.under = findCommon(leftAt.under, rightAt.under);
            resultAt = resultAt.under;
            leftAt = leftAt.under;
            rightAt = rightAt.under;
        }
        return result;
    }

    private static MultiCountData findCommon(MultiCountData left, MultiCountData right) {
        MultiCountData result = new MultiCountData();
//        HashSet<Equation> leftCopy = left.copyKey();
//        HashSet<Equation> rightCopy = right.copyKey();

        // convert everything to Equation Counts
        ArrayList<EquationCounts> leftCopy = new ArrayList<EquationCounts>();
        for (Equation e : left.copyKey()) {
            leftCopy.add(new EquationCounts(e));
        }
        ArrayList<EquationCounts> rightCopy = new ArrayList<EquationCounts>();
        for (Equation e : right.copyKey()) {
            rightCopy.add(new EquationCounts(e));
        }

        for (EquationCounts e : leftCopy) {
            for (EquationCounts ee : rightCopy) {
                EquationCounts common = findCommon(e, ee);
                if (common != null) {
                    Equation newKey = common.getEquation();
                    if (!(sortaNumber(newKey) && getValue(newKey).doubleValue() == 1.0)) {
                        result.addToKey(newKey);
                        int index =rightCopy.indexOf(ee);

                        ee = ee.remainder(new EquationCounts(newKey));
                        rightCopy.set(index, ee);

                        index =leftCopy.indexOf(e);
                        e =e.remainder(new EquationCounts(newKey));
                        leftCopy.set(index, e);
                        // i want this break to break out of both ifs and the for
                        // does it?
                        if (e.isEmpty()) {
                            break;
                        }
                    }
                }
            }
        }

        ArrayList<Equation> leftNumCopy = left.copyNumbers();
        ArrayList<Equation> rightNumCopy = right.copyNumbers();


        for (int i = 0; i < leftNumCopy.size(); i++) {
            Equation e = leftNumCopy.get(i);
            for (int ii = 0; ii < rightNumCopy.size(); ii++) {
                Equation ee = rightNumCopy.get(ii);
                if (e.removeNeg().same(ee.removeNeg()) && getValue(e).doubleValue() != 0) {
                    boolean neg = e.isNeg() && ee.isNeg();
                    result.numbers.add((!neg?e.removeNeg():e));
                    leftNumCopy.remove(i);
                    rightNumCopy.remove(ii);
                    i--;
                    break;
                } else {
                    int eInt = (int) Math.floor(getValue(e).doubleValue());
                    int eeInt = (int) Math.floor(getValue(ee).doubleValue());
                    int myGcd = gcd(eInt, eeInt);
                    if (getValue(e).doubleValue() == eInt
                            && 0 != eInt
                            && 0 != eeInt
                            && getValue(ee).doubleValue() == eeInt
                            && Math.abs(myGcd) != 1 && myGcd!=0 ) {
                        result.numbers.add(NumConstEquation.create(new BigDecimal(myGcd), e.owner));
                        if (eInt == myGcd) {
                            leftNumCopy.remove(i);
                            i--;
                        } else {
                            leftNumCopy.set(i, NumConstEquation.create(new BigDecimal(eInt / myGcd), e.owner));
                        }
                        if (eeInt == myGcd) {
                            rightNumCopy.remove(ii);
                        } else {
                            rightNumCopy.set(ii, NumConstEquation.create(new BigDecimal(eeInt / myGcd), ee.owner));
                        }
                        break;
                    }
                }
            }
        }
        if (left.negative && right.negative) {
            result.negative = true;
        }
        // find what is common in the values
        // only if both values are ints
        //if ((left.value == Math.floor(left.value)) && (right.value == Math.floor(right.value))) {
        //    result.value = Double.valueOf(gcd((int) (Math.abs(Math.floor(left.value))), (int) Math.abs(Math.floor(right.value))));
        //}
        Log.d("found Common:", result.toString());

        result.combine = true;

        return result;
    }

    private static EquationCounts findCommon(EquationCounts left, EquationCounts right) {
        if (left.root.same(right.root)) {
            EquationCounts result = new EquationCounts();
            result.root = left.root;
            // find what is common with v
            if (Math.signum(left.v.doubleValue()) == Math.signum(right.v.doubleValue())) {
                result.v = new BigDecimal(Math.min(Math.abs(left.v.doubleValue()), Math.abs(right.v.doubleValue())) * Math.signum(left.v.doubleValue()));
            }
            // now what keys do they share up top side
            for (Equation keyl : left.equations.keySet()) {
                for (Equation keyr : right.equations.keySet()) {
                    if (keyl.same(keyr)) {
                        float vl = left.equations.get(keyl);
                        float vr = right.equations.get(keyr);
                        if (Math.signum(vl) == Math.signum(vr)) {
                            float myV = Math.min(Math.abs(vl), Math.abs(vr)) * Math.signum(vl);
                            result.equations.put(keyl, myV);
                        }
                    }
                }
            }

            return result;
        } else {
            return null;
        }
    }

    private static EquationCounts removeCommon(EquationCounts old, EquationCounts remove) {
        if (old.root.same(remove.root)) {
            EquationCounts result = new EquationCounts();
            result.root = old.root;
            // find what is common with v
            if (Math.signum(old.v.doubleValue()) == Math.signum(old.v.doubleValue())) {
                result.v = new BigDecimal(old.v.doubleValue() - remove.v.doubleValue());
            }
            // now what do they share up top side
            for (Equation keyl : old.equations.keySet()) {
                boolean foundMatch = false;

                for (Equation keyr : remove.equations.keySet()) {
                    if (keyl.same(keyr)) {
                        foundMatch = true;
                        float vl = old.equations.get(keyl);
                        float vr = remove.equations.get(keyr);
                        result.equations.put(keyl, vl - vr);
                    }
                }
                if (!foundMatch) {
                    result.equations.put(keyl, old.equations.get(keyl));
                }
            }
            return result;
        } else {
            return old;
        }
    }


    static int gcd(int a, int b) {
        while (a != 0 && b != 0) // until either one of them is 0
        {
            int c = b;
            b = a % b;
            a = c;
        }
        return a + b; // either one is 0, so return the non-zero value
    }

    // **************************** DIVIDE ****************************

    public static Equation divide(Equation a, Equation b) {
        SuperView owner = Algebrator.getAlgebrator().solveView;
        Equation result = null;

        // we check to see if they are both power equations of the same power
        // or close to that (just a negetive sign away
        boolean samePower = (a.removeNeg() instanceof PowerEquation &&
                b.removeNeg() instanceof PowerEquation &&
                a.removeNeg().get(1).same(b.removeNeg().get(1)));

        if (samePower) {
            boolean neg = a.isNeg() != b.isNeg();
            boolean plusMinus = a.isPlusMinus() != b.isPlusMinus();
            Equation div = new DivEquation(owner);
            div.add(a.removeNeg().get(0).copy());
            div.add(b.removeNeg().get(0).copy());
            Equation power = new PowerEquation(owner);
            power.add(div);
            power.add(a.removeNeg().get(1).copy());

            if (plusMinus) {
                result = result.plusMinus();
            } else if (neg) {
                result = power.negate();
            } else {
                result = power;
            }

        } else if (a instanceof AddEquation) {
            result = new AddEquation(owner);
            for (Equation e : a) {
                Equation newEq = new DivEquation(owner);
                newEq.add(e);
                newEq.add(b.copy());
                result.add(newEq);
            }
        } else {
            // figure out what is common
            MultiCountData top = new MultiCountData(a);
            MultiCountData bot = new MultiCountData(b);

            MultiCountData common = deepFindCommon(top, bot);

            MultiCountData commonAt = common;

            boolean anyCommon = false;
            while (!anyCommon && commonAt != null) {
                if (!commonAt.isEmpty()) {
                    anyCommon = true;
                }
                commonAt = commonAt.under;
            }

            if (anyCommon) { // they have anything in common

                ((ColinView)owner).tryWarn(common.getEquation(owner));

                MultiCountData topData = remainder(top, common);
                MultiCountData botData = remainder(bot, common);
                Equation topEq = topData.getEquation(owner);
                Equation botEq = botData.getEquation(owner);
                result = getResult(topEq, botEq);
                //old code
//            MultiCountData top = new MultiCountData(a);
//            MultiCountData bot = new MultiCountData(b);
//
//            MultiCountData atTop = new MultiCountData(a);
//            MultiCountData atBot = new MultiCountData(b);
//            MultiCountData common = findCommon(atTop, atBot);
//            int depth = 0;
//            while ((atBot.under != null && atTop.under != null) && (common.numbers.isEmpty() && common.key.isEmpty())) {
//                atTop = atTop.under;
//                atBot = atBot.under;
//                common = findCommon(atTop, atBot);
//                depth++;
//            }
//            if (!common.numbers.isEmpty() || !common.key.isEmpty()) {
//                atTop = remainder(atTop, common);
//                atBot = remainder(atBot, common);
//
//                // now we need to put atTop and atBot back
//                MultiCountData parentTop = top;
//                MultiCountData parentBot = bot;
//
//                if (depth > 0) {
//                    while (depth > 1) {
//                        parentTop = parentTop.under;
//                        parentBot = parentBot.under;
//                    }
//                    parentTop.under = atTop;
//                    parentBot.under = atBot;
//                } else {
//                    top = atTop;
//                    bot = atBot;
//                }
//
//                if (bot.plusMinus) {
//                    bot.plusMinus = false;
//                    top.plusMinus = true;
//                }
//
//                Equation topEq = top.getEquation(owner);
//                Equation botEq = bot.getEquation(owner);
//                result = getResult(topEq, botEq);

                // if we have a/b where a and b are sortaNumbers
            } else if (bot.numbers.size() == 1 && top.numbers.size() == 1 && bot.under == null && top.under == null && !(bot.getValue().doubleValue() == 0)) {
                int topInt = (int) Math.floor(top.getValue().doubleValue());
                int botInt = (int) Math.floor(bot.getValue().doubleValue());
                int myGcd = gcd(topInt, botInt);

                // if it can be reduced
                if (bot.getValue().doubleValue() == botInt
                        && top.getValue().doubleValue() == topInt
                        && Math.abs(myGcd) != 1) {

                    bot.numbers = new ArrayList<Equation>();
                    bot.numbers.add(NumConstEquation.create(new BigDecimal(botInt / myGcd), owner));
                    top.numbers = new ArrayList<Equation>();
                    top.numbers.add(NumConstEquation.create(new BigDecimal(topInt / myGcd), owner));


                } else {
                    BigDecimal value = top.getValue().divide(bot.getValue(), 20, RoundingMode.HALF_UP);
                    bot.numbers = new ArrayList<Equation>();
                    top.numbers = new ArrayList<Equation>();
                    top.numbers.add(NumConstEquation.create(value, owner));
                }

                if (bot.plusMinus) {
                    bot.plusMinus = false;
                    top.plusMinus = true;
                }

                Equation topEq = top.getEquation(owner);
                Equation botEq = bot.getEquation(owner);
                result = getResult(topEq, botEq);
            } else {
                Equation topEq = a;
                Equation botEq = b;
                result = getResult(topEq, botEq);
            }
        }
        return result;
    }

    private static Equation getResult(Equation topEq, Equation botEq) {
        SuperView owner = Algebrator.getAlgebrator().solveView;
        // if the top is 0 and the bottom is not
        if ((sortaNumber(topEq) && getValue(topEq).doubleValue() == 0) && !((sortaNumber(botEq) && getValue(botEq).doubleValue() == 0))) {
            ((ColinView)owner).tryWarn(botEq);
            return new NumConstEquation(0.0, owner);
        } else
            // they are both meaningful
            if (!(sortaNumber(botEq) && getValue(botEq).abs().doubleValue() == 1)) {

                Equation inner = new DivEquation(owner);
                inner.add(topEq);
                inner.add(botEq);
                return inner;
            }
            // bottom is 1
            else {
                if (botEq.isPlusMinus()){
                    return topEq.plusMinus();
                }else if (botEq.isNeg()) {
                    return topEq.negate();
                } else  {
                    return topEq;
                }
            }
    }

    public static BigDecimal getValue(Equation e) {
        BigDecimal minus = BigDecimal.ONE;
        while (e instanceof MinusEquation || e instanceof PlusMinusEquation) {
            e = e.get(0);
            minus = minus.negate();
        }
        return ((NumConstEquation) e).getValue().multiply(minus);
    }

    public static boolean sortaNumber(Equation e) {
        return e.removeNeg() instanceof NumConstEquation;
    }

    public static Equation flip(Equation demo) {

        SuperView owner = Algebrator.getAlgebrator().solveView;
        // if it's a div equation flip it over
        if (demo instanceof DivEquation) {
            if (demo.get(0) instanceof NumConstEquation && ((NumConstEquation) demo.get(0)).getValue().doubleValue() == 1) {
                return demo.get(1);
            }
            Equation result = new DivEquation(owner);
            result.add(demo.get(1));
            result.add(demo.get(0));
            return result;
        }
        // else just throw a 1 over it
        Equation result = new DivEquation(owner);
        result.add(new NumConstEquation(BigDecimal.ONE, owner));
        result.add(demo);
        return result;
    }
}
