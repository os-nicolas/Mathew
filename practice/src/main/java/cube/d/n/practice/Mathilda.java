package cube.d.n.practice;

import android.app.Activity;
import android.graphics.Typeface;
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

    public Typeface getDJV(){
        Typeface dj = Typeface.createFromAsset(this.getAssets(),
                "fonts/DejaVuSans.ttf");
        return dj;
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
            while ((line = reader.readLine()) != null){
                topics.add(new TopicRow(line));
            }
            is.close();
        }
        catch (IOException ex) {
            Log.e("error loading file",ex.toString());
        }

        return topics;
    }

    private ArrayList<ProblemRow> initProblems() {

        ArrayList<ProblemRow> problemRows = new ArrayList<ProblemRow>();
        try {
            InputStream is = getAssets().open("problems.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine(); // the first line is the table header so we skip it
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
}
