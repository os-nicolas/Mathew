package cube.d.n.commoncore.Action.SovleScreen;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.v2.lines.AlgebraLine;

/**
 * Created by Colin_000 on 3/28/2015.
 */
public class BothSides extends Action {


    public BothSides(AlgebraLine line) {
        super(line);
    }

    public static Equation mine = null;
    @Override
    protected void privateAct() {

    }

    @Override
    public boolean canAct() {
        return true;
    }
}
