package dash.dev.mathilda.simple;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.InputLineEnum;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.Line;

/**
 * Created by Colin_000 on 5/22/2015.
 */
public class Mathilda extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public String getPropertyId() {
        return "UA-59613283-1";
    }

    @Override
    public Action getEnter(InputLine line) {
        return new EnterAction(line);
    }

    @Override
    public Action getDone(Line line) {
        return null;
    }

    @Override
    public Action getOk(BothSidesLine line) {
        return null;
    }

    @Override
    public Action getCancel(BothSidesLine line) {
        return null;
    }

    private static HashMap<String, View> views = new HashMap<>();

    public static void setMain(String mainActivity, Main main) {

        views.put(mainActivity, main);
    }

    public static View getView(String mainActivity, Activity activity) {
        View result;
        if (views.containsKey(mainActivity)) {
            result = views.get(mainActivity);
            if (result.getParent() != null) {
                ((ViewGroup) result.getParent()).removeView(result);
            }
        } else {
            result = new Main(activity, InputLineEnum.CALC);
            views.put(mainActivity, result);
        }
        return result;
    }
}
