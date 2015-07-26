package dash.dev.mathilda.simple;

import android.app.Activity;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import cube.d.n.commoncore.BaseApp;

/**
 * Created by Colin_000 on 5/22/2015.
 */
public class Landing extends Activity {
    public static final String screenName = "Calc";

    private static WeakReference<Landing> instance=new WeakReference<Landing>(null);

    public static Landing getInstance(){
        return instance.get();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance= new WeakReference<Landing>(this);
        setContentView(Mathilda.getView(screenName,this));
    }


    @Override
    protected void onResume(){
        super.onResume();
        BaseApp.getApp().recordScreen("main");
    }
}
