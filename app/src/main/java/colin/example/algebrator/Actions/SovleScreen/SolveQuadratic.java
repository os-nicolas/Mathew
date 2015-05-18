package colin.example.algebrator.Actions.SovleScreen;

import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.MultiCountData;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.any.PlusMinusEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.ColinView;

/**
 * Created by Colin_000 on 3/25/2015.
 */
public class SolveQuadratic extends Action<ColinView> {

    String varName="";

    public SolveQuadratic(String varName, ColinView colinView) {
        super(colinView);
        this.varName = varName;
    }

    @Override
    protected void privateAct() {
        Equation stuffSide= null;
        if (myView.getStupid().get(0) instanceof NumConstEquation && ((NumConstEquation) myView.getStupid().get(0)).isZero()) {
            stuffSide = myView.getStupid().get(1);
        } else if (myView.getStupid().get(1) instanceof NumConstEquation && ((NumConstEquation) myView.getStupid().get(1)).isZero()) {
            stuffSide = myView.getStupid().get(0);
        }
        Equation a = getA(stuffSide);
        Equation b = getB(stuffSide);
        Equation c = getC(stuffSide);
        Equation newStupid = new EqualsEquation(stuffSide.owner);

            Equation x1 = new VarEquation(varName, stuffSide.owner);
            newStupid.add(x1);
            Equation result = new DivEquation(stuffSide.owner);
            newStupid.add(result);
        if (b!= null) {
            Equation top = new AddEquation(stuffSide.owner);
            result.add(top);
            top.add(b.copy().negate());
            Equation plusMinus = new PlusMinusEquation(stuffSide.owner);
            top.add(plusMinus);
            Equation sqrt = new PowerEquation(stuffSide.owner);
            plusMinus.add(sqrt);
            if (c != null) {
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
            } else {
                Equation squaredB = new PowerEquation(stuffSide.owner);
                sqrt.add(squaredB);
                Equation b2 = b.copy();
                squaredB.add(b2);
                Equation two = NumConstEquation.create(2, stuffSide.owner);
                squaredB.add(two);
            }
            Equation half = NumConstEquation.create(.5, stuffSide.owner);
            sqrt.add(half);
        }else{
            Equation top = new PowerEquation(stuffSide.owner);
            result.add(top);
            Equation multi4AC = new MultiEquation(stuffSide.owner);
            Equation four = NumConstEquation.create(4, stuffSide.owner);
            multi4AC.add(four);
            Equation a1 = a.copy();
            multi4AC.add(a1);
            Equation c1 = c.copy();
            multi4AC.add(c1);
            top.add(multi4AC.negate());
            Equation half = NumConstEquation.create(.5, stuffSide.owner);
            top.add(half);
        }
                Equation bot = new MultiEquation(stuffSide.owner);
                result.add(bot);
                    Equation two = NumConstEquation.create(2, stuffSide.owner);
                    bot.add(two);
                    Equation a2 = a.copy();
                    bot.add(a2);
        stuffSide.owner.setStupid(newStupid);
        ((ColinView)newStupid.owner).changed();

    }

    @Override
    public boolean canAct() {
        if (myView.getStupid() instanceof EqualsEquation) {
            Equation stuffSide;
            if (myView.getStupid().get(0) instanceof NumConstEquation && ((NumConstEquation) myView.getStupid().get(0)).isZero()) {
                stuffSide = myView.getStupid().get(1);
            } else if (myView.getStupid().get(1) instanceof NumConstEquation && ((NumConstEquation) myView.getStupid().get(1)).isZero()) {
                stuffSide = myView.getStupid().get(0);
            } else {
                return false;
            }

                if (worksFor(stuffSide)) {
                    return true;
                }
        }
        return false;
    }

    private boolean worksFor(Equation stuffSide) {
        if (stuffSide instanceof AddEquation &&
                stuffSide.size() == 2 &&
                getA(stuffSide) != null &&
                (getC(stuffSide) != null || getB(stuffSide) != null)) {
            return true;
        } else if (stuffSide instanceof AddEquation &&
                stuffSide.size() == 3 &&
                getA(stuffSide) != null &&
                getB(stuffSide) != null &&
                getC(stuffSide) != null) {
            return true;
        }
        return false;
    }

//    private String getVarString(Equation stuffSide) {
//        String result = "";
//        if (stuffSide instanceof VarEquation) {
//            return stuffSide.getDisplay(-1);
//        } else {
//            for (Equation e : stuffSide) {
//                String myResult = getVarString(e);
//                if (!myResult.equals("")) {
//                    if (!myResult.equals(result) && !result.equals("")) {
//                        return "~bad var name";
//                    } else {
//                        result = myResult;
//                    }
//                }
//            }
//        }
//        return result;
//    }

    // TODO redo this all match MultiCountData

    // a*x^2
    private Equation getA(Equation stuffSide) {
        Equation result = null;
        Equation lookingFor = new PowerEquation(stuffSide.owner);
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
                if (eq.removeSign() instanceof NumConstEquation){
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
