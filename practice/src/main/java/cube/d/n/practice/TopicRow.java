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

    public final Equation equation;
    private ArrayList<ProblemRow> problems;

    public TopicRow(String line) {
        super(getTitle(line), getSubtitle(line));
        topics.put(getTitle(line),this);

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

    public ProblemArrayAdapter getAdapter(Context context,ListView parent){
        return new ProblemArrayAdapter(context,Utilz.asRowList(getProblems()),parent);
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
            cir.circleDrawer.setPrecent(getPrecent());
            if (getPrecent()==1){
                cir.circleDrawer.setSubText("COMPLETE");
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
