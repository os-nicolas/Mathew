package colin.example.algebrator;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.Random;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.BaseView;
import cube.d.n.commoncore.eq.Equation;

/**
 * Created by Colin on 12/30/2014.
 */
public class Algebrator extends BaseApp {

    public EmilyView writeView  = null;
    public BothSidesView addBothView = null;
    public ColinView solveView  = null;

    public String getPropertyId(){
        return "UA-59613283-1";
    }

    public static Algebrator getAlgebrator(){
        return (Algebrator) BaseApp.getApp();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
