package cube.d.n.commoncore;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;

import cube.d.n.commoncore.eq.BinaryOperator;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MinusEquation;
import cube.d.n.commoncore.eq.any.MonaryEquation;
import cube.d.n.commoncore.eq.any.MultiDivSuperEquation;
import cube.d.n.commoncore.eq.any.MultiEquation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.PlusMinusEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.NullLine;

/**
 * Created by Colin_000 on 6/26/2015.
 */
public class Util {

    public static Equation stringEquation(String [] inpus){
        int at =0;
        Equation res;

        String db = "";
        for (String s: inpus){
            db+=s+",";
        }
        Log.d("stringEquation" ,db);

        if (inpus[at].equals("+")){
            res = new AddEquation(new NullLine());
        }else if (inpus[at].equals("-")){
            res = new MinusEquation(new NullLine());
        }else if (inpus[at].equals("/")){
            res = new DivEquation(new NullLine());
        }else if (inpus[at].equals("^")){
            res = new PowerEquation(new NullLine());
        }else if (inpus[at].equals("*")){
            res = new MultiEquation(new NullLine());
        }else if (inpus[at].equals("±")){
            res = new PlusMinusEquation(new NullLine());
        }else if (inpus[at].equals("=")){
            res = new EqualsEquation(new NullLine());
        }else {
            res = getEquation(inpus[at]);
        }
        at++;
        while (at < inpus.length){
            if (inpus[at].equals("(")){
                int end= closeAt(inpus,at);
                String[] inner = new String[end-(at+1)];
                for (int i = at+1; i < end;i++){
                    inner[i-(at+1)] = inpus[i];
                }

                res.add(stringEquation(inner));
                at = end+1;
            }else{
                res.add( getEquation(inpus[at]));
                at++;
            }
        }
        return res;
    }

    private static Equation getEquation(String inpu) {
        Equation res;
        boolean canConvert = false;
        try{
            new BigDecimal(inpu);
            canConvert = true;
        }catch(Exception e){}

        if (canConvert){
            res= NumConstEquation.create(new BigDecimal(inpu), new NullLine(),true);
        }else {
            res=new VarEquation(inpu,new NullLine());
        }
        return res;
    }

    private static int closeAt(String[] str, int start) {
        int at = start;
        int count = 0;
        while (at< str.length){
            if (str[at].equals("(")){
                count++;
            }else if (str[at].equals(")")){
                count--;
            }
            if (count==0){
                return at;
            }
            at++;
        }
        Log.e("closeAt","match not found");
        return -1;
    }

    public static void reduce(GS<Equation> stupid) {

        Equation outerLast = null;
        int startAt =0;
        while (outerLast == null || !outerLast.reallySame(stupid.get())) {
            int at = startAt;
            outerLast = stupid.get().copy();

            while (at < stupid.get().size()) {
                reduce(stupid.get() ,at);

                stupid.get().fixIntegrety();
                if (at+1 < stupid.get().size()){
                    reduce(stupid.get(),at+1);
                    stupid.get().fixIntegrety();
                }

                if (shouldOperate(stupid.get(),at)){
                    stupid.get().tryOperator(at);
                }
                stupid.get().fixIntegrety();
                if (at<  stupid.get().size()){
                    if ((stupid.get().get(at) instanceof DivEquation &&
                            Operations.sortaNumber(stupid.get().get(at).get(1)) &&
                            Operations.getValue(stupid.get().get(at).get(1)).doubleValue() ==0)||(
                            stupid.get().get(at) instanceof PlusMinusEquation && stupid.get().get(at).get(0).size()==0
                    )){
                        Equation oldEq = stupid.get().get(at);
                        stupid.get().get(at).justRemove();
                        stupid.get().add(0,oldEq);
                        startAt++;
                    }}
                at++;
            }
        }
    }



    private static Equation reduce(Equation parent,int index) {
        Equation outerLast = null;
        Equation newEq = parent.get(index);
        int startAt =0;
        while (outerLast == null || !outerLast.reallySame(newEq)) {
            int at = startAt;
            outerLast = newEq.copy();

            while (at < newEq.size()) {
                reduce(newEq,at);
                newEq.fixIntegrety();
                if (at+1 < newEq.size()){
                    reduce(newEq,at+1);
                    newEq.fixIntegrety();
                }
                if (shouldOperate(newEq,at)) {
                    newEq.tryOperator(at);
                }
                newEq = parent.get(index);
                newEq.fixIntegrety();

                if (at<  newEq.size()) {
                    if ((newEq.get(at) instanceof DivEquation &&
                            Operations.sortaNumber(newEq.get(at).get(1)) &&
                            Operations.getValue(newEq.get(at).get(1)).doubleValue() == 0) || (
                            newEq.get(at) instanceof PlusMinusEquation && newEq.get(at).get(0).size() == 0 && newEq  instanceof AddEquation
                    )) {
                        Equation oldEq = newEq.get(at);
                        newEq.get(at).justRemove();
                        newEq.add(0, oldEq);
                        startAt++;
                    }
                }
                at++;
            }
        }

        return newEq;
    }

    private static boolean shouldOperate(Equation equation, int at) {
        if (equation instanceof BinaryOperator &&  at +1 <equation.size() ){
            if ((equation.get(at).removeNeg() instanceof NumConstEquation)&&
                    (equation.get(at+1).removeNeg() instanceof NumConstEquation)){
                return true;
            }
            //div and multi don'r care about +-
            if (equation instanceof MultiDivSuperEquation){
                if ((equation.get(at).removeSign() instanceof NumConstEquation || equation.get(at).removeSign() instanceof DivEquation)&&
                        (equation.get(at+1).removeSign() instanceof NumConstEquation || equation.get(at+1).removeSign() instanceof DivEquation)){
                    return true;
                }
            }
            // we want to expand tho
            if (equation instanceof MultiEquation){
                if ((equation.get(at).removeNeg() instanceof NumConstEquation || equation.get(at).removeNeg() instanceof AddEquation)&&
                        (equation.get(at+1).removeNeg() instanceof NumConstEquation || equation.get(at+1).removeNeg() instanceof AddEquation)){
                    return true;
                }
            }
            // we want to put things under a common denominator also
            if (equation instanceof AddEquation){
                if ((equation.get(at).removeNeg() instanceof NumConstEquation || equation.get(at).removeNeg() instanceof DivEquation)&&
                        (equation.get(at+1).removeNeg() instanceof NumConstEquation || equation.get(at+1).removeNeg() instanceof DivEquation)){
                    return true;
                }
            }

        }


        if (equation instanceof MonaryEquation){
            return true;
        }
        return false;
    }

    public static  ArrayList<String> getVars(Equation stupid) {
        ArrayList<String> result = new ArrayList<>();
        if (stupid instanceof VarEquation){
            result.add(stupid.getDisplay(-1));
        }
        for (Equation e: stupid){
            ArrayList<String> innerResult = getVars(e);
            for (String s: innerResult){
                if (!result.contains(s)){
                    result.add(s);
                }
            }
        }
        return result;
    }
}
