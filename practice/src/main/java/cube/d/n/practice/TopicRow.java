package cube.d.n.practice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin on 6/23/2015.
 */
public class TopicRow extends MainRow {

    public  static HashMap<Integer,TopicRow> topics = new HashMap<>();

    private static int id =0;

    public final int myId = id++;

    public final Equation equation;
    private ArrayList<ProblemRow> problems;

    public TopicRow(String line) {
        super(getTitle(line), getSubtitle(line));
        topics.put(myId,this);

        String[] split = line.split("\t");

        if (split[1].startsWith("(")){
            Log.d("wut", split[1]);
            String[] eqSplit = split[1].substring(2,split[1].length()-2).split(",");
            this.equation = Util.stringEquation(eqSplit);
        }else {
            this.equation = null;
        }

    }

    private static String getSubtitle(String line) {
        String[] split = line.split("\t");

        String about ="";
        if (split[1].startsWith("(")){
        }else {
            about = split[1];
        }
        return about;
    }

    private static String getTitle(String line) {
        String[] split = line.split("\t");


        return split[0];
    }

    private ArrayList<ProblemRow> getProblems() {
        if (problems == null) {
            ArrayList<ProblemRow> probs = new ArrayList<>();
            ArrayList<ProblemRow> allProbs = Mathilda.getMathilda().problems;

            for (ProblemRow prob : allProbs) {
                if (prob.myProblem.topic.equals(title)) {
                    probs.add(prob);
                }
            }
            problems = probs;
        }
        return problems;


    }

    public ProblemArrayAdapter getAdapter(Context context,ListView parent){
        return new ProblemArrayAdapter(context,getProblems(),parent);
    }

    @Override
    public void go(Activity that) {
        Intent intent = new Intent(that,ChooseProblem.class);
        //based on item add info to intent
        intent.putExtra("topic",myId);
        that.startActivity(intent);
    }
}
