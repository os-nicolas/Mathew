package dash.dev.mathilda.helper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.ref.WeakReference;

import cube.d.n.commoncore.BaseApp;
import dash.dev.mathilda.helper.tuts.TutActivity;


public class EmilyAct extends Activity {

    private static final String PREFS_NAME = "tuts";
    public static final String screenName = "EmilyAct";

    //public static boolean hasShown = false;

    private static WeakReference<EmilyAct> instance=new WeakReference<EmilyAct>(null);

    public static EmilyAct getInstance(){
        return instance.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance =new WeakReference<EmilyAct>(this);

        SharedPreferences settings = Mathilda.getApp().getSharedPreferences(PREFS_NAME, 0);
        boolean alreadyShown = settings.getBoolean("write", false) ;
        final Activity that = this;
        if (!alreadyShown) {
            Intent myIntent = new Intent(that, TutActivity.class);
            //hasShown = true;
            that.finish();
            that.startActivity(myIntent);
        }

        setContentView(Mathilda.getView(screenName,this));
    }

    @Override
    public void finish(){
        instance = null;
        super.finish();
    }
}
