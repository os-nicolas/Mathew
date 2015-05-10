package cube.d.n.commoncore.Action;

import android.app.Activity;

import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.v2.lines.AlgebraLine;
import cube.d.n.commoncore.v2.lines.InputLine;
import cube.d.n.commoncore.v2.lines.Line;

/**
 * Created by Colin_000 on 5/10/2015.
 */
public class Done extends Action {
    public Done(Line owner) {
        super(owner);
    }

    @Override
    protected void privateAct() {
        InputLine line = new InputLine(owner.owner);
        owner.owner.addLine(line);
    }
}
