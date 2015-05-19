package cube.d.n.calc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import cube.d.n.calc.tuts.TutActivity;
import cube.d.n.commoncore.BaseApp;


public class MainActivity extends Activity {

    private static final String PREFS_NAME = "tuts";

    //public static boolean hasShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = BaseApp.getApp().getSharedPreferences(PREFS_NAME, 0);
        boolean alreadyShown = settings.getBoolean("startup_tut", false);// || hasShown;
        final Activity that = this;
        if (!alreadyShown) {
            Intent myIntent = new Intent(that, TutActivity.class);
            //hasShown = true;
            that.finish();
            that.startActivity(myIntent);
        }

        setContentView(Mathilda.getView("mainActivity",this));
    }
}
