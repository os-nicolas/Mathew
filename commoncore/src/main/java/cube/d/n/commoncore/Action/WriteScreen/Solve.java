package cube.d.n.commoncore.Action.WriteScreen;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.v2.InputLine;


public class Solve extends Action {

    public Solve(InputLine emilyView) {
        super(emilyView);
    }

    public static Equation mine;

    @Override
    public boolean canAct() {
        return true;
    }

    @Override
    protected void privateAct() {
        Log.i("solve", "gogo!");

    }
}