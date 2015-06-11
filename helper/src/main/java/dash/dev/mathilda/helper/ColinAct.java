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


public class ColinAct extends Activity {

    public static final String screenName = "ColinAct";

    private static WeakReference<ColinAct> instance=new WeakReference<ColinAct>(null);

    public static ColinAct getInstance(){
        return instance.get();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance= new WeakReference<ColinAct>(this);
        setContentView(Mathilda.getAndRemoveView(screenName,this));
    }
}
