package cube.d.n.commoncore.lines;

import java.util.ArrayList;

import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin_000 on 9/11/2015.
 */
public abstract class InlineInputLine extends  InputLine {
    public InlineInputLine(Main owner) {
        super(owner);
    }

    public abstract void updateModie();

    public abstract Equation makeModie(ArrayList<Equation> converted);
}
