package cube.d.n.commoncore.eq;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MinusEquation;
import cube.d.n.commoncore.lines.Line;

/**
 * Created by Colin on 2/16/2015.
 */
public class MultiCountDatas extends  ArrayList<MultiCountData>{
    public boolean neg =false;

    public MultiCountDatas(Equation eq){
        super();
        while (eq instanceof MinusEquation){
            eq = eq.get(0);
            neg = !false;
        }

        Operations.findEquation(eq, this);

    }

    public MultiCountDatas() {
        super();
    }

    public Equation getEquation(Line owner) {
        Equation result=null;
        if (size() == 1) {
            MultiCountData mine =  get(0);
            //mine.combine = true;
            result = mine.getEquation(owner);
        } else if (size() > 1) {
            result = new AddEquation(owner);
            for (MultiCountData e : this) {
                //e.combine = true;
                result.add(e.getEquation(owner));
            }
        }

        if (neg){
            if (result instanceof MinusEquation){
                result = result.get(0);
            }else{
                result = result.negate();
            }
        }
        return result;

    }
}
