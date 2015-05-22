package dash.dev.mathilda.helper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.Line;
import dash.dev.mathilda.helper.Actions.BothSides.CancelAction;
import dash.dev.mathilda.helper.Actions.BothSides.OkAction;
import dash.dev.mathilda.helper.Actions.Solve.DoneAction;
import dash.dev.mathilda.helper.Actions.Write.EnterAction;

/**
 * Created by Colin_000 on 5/20/2015.
 */
public class Mathilda extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public String getPropertyId(){
        return "UA-59613283-1";
    }

    @Override
    public Action getEnter(InputLine line) {
        return new EnterAction(line);
    }

    @Override
    public Action getDone(Line line) {
        return new DoneAction(line);
    }

    @Override
    public Action getOk(BothSidesLine line) {
        return new OkAction(line);
    }

    @Override
    public Action getCancel(BothSidesLine line) {
        return new CancelAction(line);
    }

    private static HashMap<String,View> views = new HashMap<>();

    public static void setMain(String mainActivity,Main main) {

            views.put(mainActivity, main);
    }

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

    public boolean includeClear() {
        return true;
    }
}
