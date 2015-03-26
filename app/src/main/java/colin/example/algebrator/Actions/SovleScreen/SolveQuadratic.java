package colin.example.algebrator.Actions.SovleScreen;

import colin.algebrator.eq.AddEquation;
import colin.algebrator.eq.DivEquation;
import colin.algebrator.eq.EqualsEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.MinusEquation;
import colin.algebrator.eq.MultiCountData;
import colin.algebrator.eq.MultiCountDatas;
import colin.algebrator.eq.MultiEquation;
import colin.algebrator.eq.NumConstEquation;
import colin.algebrator.eq.Operations;
import colin.algebrator.eq.PlusMinusEquation;
import colin.algebrator.eq.PowerEquation;
import colin.algebrator.eq.VarEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.ColinView;

/**
 * Created by Colin_000 on 3/25/2015.
 */
public class SolveQuadratic extends Action<ColinView> {


    public SolveQuadratic(ColinView colinView) {
        super(colinView);
    }

    @Override
    protected void privateAct() {
        Equation stuffSide= null;
        if (myView.stupid.get(0) instanceof NumConstEquation && ((NumConstEquation) myView.stupid.get(0)).isZero()) {
            stuffSide = myView.stupid.get(1);
        } else if (myView.stupid.get(1) instanceof NumConstEquation && ((NumConstEquation) myView.stupid.get(1)).isZero()) {
            stuffSide = myView.stupid.get(0);
        }

        Equation a = getA(stuffSide);
        Equation b = getB(stuffSide);
        Equation c = getC(stuffSide);
        Equation newStupid = new EqualsEquation(stuffSide.owner);
            String varName= getVarString(stuffSide);
            Equation x1 = new VarEquation(varName, stuffSide.owner);
            newStupid.add(x1);
            Equation result = new DivEquation(stuffSide.owner);
            newStupid.add(result);
                Equation top = new AddEquation(stuffSide.owner);
                result.add(top);
                    top.add(b.copy().negate());
                    Equation plusMinus = new PlusMinusEquation(stuffSide.owner);
                    top.add(plusMinus);
                        Equation sqrt = new PowerEquation(stuffSide.owner);
                        plusMinus.add(sqrt);
        if (c!= null){
                            Equation add = new AddEquation(stuffSide.owner);
                            sqrt.add(add);
                                Equation squaredB = new PowerEquation(stuffSide.owner);
                                add.add(squaredB);
                                    Equation b2 = b.copy();
                                    squaredB.add(b2);
                                    Equation two = NumConstEquation.create(2, stuffSide.owner);
                                    squaredB.add(two);
                                Equation multi4AC = new MultiEquation(stuffSide.owner);
                                        Equation four = NumConstEquation.create(4, stuffSide.owner);
                                        multi4AC.add(four);
                                        Equation a1 = a.copy();
                                        multi4AC.add(a1);
                                        Equation c1 = c.copy();
                                        multi4AC.add(c1);
                                add.add(multi4AC.negate());
        }else{
                            Equation squaredB = new PowerEquation(stuffSide.owner);
                            sqrt.add(squaredB);
                                Equation b2 = b.copy();
                                squaredB.add(b2);
                                Equation two = NumConstEquation.create(2, stuffSide.owner);
                                squaredB.add(two);
        }
                        Equation half = NumConstEquation.create(.5, stuffSide.owner);
                        sqrt.add(half);
                Equation bot = new MultiEquation(stuffSide.owner);
                result.add(bot);
                    Equation two = NumConstEquation.create(2, stuffSide.owner);
                    bot.add(two);
                    Equation a2 = a.copy();
                    bot.add(a2);
        stuffSide.owner.stupid = newStupid;
        ((ColinView)newStupid.owner).changed =true;

    }

    @Override
    public boolean canAct() {
        Equation stuffSide;
        if (myView.stupid.get(0) instanceof NumConstEquation && ((NumConstEquation) myView.stupid.get(0)).isZero()) {
            stuffSide = myView.stupid.get(1);
        } else if (myView.stupid.get(1) instanceof NumConstEquation && ((NumConstEquation) myView.stupid.get(1)).isZero()) {
            stuffSide = myView.stupid.get(0);
        } else {
            return false;
        }
        if (getVarString(stuffSide).equals("")) {
            return false;
        }

        if (stuffSide instanceof AddEquation &&
                stuffSide.size() == 2 &&
                null != getA(stuffSide) &&
                (getC(stuffSide) != null || getB(stuffSide) != null)) {
            return true;
        } else if (stuffSide instanceof AddEquation &&
                stuffSide.size() == 3 &&
                null != getA(stuffSide) &&
                getC(stuffSide) != null &&
                getB(stuffSide) != null) {
            return true;
        }

        return false;
    }

    private String getVarString(Equation stuffSide) {
        String result = "";
        if (stuffSide instanceof VarEquation) {
            return stuffSide.getDisplay(-1);
        } else {
            for (Equation e : stuffSide) {
                String myResult = getVarString(e);
                if (!myResult.equals("")) {
                    if (!myResult.equals(result) && !result.equals("")) {
                        return "~bad var name";
                    } else {
                        result = myResult;
                    }
                }
            }
        }
        return result;
    }

    // TODO redo this all match MultiCountData

    // a*x^2
    private Equation getA(Equation stuffSide) {
        Equation result = null;
        Equation lookingFor = new PowerEquation(stuffSide.owner);
        String varName = getVarString(stuffSide);
        lookingFor.add(new VarEquation(varName, stuffSide.owner));
        lookingFor.add(NumConstEquation.create(2,stuffSide.owner));
        if (stuffSide instanceof AddEquation) {
            for (Equation eq : stuffSide) {
                if (eq.containsSame(lookingFor)){
                    MultiCountData term = new MultiCountData(eq);
                    MultiCountData toRemove = new MultiCountData(lookingFor);
                    MultiCountData resultMCD = Operations.remainder(term,toRemove);
                    result = resultMCD.getEquation(stuffSide.owner);
                }
            }
        }
        // this does not handle -(5*x^2) really at all
        if (stuffSide instanceof MultiEquation) {
            if (stuffSide.containsSame(lookingFor)){
                MultiCountData term = new MultiCountData(stuffSide);
                MultiCountData toRemove = new MultiCountData(lookingFor);
                MultiCountData resultMCD = Operations.remainder(term,toRemove);
                result = resultMCD.getEquation(stuffSide.owner);
            }
        }
        if (result instanceof NumConstEquation && ((NumConstEquation) result).isZero()) {
            return null;
        } else {
            return result;
        }
    }

    // b*x
    private Equation getB(Equation stuffSide) {
        Equation result = null;
        String varName = getVarString(stuffSide);
        Equation lookingFor = new VarEquation(varName,stuffSide.owner);
        if (stuffSide instanceof AddEquation) {
            for (Equation eq : stuffSide) {
                if (eq.containsSame(lookingFor)){
                    MultiCountData term = new MultiCountData(eq);
                    MultiCountData toRemove = new MultiCountData(lookingFor);
                    MultiCountData resultMCD = Operations.remainder(term,toRemove);
                    result = resultMCD.getEquation(stuffSide.owner);
                }
            }
        }
        // this does not handle -(5*x) really at all
        if (result instanceof NumConstEquation && ((NumConstEquation) result).isZero()) {
            return null;
        } else {
            return result;
        }
    }

    // c
    private Equation getC(Equation stuffSide) {
        Equation result = null;
        if (stuffSide instanceof AddEquation) {
            for (Equation eq : stuffSide) {
                if (eq.removeNeg() instanceof NumConstEquation){
                    result = eq;
                }
            }
        }
        // does not handle -(x^2 + 2x + 1)
        if (result instanceof NumConstEquation && ((NumConstEquation) result).isZero()) {
            return null;
        } else {
            return result;
        }
    }

}
