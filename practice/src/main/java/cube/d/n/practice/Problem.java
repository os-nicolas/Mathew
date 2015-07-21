package cube.d.n.practice;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin_000 on 7/1/2015.
 */
public class Problem {

    private static final String PREFS_NAME = "SOLVED";
    public  static HashMap<Integer,Problem> problems = new HashMap<>();

    public final int myId;

    public final String topic;
    public final String name;
    public final String text;
    public final Equation equation;
    public final Equation solution;
    public final boolean input;
    private ProblemRow row;
    public View view;

    public Problem(String line) {
        super();


        Log.d("Equation Line", line);

        String[] split = line.split("\t");
        myId = Integer.parseInt(split[0]);

        problems.put(myId,this);
        this.topic = split[1];
        this.name = split[2];
        this.text =split[3];
        if (!split[5].equals("")){
            String[] eqSplit = split[5].substring(3,split[5].length()-3).split(",");
            this.equation = Util.stringEquation(eqSplit);
            //setFont( Mathilda.getMathilda().getDJV());
            //setColor(0xff888888);

        }else{
            this.equation = null;
        }
        this.input = split[6].equals("yes");
        if (!split[8].equals("")){
            String[] eqSplit = split[8].substring(3,split[8].length()-3).split(",");
            this.solution = Util.stringEquation(eqSplit);
            //setFont( Mathilda.getMathilda().getDJV());
            //setColor(0xff888888);
        }else{
            Log.e("solutions should not be null!","wikeis!");
            this.solution = null;
        }

        Log.d("Problem","topic "+topic + " name: " + name + " text: "+ text + " equation: "+ equation + " input: "+ input  + " solution: " + solution);
     }

    public void setFont(Typeface dj){
        equation.getPaint().setTypeface(dj);
    }

    public void setColor(int color){
        equation.setColor(color);
    }

    public ProblemRow getRow() {
        return row;
    }

    public void setRow(ProblemRow row) {
        this.row=row;
    }

    public boolean getSolved(){
        SharedPreferences settings = BaseApp.getApp().getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(myId+"", false);
    }

    public void setSolved(boolean solved){
        SharedPreferences settings = BaseApp.getApp().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(myId+"", solved);
        editor.commit();
    }

    public Problem next() {
        ArrayList<ProblemRow> siblins = TopicRow.topics.get(topic).getProblems();
        boolean next = false;
        for (ProblemRow pr: siblins){
            if (next){
                return pr.myProblem;
            }
            if (pr.myProblem.equals(this)){
                next = true;
            }
        }
        return null;
    }

    public int getIndex() {
        ArrayList<ProblemRow> siblins = TopicRow.topics.get(topic).getProblems();
        for (int i =0;i< siblins.size();i++){
            if (siblins.get(i).myProblem.equals(this)){
                return i+1;
            }
        }
        return -1;
    }

    public TopicRow getTopic() {
        return TopicRow.topics.get(topic);
    }
}
