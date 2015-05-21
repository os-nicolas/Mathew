package cube.d.n.calc;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cube.d.n.calc.Actions.BothSides.CancelAction;
import cube.d.n.calc.Actions.BothSides.CheckAction;
import cube.d.n.calc.Actions.SolveScreen.Done;
import cube.d.n.calc.Actions.WriteScreen.Solve;
import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.Line;

/**
 * Created by Colin_000 on 5/9/2015.
 */
public class Mathilda extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();Log.i("Mathilda", "created");
    }

    @Override
    public String getPropertyId(){
        return "UA-59613283-3";
    }

    @Override
    public Action getEnter(InputLine line) {
        return new Solve(line);
    }

    @Override
    public Action getDone(Line line) {
        return new Done(line);
    }

    @Override
    public Action getOk(BothSidesLine line) {
        return new CheckAction(line);
    }

    @Override
    public Action getCancel(BothSidesLine line) {
        return new CancelAction(line);
    }

    private static HashMap<String,View> views = new HashMap<>();
    public static View getView(String mainActivity,Activity activity) {
        View result;
        if (views.containsKey(mainActivity)){
            result= views.get(mainActivity);
            if(result.getParent() != null) {
                ((ViewGroup) result.getParent()).removeView(result);
            }
        }else {
            result = new Main(activity);
            views.put(mainActivity, result);
        }
        return result;
    }
}
