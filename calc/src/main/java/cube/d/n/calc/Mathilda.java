package cube.d.n.calc;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cube.d.n.commoncore.*;

/**
 * Created by Colin_000 on 5/9/2015.
 */
public class Mathilda extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();Log.i("Mathilda", "created");
    }

    @Override
    public String about(){
        return "Algebra Calculator \n" + super.about();
    }

    @Override
    public boolean bothSidesPopUps() {
        return false;
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

    public String getMultiSymbol() {
        return "*";
    }

    public String getVar1() {
        return "x";
    }

    public String getVar2() {
        return "y";
    }
}
