package dash.dev.mathilda.helper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.ref.WeakReference;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.lines.Line;
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
        boolean alreadyShown = settings.getBoolean("write", false);
        final Activity that = this;
        if (!alreadyShown) {
            Intent myIntent = new Intent(that, TutActivity.class);
            //hasShown = true;
            that.finish();
            that.startActivity(myIntent);
        }
        setContentView(Mathilda.getAndRemoveView(screenName,this));
    }

    @Override
    protected void onResume(){
        super.onResume();
       View myView = Mathilda.justGetView(screenName, this);
       if (myView instanceof  Main ){
            for (int i=0;i < ((Main) myView).getLinesSize();i++){
                Equation st = ((Main) myView).getLine(i).stupid.get();
                if (st != null) {
                    st.deepNeedsUpdate();
                }
            }

       }
    }

    @Override
    public void finish(){
        instance = null;
        super.finish();
    }
}
