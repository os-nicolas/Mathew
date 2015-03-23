package colin.algebrator.eq;

import android.util.Log;

import colin.example.algebrator.SuperView;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by Colin_000 on 1/18/2015.
 */
public class EquationCounts {
    Equation root = null;
    HashMap<Equation,Float> equations = new HashMap<Equation,Float>();
    BigDecimal v =BigDecimal.ZERO;
    boolean neg = false;

    public EquationCounts(Equation e) {
        while (e instanceof MinusEquation){
            neg = !neg;
            e = e.get(0);
        }
        if (e instanceof  PowerEquation){
            root = e.get(0);
            // add all the top stuff
            Equation power = e.get(1);
            if (power instanceof AddEquation){
                for (Equation ee:power){
                    update(ee);
                }
            }else{
                update(e.get(1));
            }
        }else{
            if (neg){
                e = e.negate();
                neg = false;
            }
            root = e;
            v=BigDecimal.ONE;
        }
    }

    public EquationCounts() { }

    public EquationCounts(EquationCounts toCopy) {
        root = toCopy.root.copy();
        equations = new HashMap<Equation,Float>();
        for (Equation k: toCopy.equations.keySet()){
            equations.put(k.copy(),toCopy.equations.get(k));
        }
        v =new BigDecimal(toCopy.v.doubleValue());
        neg = toCopy.neg;
    }

    private void update(Equation e) {
        boolean innerNeg = false;
        while (e instanceof MinusEquation){
            innerNeg = !innerNeg;
            e = e.get(0);
        }
        if (e instanceof NumConstEquation){
            v = v.add (innerNeg?((NumConstEquation)e).getValue().negate():((NumConstEquation)e).getValue());
        }else{
            boolean hasMatch =false;
            if (innerNeg){
                e = e.negate();
            }
            for (Equation ee: equations.keySet()){
                if (ee.same(e)){
                    equations.put(ee,equations.get(ee)+1);
                    hasMatch = true;
                    break;
                }
            }
            if (!hasMatch){
                equations.put(e,1f);
            }
        }
    }


    public boolean add(Equation e) {
        boolean innerNeg = false;
        while (e instanceof MinusEquation){
            innerNeg = !innerNeg;
            e = e.get(0);
        }
        if (e instanceof  PowerEquation && e.get(0).same(root)){
            Equation power = e.get(1);
            if (power instanceof AddEquation){
                for (Equation ee:power){
                    update(ee);
                }
            }else{
                update(e.get(1));
            }
            return true;
        }else if (e.same(root)){
            v=v.add(BigDecimal.ONE);
            return true;
        }
        return false;
    }

    public Equation getEquation() {
        SuperView owner = root.owner;
        if (equations.size()==0){
            if ( v.doubleValue()  == 1) {
                return root;
            }else if (v.doubleValue()  == 0){
                return new NumConstEquation(BigDecimal.ONE, owner);
            }else {
                Equation newEq = new PowerEquation(owner);
                newEq.add(root);
                newEq.add(new NumConstEquation(v, owner));
                return newEq;
            }
        }else if (equations.size()==1 && v.doubleValue()==0){
            Equation newEq = new PowerEquation(owner);
            newEq.add(root);
            Equation key =(Equation)equations.keySet().toArray()[0];
            if (equations.get(key)==0){
                newEq = new NumConstEquation(BigDecimal.ONE,owner);
            }else if (equations.get(key)==1){
                newEq.add(key);
            }else{
                Equation multi = new MultiEquation(owner);
                Equation num = NumConstEquation.create(new BigDecimal(equations.get(key)),owner);
                multi.add(num);
                multi.add(key);
                newEq.add(multi);
            }
            return newEq;
        }else{
            Equation newEq = new PowerEquation(owner);
            newEq.add(root);
            Equation addEq = new AddEquation(owner);
            for (Object o:equations.keySet().toArray()){
                Equation key = (Equation)o;
                if (equations.get(key)==0){
                }else  if (equations.get(key)==1){
                    addEq.add(key);
                }else{
                    Equation multi = new MultiEquation(owner);
                    Equation num = NumConstEquation.create(new BigDecimal(equations.get(key)),owner);
                    multi.add(num);
                    multi.add(key);
                    addEq.add(multi);
                }
            }
            if (v.doubleValue() != 0) {
                addEq.add(new NumConstEquation(v, owner));
            }
            if (addEq.size()==0) {
                newEq = new NumConstEquation(BigDecimal.ONE,owner);
            }else if (addEq.size()==1){
                newEq = addEq.get(0);
            }else{
                newEq.add(addEq);
            }
            return newEq;
        }
    }

    public boolean isEmpty(){
        if (v.equals(BigDecimal.ZERO) && equations.isEmpty()){
            return  true;
        }
        return false;
    }

    public EquationCounts remainder(EquationCounts equationCounts) {
        EquationCounts result = new EquationCounts(this);
        // if the roots are not the same log
        if (!result.root.same(equationCounts.root)){
            Log.e("ec remainder", "they should have the same root");
        }
        result.v = result.v.subtract(equationCounts.v);
        result.neg = (result.neg != equationCounts.neg );

        for (Equation k: equationCounts.equations.keySet()){
            float sub = equationCounts.equations.get(k);
            float subFrom = 0;
            Equation myKey= null;
            for ( Equation resultK: result.equations.keySet()){
                if ( k.same(resultK)){
                    subFrom =result.equations.get(resultK);
                    myKey = resultK;
                    break;
                }
            }
            float numleft = subFrom - sub;
            if (numleft == 0){
                result.equations.remove(myKey);
            }else{
                result.equations.put(myKey,numleft);
            }
        }

        return result;
    }
}
