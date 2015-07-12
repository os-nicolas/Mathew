package cube.d.n.practice;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;

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




    private  static int IDcount = 0;
    public final int myId = IDcount++;

    public final String topic;
    public final String name;
    public final String text;
    public final Equation equation;
    public final Equation solution;
    public final boolean input;
    private ProblemRow row;

    public Problem(String line) {
        super();
        problems.put(myId,this);

        String[] split = line.split("\t");

        this.topic = split[0];
        this.name = split[1];
        this.text =split[2];
        if (!split[3].equals("")){
            String[] eqSplit = split[3].split(",");
            this.equation = Util.stringEquation(eqSplit);
            //setFont( Mathilda.getMathilda().getDJV());
            //setColor(0xff888888);

        }else{
            this.equation = null;
        }
        this.input = split[4].equals("yes");
        if (!split[5].equals("")){
            String[] eqSplit = split[5].split(",");
            this.solution = Util.stringEquation(eqSplit);
            //setFont( Mathilda.getMathilda().getDJV());
            //setColor(0xff888888);
        }else{
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
        return settings.getBoolean(name, false);
    }

    public void setSolved(boolean solved){
        SharedPreferences settings = BaseApp.getApp().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, solved);
        editor.commit();
    }
}
