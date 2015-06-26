package cube.d.n.calc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cube.d.n.commoncore.Action.BothSides.CancelAction;
import cube.d.n.commoncore.Action.BothSides.CheckAction;
import cube.d.n.commoncore.Action.SolveScreen.Done;
import cube.d.n.commoncore.Action.WriteScreen.Solve;
import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.keyboards.AlgebraKeyboard;
import cube.d.n.commoncore.keyboards.KeyBoard;
import cube.d.n.commoncore.keyboards.ReturnKeyBoard;
import cube.d.n.commoncore.lines.AlgebraLine;
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
