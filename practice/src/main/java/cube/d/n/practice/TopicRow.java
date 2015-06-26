package cube.d.n.practice;

import android.content.Context;
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
    public final ArrayList<ProblemRow> problems;

    public TopicRow(String name, Equation equation){
        super();
        topics.put(id,this);


        this.name = name;
        this.equation = equation;
        this.about ="";

        problems = getProblems();
    }

    public TopicRow(String name, String about){
        super();
        topics.put(id,this);


        this.name = name;
        this.about = about;
        this.equation = null;

        problems = getProblems();
    }

    private ArrayList<ProblemRow> getProblems() {
        ArrayList<ProblemRow> probs = new ArrayList<>();
        probs.add(new ProblemRow("12"));
        probs.add(new ProblemRow("123"));
        probs.add(new ProblemRow("1234"));
        probs.add(new ProblemRow("1adf adsf"));
        probs.add(new ProblemRow("1adsf asdf"));
        probs.add(new ProblemRow("1adsf asdfas "));
        probs.add(new ProblemRow("1fasdf sad"));
        probs.add(new ProblemRow("1asdf asd sdf a "));
        probs.add(new ProblemRow("123"));
        probs.add(new ProblemRow("1234"));
        probs.add(new ProblemRow("1adf adsf"));
        probs.add(new ProblemRow("1adsf asdf"));
        probs.add(new ProblemRow("1adsf asdfas "));
        probs.add(new ProblemRow("1fasdf sad"));
        probs.add(new ProblemRow("1asdf asd sdf a "));
        probs.add(new ProblemRow("123"));
        probs.add(new ProblemRow("1234"));
        probs.add(new ProblemRow("1adf adsf"));
        probs.add(new ProblemRow("1adsf asdf"));
        probs.add(new ProblemRow("1adsf asdfas "));
        probs.add(new ProblemRow("1fasdf sad"));
        probs.add(new ProblemRow("1asdf asd sdf a "));
        probs.add(new ProblemRow("123"));
        probs.add(new ProblemRow("1234"));
        probs.add(new ProblemRow("1adf adsf"));
        probs.add(new ProblemRow("1adsf asdf"));
        probs.add(new ProblemRow("1adsf asdfas "));
        probs.add(new ProblemRow("1fasdf sad"));
        probs.add(new ProblemRow("1asdf asd sdf a "));
        return probs;

    }

    public ProblemArrayAdapter getAdapter(Context context,ListView parent){
        return new ProblemArrayAdapter(context,problems,parent);
    }
}
