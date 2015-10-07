package dash.dev.mathilda.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.InputLineEnum;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.ModeController;
import cube.d.n.commoncore.lines.BothSidesLine;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.Action.helper.HelperCancelAction;
import cube.d.n.commoncore.Action.helper.HelperOkAction;
import cube.d.n.commoncore.Action.helper.HelperDoneAction;
import cube.d.n.commoncore.Action.helper.HelperEnterAction;
import dash.dev.mathilda.helper.tuts.HelperModeController;

/**
 * Created by Colin_000 on 5/20/2015.
 */
public class Mathilda extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public String about(){
        return "Algebra Helper \n" + super.about();
    }

    @Override
    public String getPropertyId(){
        return "UA-59613283-1";
    }

    private static HashMap<String,View> views = new HashMap<>();

    public static void setMain(String mainActivity,Main main) {
            views.put(mainActivity, main);
    }

    public static View getAndRemoveView(String mainActivity,Activity activity) {
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

    public static View justGetView(String mainActivity,Activity activity) {
        View result;
        if (views.containsKey(mainActivity)){
            result= views.get(mainActivity);
        }else {
            result = new Main(activity);
            views.put(mainActivity, result);
        }
        return result;
    }


    @Override
    public void stupidLittleBackDoor(Main main, Context context){
        Mathilda.setMain(ColinAct.screenName,main);

        Intent myIntent = new Intent(context, ColinAct.class);
        context.startActivity(myIntent);
    }

    @Override
    public ModeController getModeController(InputLineEnum startLine) {
        return new HelperModeController();
    }

//    public boolean includeClear() {
//        return true;
//    }
//
//    @Override
//    public boolean bothSidesPopUps() {
//        return false;
//    }
}
