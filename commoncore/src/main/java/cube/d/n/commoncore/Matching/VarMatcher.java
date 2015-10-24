package cube.d.n.commoncore.Matching;

import cube.d.n.commoncore.GS;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.EquationLine;

/**
 * Created by Colin_000 on 10/24/2015.
 */
public class VarMatcher extends EquationMatcher {

    GS<String> lookingFor = new GS<>("");

    /*
    lookingFor should be passed in with "" as it's value if you you want to to pick up whatever value it encounters
     */
    public VarMatcher(EquationLine owner,GS<String> lookingFor) {
        this(owner);
        this.lookingFor = lookingFor;
    }

    public VarMatcher(EquationLine owner) {
        super(owner);
    }

    @Override
    public Equation copy() {
        //TODO...
        // how to make this nice?!
        return new VarMatcher(owner,new GS<String>(lookingFor));
    }

    @Override
    public boolean same(Equation other){
        if (!(other instanceof VarEquation)){
            return false;
        }else {
            VarEquation vOther = (VarEquation) other;
            if (lookingFor.get().equals("")) {
                lookingFor.set(vOther.getDisplay());
                return true;
            } else {
                return lookingFor.get().equals(vOther.getDisplay());
            }
        }

    }

}
