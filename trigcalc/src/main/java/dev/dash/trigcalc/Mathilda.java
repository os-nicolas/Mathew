package dev.dash.trigcalc;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.InputLineEnum;
import cube.d.n.commoncore.Main;

/**
 * Created by Colin_000 on 8/25/2015.
 */
public class Mathilda extends BaseApp {

        @Override
        public void onCreate() {
            super.onCreate();
            Log.i("Mathilda", "created");
        }

        @Override
        public String about(){
            return "Tig Calculator \n" + super.about();
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
                result = new Main(activity, InputLineEnum.TRIG);
                views.put(mainActivity, result);
            }
            return result;
        }

}
