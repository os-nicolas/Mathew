package cube.d.n.practice;

import android.util.Log;

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
            Log.e("closeAt","");

            res = new AddEquation(new NullLine());
            res.add(new VarEquation("Bad!",new NullLine()));
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
                boolean canConvert = false;
                try{
                    Double.parseDouble(inpus[at]);
                    canConvert = true;
                }catch(Exception e){}

                if (canConvert){
                    res.add(NumConstEquation.create(Double.parseDouble(inpus[at]),new NullLine()));
                    at++;
                }else {
                    res.add(new VarEquation(inpus[at],new NullLine()));
                    at++;
                }
            }
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
