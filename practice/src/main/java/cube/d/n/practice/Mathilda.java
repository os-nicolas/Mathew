package cube.d.n.practice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;

/**
 * Created by Colin_000 on 5/9/2015.
 */
public class Mathilda extends BaseApp {

    ArrayList<TopicRow> topics;
    ArrayList<ProblemRow> problems;

    @Override
    public void onCreate() {
        super.onCreate();Log.i("Mathilda", "created");
        topics = initTopics();
        problems = initProblems();
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

    public static Mathilda getMathilda(){
        return (Mathilda)BaseApp.getApp();
    }



    public ArrayList<TopicRow> getTopics(){
        return topics;
    }


    private ArrayList<TopicRow> initTopics() {

        ArrayList<TopicRow> topics = new ArrayList<TopicRow>();
        try {
            InputStream is = getAssets().open("testTopics.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine(); // the first line is the table header so we skip it
            line = reader.readLine(); // the second line is descriptions we skip it too
            while ((line = reader.readLine()) != null){
                if (!line.trim().equals("")) {
                    topics.add(new TopicRow(line));
                }
            }
            is.close();
        }
        catch (IOException ex) {
            Log.e("error loading file",ex.toString());
        }

        return topics;
    }

    public boolean hasB() {
        return false;
    }

    private ArrayList<ProblemRow> initProblems() {

        ArrayList<ProblemRow> problemRows = new ArrayList<ProblemRow>();
        try {
            InputStream is = getAssets().open("problems.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine(); // the first line is the table header so we skip it
            line = reader.readLine(); // the first line is the table header so we skip it
            while ((line = reader.readLine()) != null){
                problemRows.add(new ProblemRow(line));
            }
            is.close();
        }
        catch (IOException ex) {
            Log.e("error loading file",ex.toString());
        }

        return problemRows;
    }

    final static String _PREF = "misk";
    final static String TUTS_PREF_KEY = "tutsComplete";
    public static boolean hasCompletedTut() {
        SharedPreferences settings = BaseApp.getApp().getSharedPreferences(_PREF, 0);
        return settings.getBoolean(TUTS_PREF_KEY, false);
    }
    public static void completedTut() {
        SharedPreferences settings = BaseApp.getApp().getSharedPreferences(_PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(TUTS_PREF_KEY, true);
        editor.commit();
    }

    final static String SUPRT_PREF_KEY = "supporter";
    public boolean hasSupported() {
        SharedPreferences settings = BaseApp.getApp().getSharedPreferences(_PREF, 0);
        return settings.getBoolean(SUPRT_PREF_KEY, false);
    }

    public void supporter() {
        SharedPreferences settings = BaseApp.getApp().getSharedPreferences(_PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SUPRT_PREF_KEY, true);
        editor.commit();
    }

}
