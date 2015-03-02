package colin.example.algebrator;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Colin on 2/6/2015.
 */
public class SuperScreen extends Activity{

    SuperView myView;
    String ScreenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lifeCycle", "MainActivity-onCreate");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //super.onConfigurationChanged(newConfig);
        //setContentView(superView);
        Log.i("lifeCycle","MainActivity-onConfigurationChanged");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 16) {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            actionBar.hide();
        }

        // Get tracker.
        Tracker t = Algebrator.getAlgebrator().getTracker();

        // Set screen name.
        t.setScreenName(ScreenName);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

        Log.i("lifeCycle","MainActivity-onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lifeCycle","MainActivity-onResume");
    }


    public void lookAt(SuperView view) {
        view.updateOwner();

        if (view instanceof ColinView && ((ColinView) view).history.size() ==0){
            if (((ColinView) view).history.size()==0) {
                ((ColinView) view).history.add(new EquationButton(view.stupid.copy(),(ColinView)view));
            }
        }

        setContentView(view);
        //setContentView(R.layout.menu);
        view.measureScreen(this);
        myView = view;
    }
}
