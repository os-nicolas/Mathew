package cube.d.n.commoncore;

import android.util.Log;

import java.math.BigDecimal;

import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MinusEquation;
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
            res= NumConstEquation.create(new BigDecimal(inpu), new NullLine());
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

}
