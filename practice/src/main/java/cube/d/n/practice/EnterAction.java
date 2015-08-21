package cube.d.n.practice;

import android.util.Log;

import cube.d.n.commoncore.Action.WriteScreen.Solve;
import cube.d.n.commoncore.GS;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 8/20/2015.
 */
public class EnterAction extends Solve {
    public EnterAction(InputLine emilyView) {
        super(emilyView);
    }

    @Override
    protected boolean failsAdditionalConditions(Equation mine) {
        return countEquals(mine) == 0 || !hasA(mine);
    }

    @Override
    protected boolean passes(Equation inputCopy) {
        Equation goal= owner.owner.getGoal();
        VarEquation var;
        Equation subWith;
        if (goal.get(0) instanceof VarEquation){
            var = (VarEquation)goal.get(0);
            subWith = goal.get(1);
        }else{
            var = (VarEquation)goal.get(1);
            subWith = goal.get(0);
        }

        GS<Equation> gsLeft = new GS<>(Util.sub(inputCopy.get(0),var,subWith));
        gsLeft.get().parent = null;
        gsLeft.get().addWatcher(gsLeft);
        Util.reduce(gsLeft);
        Log.d("EnterAction.passes","reduces left to: " + gsLeft.get().toString());
        GS<Equation> gsRight = new GS<>(Util.sub(inputCopy.get(1),var,subWith));
        gsRight.get().parent = null;
        gsRight.get().addWatcher(gsRight);
        Util.reduce(gsRight);
        Log.d("EnterAction.passes","reduces right to: " + gsRight.get().toString());

        return gsLeft.get().same(gsRight.get());
    }

    @Override
    protected void planB() {
        Log.d("EnterAction.planB","not a good equation");
    }

    public static boolean hasA(Equation stupid) {
        int count = 0;
        for (Equation e : stupid) {
            if (e instanceof VarEquation && e.getDisplay(-1).equals("a")) {
                return true;
            }
        }
        return false;
    }
}
