package colin.example.algebrator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;

import cube.d.n.commoncore.eq.Equation;
import cube.d.n.commoncore.eq.PlaceholderEquation;

/**
 * Created by Colin on 2/6/2015.
 */
public class WriteScreen
        extends SuperScreen {

    public static final String PREFS_NAME = "tuts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = Algebrator.getAlgebrator().getSharedPreferences(PREFS_NAME, 0);
        boolean alreadyShown = settings.getBoolean("startup_tut", false);
        final Activity that = this;
        if (!alreadyShown) {
            Intent myIntent = new Intent(that, TutActivity.class);
            that.finish();
            that.startActivity(myIntent);
        }
        super.onCreate(savedInstanceState);
        ScreenName = "WriteScreen";
    }

    @Override
    protected void onResume(){
        super.onResume();

        if (Algebrator.getAlgebrator().writeView == null) {
            myView = new EmilyView(this);
            Algebrator.getAlgebrator().writeView = (EmilyView)myView;
        }else{
            myView = Algebrator.getAlgebrator().writeView;
            if(myView.getParent() != null) {
                ((ViewGroup) myView.getParent()).removeView(myView);
            }
        }
        myView.disabled = false;

        lookAt(myView);
    }
}
