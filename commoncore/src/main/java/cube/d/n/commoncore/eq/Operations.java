package cube.d.n.commoncore.eq;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;



import cube.d.n.commoncore.CanWarn;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MinusEquation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.PlusMinusEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.SignEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;


/**
 * Created by Colin on 1/15/2015.
 */
public class Operations {

    // **************************** MULTIPLY *****************************************

    public static MultiCountDatas Multiply(MultiCountDatas left, MultiCountDatas right, boolean simplify,EquationLine owner) {


        MultiCountDatas result = new MultiCountDatas();
        for (MultiCountData a : right) {
            for (MultiCountData b : left) {
                MultiCountData toAdd = Multiply(a, b, owner);
                boolean found = false;
                if (simplify) {
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
                }
                if (!found) {
                    result.add(toAdd);
                }
            }
        }
        result.neg = (left.neg != right.neg);

        return result;
    }

    private static MultiCountData Multiply(MultiCountData a, MultiCountData b,EquationLine owner) {
        MultiCountData result = new MultiCountData();

        for (MultiCountData e : new MultiCountData[]{a, b}) {
            multiplyHelper(e, result,owner);
        }
        return result;
    }

    private static void multiplyHelper(MultiCountData newMcd, MultiCountData result,EquationLine owner) {

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
            set.add(new MultiCountData(e));
        }
    }

    // **************************** ADD *****************************************

    public static Equation Add(MultiCountData left, MultiCountData right,EquationLine owner) {
        if (add_canAddNumber(left,right,owner)){
            return add_AddNumber(left,right,owner);
        }else if (add_canCommonDenom(left, right, owner)){
            return add_CommonDenom(left, right, owner);
        }else if (add_canCommon(left, right, owner)){
            return add_Common(left, right, owner);
        }else  if (add_canZero(left, right, owner)){
            return add_Zero(left, right, owner);
        }else {//if (canCombineLikeTerms(left, right, owner)){
            // this is really not combine like terms, it's really do nothing
            //return add_CombineLikeTerms(left, right, owner);

            AddEquation result = new AddEquation(owner);
            result.smartAdd(left.getEquation(owner));
            result.smartAdd(right.getEquation(owner));
            return result;
        }

//
//        //
//        MultiCountData under = null;
//        boolean dontDoIt = false;
//        if (overZero(left,owner) || overZero(right,owner)){
//            dontDoIt =true;
//        }else if (left.under != null && right.under == null) {
//            under = left.under;
//            right = Multiply(right, under,owner);
//            left.under = null;
//        } else if (left.under == null && right.under != null) {
//            under = right.under;
//            left = Multiply(left, under,owner);
//            right.under = null;
//        } else if (left.under != null && right.under != null) {
//            MultiCountData common = deepFindCommon(left.under, right.under);
//            MultiCountData leftRem = remainder(left.under, common,owner);
//            MultiCountData rightRem = remainder(right.under, common,owner);
//
//            under = Multiply(right.under, leftRem,owner);
//            left.under = null;
//            right.under = null;
//
//            right = Multiply(right, leftRem,owner);
//
//            left = Multiply(left, rightRem,owner);
//
//        }
//
//
//
//        //if under == null we actully add
//        if (!dontDoIt && under == null && !(left.key.isEmpty() &&
//                right.key.isEmpty() &&
//                (right.numbers.size() > 1 || left.numbers.size() > 1))) {
//
//
//            MultiCountData common = findCommon(left, right);
//            if (!common.key.isEmpty() || !common.numbers.isEmpty()) {
//                left = remainder(left, common,owner);
//                right = remainder(right, common,owner);
//            }
//            Equation result = addHelper(left, right,owner);
//            // and multiply the result time common if there is any common
//            if (result instanceof NumConstEquation && ((NumConstEquation) result).getValue().doubleValue() == 0) {
//                return result;
//            } else if (common.notOne()) {
//                // handle 5A + 5A in this case we get
//                // result = 2, commmon = 5,A
//                // we want to clear out result and
//                if (sortaNumber(result) && common.numbers.size() == 1) {
//                    BigDecimal number = getValue(result).multiply(common.getValue());
//                    number = (common.negative ? number.negate() : number);
//                    common.numbers = new ArrayList<>();
//                    common.numbers.add(NumConstEquation.create(number, owner));
//                    return common.getEquation(owner);
//                } else
//                    // handle -A -A
//                    if (common.numbers.size() == 1 && common.getValue().doubleValue() == -1) {
//                        return result.negate();
//                    } else
//                        // handle 2A - A
//                        if (sortaNumber(result) && getValue(result).doubleValue() == 1) {
//                            return common.getEquation(owner);
//                        } else {
//                            // handle AB + AC
//                            Equation holder = new MultiEquation(owner);
//                            holder.add(result);
//                            holder.add(common.getEquation(owner));
//                            return holder;
//                        }
//            } else {
//                return result;
//            }
//        }
//        //otherwise we are done
//        else {
//            //return (left + right ) /under
//            Equation lEq = left.getEquation(owner);
//            Equation rEq = right.getEquation(owner);
//            Equation top = new AddEquation(owner);
//            for (Equation eq : new Equation[]{lEq, rEq}) {
//                if (eq instanceof AddEquation) {
//                    for (Equation e : eq) {
//                        top.add(e);
//                    }
//                } else {
//                    top.add(eq);
//                }
//            }
//            if (under != null) {
//                Log.d("bot", "combine:  " + under.combine + " numbers.size: " + under.numbers.size() + "under.getValue" + under.getValue().doubleValue() + " neg: " + under.negative);
//                Equation bot = under.getEquation(owner);
//                Equation result = new DivEquation(owner);
//                result.add(top);
//                result.add(bot);
//                return result;
//            }else{
//                return top;
//            }
//        }
    }

    protected static boolean canCombineLikeTerms(MultiCountData left, MultiCountData right, EquationLine owner) {
        return !overZero(left,owner) && !overZero(right,owner);
    }

    public static Equation add_CombineLikeTerms(MultiCountData left, MultiCountData right, EquationLine owner) {
        return addHelper(left, right,owner);
    }

    public static Equation add_CommonDenom(MultiCountData left, MultiCountData right, EquationLine owner) {
        MultiCountData under=null;
        if (left.under != null && right.under == null) {
            under = left.under;
            right = Multiply(right, under,owner);
            left.under = null;
        } else if (left.under == null && right.under != null) {
            under = right.under;
            left = Multiply(left, under,owner);
            right.under = null;
        } else if (left.under != null && right.under != null) {
            MultiCountData common = deepFindCommon(left.under, right.under);
            MultiCountData leftRem = remainder(left.under, common,owner);
            MultiCountData rightRem = remainder(right.under, common,owner);

            under = Multiply(right.under, leftRem,owner);
            left.under = null;
            right.under = null;

            right = Multiply(right, leftRem,owner);

            left = Multiply(left, rightRem,owner);
        }

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

        Log.d("bot", "combine:  " + under.combine + " numbers.size: " + under.numbers.size() + "under.getValue" + under.getValue().doubleValue() + " neg: " + under.negative);
        Equation bot = under.getEquation(owner);
        Equation result = new DivEquation(owner);
        result.add(top);
        result.add(bot);
        return result;
    }

    public static boolean add_canCommonDenom(MultiCountData left, MultiCountData right, EquationLine owner) {
        return (left.under !=null || right.under!=null) && !(overZero(left,owner) || overZero(right,owner));
    }

    public static Equation add_AddNumber(MultiCountData left, MultiCountData right, EquationLine owner) {
        return addHelper(left, right, owner);
    }

    public static boolean add_canAddNumber(MultiCountData left, MultiCountData right, EquationLine owner) {
        return left.numbers.size() == 1 &&
                right.numbers.size() == 1 &&
                left.under == null &&
                right.under == null &&
                left.key.size() ==0 &&
                right.key.size() ==0 &&
                !left.plusMinus &&
                !right.plusMinus;
    }

    public static Equation add_Common(MultiCountData left, MultiCountData right, EquationLine owner) {
        MultiCountData common = findCommon(left, right);

        left = remainder(left, common,owner);
        right = remainder(right, common,owner);

        Equation result = addHelper(left, right,owner);

        if (result instanceof NumConstEquation && ((NumConstEquation) result).getValue().doubleValue() == 0) {
            // A-A
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
        } else if (common.plusMinus){
            Log.e("add_Common","we should never factor out a plusMinus");
            return result;
        }else if (common.negative){
            // we factored out a minus sign
                return result.negate();
        } else {
            return result;
        }
    }

    public static boolean add_canCombineLikeTerms(MultiCountData left, MultiCountData right, EquationLine owner) {

        Equation result = new AddEquation(owner);
        result.add(left.getEquation(owner));
        result.add(right.getEquation(owner));

        return !result.same(addHelper(left, right, owner));
    }

    public static boolean add_canCommon(MultiCountData left, MultiCountData right, EquationLine owner) {
        //TODO
        // atm 3a/5 + 4a will return 7a
        // to protect from that we
        if (left.under != null || right.under != null){
            return false;
        }

        MultiCountData common = findCommon(left, right);

        return (!common.key.isEmpty() || !(common.getValue().doubleValue() == 1));
    }

    private static boolean overZero(MultiCountData left,EquationLine owner) {
        return left.under != null && sortaNumber(left.under.getEquation(owner)) && getValue(left.under.getEquation(owner)).doubleValue()==0;
    }

    private static Equation addHelper(MultiCountData left, MultiCountData right,EquationLine owner) {
        // if they are both just numbers make a NumConst
        if (left.key.size() == 0 && right.key.size() == 0 && right.numbers.size() <= 1 && left.numbers.size() <= 1 && !left.plusMinus && !right.plusMinus) {
            BigDecimal sum = right.getValue().add(left.getValue());
            return NumConstEquation.create(sum, owner);
        }
        // otherwise make an add equation and throw them both in it
        if (left.key.isEmpty() && left.numbers.size() == 1 && left.getValue().doubleValue() == 0
                && right.key.isEmpty() && right.numbers.size() == 1 && right.getValue().doubleValue() == 0) {
            return NumConstEquation.create(0, owner);
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


    public static MultiCountData remainder(MultiCountData left, MultiCountData common,EquationLine owner) {
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

        Equation oneEq = NumConstEquation.create(1,owner);
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
                if (e.removeSign().same(ee.removeSign())) {
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
            result.under = remainder(left.under, common.under,owner);
        }
        // what do we do with unders?
        // nothing for now

        return result;
    }

    public static MultiCountData deepFindCommon(MultiCountData left, MultiCountData right) {
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

        // we don't factory anything out of +-
        // this is not really ever useful
        // and it breaks OutputLine.reduce()
        // because it flip between 3+-3 and (1+-1)3 forever
        if (!left.plusMinus && !right.plusMinus) {
            for (int i = 0; i < leftNumCopy.size(); i++) {
                Equation e = leftNumCopy.get(i);
                for (int ii = 0; ii < rightNumCopy.size(); ii++) {
                    Equation ee = rightNumCopy.get(ii);
                    // TODO i am checking for zero and one here but really i think we should just not be able to add them to numbers in the first place
                    if (e.removeSign().same(ee.removeSign()) &&
                            getValue(e).doubleValue() != 0 &&
                            getValue(e).doubleValue() != 1
                            ) {
                        boolean neg = e.isNeg() && ee.isNeg();
                        result.numbers.add((!neg ? e.removeSign() : e));
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
                                && 1 != eInt
                                && 1 != eeInt
                                && getValue(ee).doubleValue() == eeInt
                                && Math.abs(myGcd) != 1 && myGcd != 0) {
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

    public static Equation divide(Equation a, Equation b,EquationLine owner) {
        Equation result = null;

        // we check to see if they are both power equations of the same power
        // or close to that (just a negetive sign away

        if (divide_CanTopIsZero(a, b)){
            result = divide_TopIsZero(a, b, owner);
            if (owner instanceof AlgebraLine) {
                ((AlgebraLine) owner).tryWarn(b.copy());
            }
        }else if (divide_CanBotIsOne(a, b)){
            result = divide_BotIsOne(a, b, owner);
        }else if (divide_CanSamePower(a, b)) {
            result = divide_samePower(a, b, owner);

         //TODO this need to be able to handle negs on the addEquation
        } else if (divide_CanSplitUp(a)) {
            result = divide_SplitUp(a, b, owner);
        } else {
            // figure out what is common
            MultiCountData top = new MultiCountData(a);
            MultiCountData bot = new MultiCountData(b);

            MultiCountData common = deepFindCommon(top, bot);


            if (divide_CanCancel(common)) { // they have anything in common
                result = divide_Cancel(owner, top, bot, common);
                if (owner instanceof AlgebraLine) {
                    ((AlgebraLine) owner).tryWarn(common.getEquation(owner));
                }
            }else  if (divide_CanFlatten(a,b)){
                result = divide_Flatten(a, b, owner);
            } else if (divide_CanSortaNumbers(top, bot)) {


                // if it can be reduced
                if (divide_CanReduce(top, bot)) {

                    result =divide_Reduce(owner, top, bot);

                } else {
                    result = divide_Divide(owner, top, bot);
                }

                // if we have sqrt(5)/23
            }else if (divide_CanBringIn(a)) {
                result = divide_BringIn(a, b, owner);
                // if we have a/b where a and b are sortaNumbers
            }else {
                Equation topEq = a;
                Equation botEq = b;
                result = getResult(topEq, botEq,owner);
            }
        }
        return result;
    }

    public static Equation divide_BotIsOne(Equation top, Equation bot, EquationLine owner) {
        return top.copy();
    }

    public static boolean divide_CanBotIsOne(Equation topEq, Equation botEq) {
        return (sortaNumber(botEq) && getValue(botEq).doubleValue() == 1) && !((sortaNumber(topEq) && getValue(topEq).doubleValue() == 0));
    }

    public static DivEquation divide_Flatten(Equation oldTop, Equation oldBot, EquationLine owner) {
        DivEquation result = new DivEquation(owner);
        Equation top;
        if (oldBot instanceof DivEquation){
            top = new MultiEquation(owner);
            if (oldTop instanceof DivEquation){
                top.add(oldTop.get(0).copy());
            }else{
                top.add(oldTop.copy());
            }
            top.add(oldBot.get(1).copy());
        }else{
            //oldTop instanceof DivEquation
            top=oldTop.get(0).copy();
        }
        result.add(top);

        Equation bot;
        if (oldTop instanceof DivEquation){
            bot = new MultiEquation(owner);
            bot.add(oldTop.get(1).copy());
            if (oldBot instanceof DivEquation){
                bot.add(oldBot.get(0).copy());
            }else{
                bot.add(oldBot.copy());
            }

        }else{
           //oldBot instanceof DivEquation
           bot=oldBot.get(0).copy();
        }
        result.add(bot);

        return result;

    }

    public static boolean divide_CanFlatten(Equation a, Equation b) {
        return a instanceof DivEquation || b instanceof DivEquation;
    }

    public static Equation divide_TopIsZero(Equation a, Equation b, EquationLine owner) {
        return NumConstEquation.create(0,owner);
    }

    public static boolean divide_CanCancel(MultiCountData common) {
        MultiCountData commonAt = common;

        boolean anyCommon = false;
        while (!anyCommon && commonAt != null) {
            if (!commonAt.isEmpty()) {
                anyCommon = true;
            }
            commonAt = commonAt.under;
        }
        return anyCommon && !common.key.isEmpty();
    }

    public static Equation divide_Divide(EquationLine owner, MultiCountData top, MultiCountData bot) {
        Equation result;
        BigDecimal value = top.getValue().divide(bot.getValue(), 20, RoundingMode.HALF_UP);
        top.negative = false;
        bot.numbers = new ArrayList<Equation>();
        top.numbers = new ArrayList<Equation>();
        top.numbers.add(NumConstEquation.create(value, owner));

        if (bot.plusMinus) {
            bot.plusMinus = false;
            top.plusMinus = true;
        }

        Equation topEq = top.getEquation(owner);
        Equation botEq = bot.getEquation(owner);
        result = getResult(topEq, botEq,owner);
        return result;
    }

    public static Equation divide_Reduce(EquationLine owner, MultiCountData top, MultiCountData bot) {
        Equation result;
        int topInt = (int) Math.floor(top.getValue().doubleValue());
        int botInt = (int) Math.floor(bot.getValue().doubleValue());
        int myGcd = gcd(topInt, botInt);

        bot.numbers = new ArrayList<Equation>();
        bot.numbers.add(NumConstEquation.create(new BigDecimal(botInt / myGcd), owner));
        top.numbers = new ArrayList<Equation>();
        top.numbers.add(NumConstEquation.create(new BigDecimal(topInt / myGcd), owner));

        if (bot.plusMinus) {
            bot.plusMinus = false;
            top.plusMinus = true;
        }


        Equation topEq = top.getEquation(owner);
        Equation botEq = bot.getEquation(owner);
        result = getResult(topEq, botEq,owner);
        return result;
    }

    public static boolean divide_CanReduce(MultiCountData top, MultiCountData bot) {
        int topInt = (int) Math.floor(top.getValue().doubleValue());
        int botInt = (int) Math.floor(bot.getValue().doubleValue());
        int myGcd = gcd(topInt, botInt);

        return bot.getValue().doubleValue() == botInt
                && top.getValue().doubleValue() == topInt
                && Math.abs(myGcd) != 1;
    }



    public static boolean divide_CanSortaNumbers(MultiCountData top, MultiCountData bot) {
        return  bot.numbers.size() == 1 && top.numbers.size() <= 1  && bot.under == null && top.under == null && !(bot.getValue().doubleValue() == 0);// we don't care if top has a number a/.5 is totally ok
    }

    public static Equation divide_BringIn(Equation a, Equation b, EquationLine owner) {
        Equation result;
        result = new PowerEquation(owner);
        // we raise b to the the same power as a and bring it inside
        Equation newBot = new PowerEquation(owner);
        newBot.add(b.copy());
        newBot.add(Operations.flip(a.removeSign().get(1).copy()));
        Equation newTop = a.removeSign().get(0).copy();
        Equation inside = new DivEquation(owner);
        inside.add(newTop);
        inside.add(newBot);
        result.add(inside);
        result.add(a.removeSign().get(1).copy());
        result = a.passNegs(result);
        return result;
    }

    public static boolean divide_CanBringIn(Equation a) {
        return a.removeSign() instanceof PowerEquation;
    }

    public static Equation divide_Cancel(EquationLine owner, MultiCountData top, MultiCountData bot, MultiCountData common) {
        Equation result;

        MultiCountData topData = remainder(top, common,owner);
        MultiCountData botData = remainder(bot, common,owner);
        Equation topEq = topData.getEquation(owner);
        Equation botEq = botData.getEquation(owner);
        result = getResult(topEq, botEq,owner);
        return result;
    }

    public static Equation divide_SplitUp(Equation a, Equation b, EquationLine owner) {
        Equation result;
        result = new AddEquation(owner);
        for (Equation e : a.removeSign()) {
            Equation newEq = new DivEquation(owner);
            newEq.add(e);
            newEq.add(b.copy());
            result.add(newEq);
        }
        result = a.passNegs(result);
        return result;
    }

    public static boolean divide_CanSplitUp(Equation a) {
        return a.removeSign() instanceof AddEquation;
    }

    public static boolean divide_CanSamePower(Equation a, Equation b) {
        return (a.removeSign() instanceof PowerEquation &&
                b.removeSign() instanceof PowerEquation &&
                a.removeSign().get(1).same(b.removeSign().get(1)));
    }

    public static Equation divide_samePower(Equation a, Equation b, EquationLine owner) {
        boolean neg = a.isNeg() != b.isNeg();
        boolean plusMinus = a.isPlusMinus() != b.isPlusMinus();
        Equation div = new DivEquation(owner);
        div.add(a.removeSign().get(0).copy());
        div.add(b.removeSign().get(0).copy());
        Equation power = new PowerEquation(owner);
        power.add(div);
        power.add(a.removeSign().get(1).copy());

        Equation result=null;
        if (plusMinus) {
            result = power.plusMinus();
        } else if (neg) {
            result = power.negate();
        } else {
            result = power;
        }
        return result;
    }

    public static Equation getResult(Equation topEq, Equation botEq,EquationLine owner) {
        // if the top is 0 and the bottom is not
        if (divide_CanTopIsZero(topEq, botEq)) {
            if (owner instanceof CanWarn) {
                ((CanWarn) owner).tryWarn(botEq);
            }
            return NumConstEquation.create(0.0, owner);
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

    public static boolean divide_CanTopIsZero(Equation topEq, Equation botEq) {
        return (sortaNumber(topEq) && getValue(topEq).doubleValue() == 0) && !((sortaNumber(botEq) && getValue(botEq).doubleValue() == 0));
    }

    public static BigDecimal getValue(Equation e) {
        BigDecimal minus = BigDecimal.ONE;
        Equation ee= e.copy();
        while (ee instanceof MinusEquation || ee instanceof PlusMinusEquation) {
            ee = ee.get(0);
            minus = minus.negate();
        }
        return ((NumConstEquation) ee).getValue().multiply(minus);
    }

    public static boolean sortaNumber(Equation e) {
        return e.removeSign() instanceof NumConstEquation;
    }

    public static Equation flip(Equation demo) {

        EquationLine owner = demo.owner;

        // if we have a number we invert it
        if (sortaNumber(demo)){
            return NumConstEquation.create(BigDecimal.ONE.divide(getValue(demo)),owner);
        }

        // if it's div equation we flip it over
        if (demo.removeSign() instanceof DivEquation) {
            Equation at = (Equation)demo;
            Equation atPrnt = null;
            while (at instanceof SignEquation) {
                atPrnt= at;
                at = at.get(0);
            }

            // if we have 1/asdfas we just want asdfas not asdfas/1
            if (at.get(0) instanceof NumConstEquation && ((NumConstEquation) at.get(0)).getValue().doubleValue() == 1) {
                if (atPrnt==null){
                    return at.get(1);
                }else{
                    atPrnt.set(0,at.get(1));
                    return demo;
                }
            }else {
                Equation result = new DivEquation(owner);
                result.add(at.get(1));
                result.add(at.get(0));
                if (atPrnt==null){
                    return result;
                }else{
                    atPrnt.set(0,result);
                    return demo;
                }
            }
        }
        // else just throw a 1 over it
        Equation result = new DivEquation(owner);
        result.add(NumConstEquation.create(BigDecimal.ONE, owner));
        result.add(demo);
        return result;
    }


    public static boolean add_canZero(MultiCountData left, MultiCountData right, EquationLine owner) {
        return (left.sortaNumber() && left.getValue().doubleValue() == 0) || (right.sortaNumber() && right.getValue().doubleValue() ==0);
    }

    public static Equation add_Zero(MultiCountData left, MultiCountData right, EquationLine owner) {
        if (left.sortaNumber() && left.getValue().doubleValue() == 0){
            return right.getEquation(owner);
        }else{
            return left.getEquation(owner);
        }
    }
}
