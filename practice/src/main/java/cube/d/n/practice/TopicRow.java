package cube.d.n.practice;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.lines.NullLine;

/**
 * Created by Colin on 6/23/2015.
 */
public class TopicRow  {

    public  static HashMap<Integer,TopicRow> topics = new HashMap<>();

    private static int id =0;

    public final int myId = id++;

    public final String name;
    public final String about;
    public final Equation equation;
    private ArrayList<ProblemRow> problems;

    public TopicRow(String line) {
        super();
        topics.put(myId,this);
        String[] split = line.split("\t");


        this.name = split[0];
        if (split[1].startsWith("(")){
            Log.d("wut", split[1]);
            String[] eqSplit = split[1].substring(2,split[1].length()-2).split(",");


            this.equation = Util.stringEquation(eqSplit);
            this.about ="";
        }else {
            this.about = split[1];
            this.equation = null;
        }
    }

    private ArrayList<ProblemRow> getProblems() {
        if (problems == null) {
            ArrayList<ProblemRow> probs = new ArrayList<>();
            ArrayList<ProblemRow> allProbs = Mathilda.getMathilda().problems;

            for (ProblemRow prob : allProbs) {
                if (prob.topic.equals(name)) {
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
}
