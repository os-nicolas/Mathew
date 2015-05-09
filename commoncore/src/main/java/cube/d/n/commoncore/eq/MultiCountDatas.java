package cube.d.n.commoncore.eq;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MinusEquation;

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
}
