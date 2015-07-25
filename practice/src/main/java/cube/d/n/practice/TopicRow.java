package cube.d.n.practice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import cube.d.n.commoncore.CircleView;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin on 6/23/2015.
 */
public class TopicRow extends MainRow implements CanUpdatePrecent {

    public  static HashMap<String,TopicRow> topics = new HashMap<>();

    private static int id =0;

    public final int myId = id++;
    public final String shortName;

    public final Equation equation;
    private ArrayList<ProblemRow> problems;

    public TopicRow(String line) {
        super(getTitle(line), getSubtitle(line));
        topics.put(getTitle(line),this);

        Log.d("line is", line);

        String[] split = line.split("\t");
        shortName = split[1];

        if (split.length>2) {
            if (split[2].startsWith("\"(")) {
                Log.d("wut", split[2]);
                String[] eqSplit = split[2].substring(3, split[2].length() - 3).split(",");
                this.equation = Util.stringEquation(eqSplit);
            } else {
                this.equation = null;
            }
        }else {
            this.equation = null;
        }

    }

    private static String getSubtitle(String line) {
        String[] split = line.split("\t");

        String about ="";

        if (split.length>2) {
            if (split[2].startsWith("\"(")) {
            } else {
                about = split[2];
            }
        }
        return about;
    }

    private static String getTitle(String line) {
        String[] split = line.split("\t");


        return split[0];
    }

    public ArrayList<ProblemRow> getProblems() {
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

    public ProblemArrayAdapter getAdapter(Context context,ListView parent,Runnable callBack){
        return new ProblemArrayAdapter(context,getProbs(),parent,callBack);
    }

    public ProblemArrayAdapter getAdapter(Context context,ListView parent){
        return new ProblemArrayAdapter(context,getProbs(),parent);
    }


    public ArrayList<Row> getProbs(){
       return Utilz.asRowList(getProblems());
    }


    @Override
    public void go(Activity that) {
        Intent intent = new Intent(that,ChooseProblem.class);
        //based on item add info to intent
        intent.putExtra("topic",title);
        that.startActivity(intent);
    }

    @Override
    public void updatePrecent() {
        // sometimes we have not vet inflated the row
        // in perticular this happens when you first create the activity
        // since onresume is called before the views are made
        // it is ok becuase precent is updated when we make the view
        if (rowView != null) {
            CircleView cir = (CircleView) rowView.findViewById(R.id.topic_circle);
            float pcnt = getPrecent();
            cir.circleDrawer.setPrecent(pcnt);
            if (pcnt==1f){
                cir.circleDrawer.setSubText("COMPLETE");
            }else if (pcnt != 0f){
                cir.circleDrawer.setSubText((int)(pcnt*100)+"%");
            }
        }
    }

    public float getPrecent() {
        int right = 0;
        for (ProblemRow pr: getProblems()){
            if (pr.myProblem.getSolved()){
                right++;
            }
        }
        return ((float)right)/((float)problems.size());
    }
}
