package cube.d.n.practice;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import cube.d.n.commoncore.eq.any.AddEquation;
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
    public final ArrayList<ProblemRow> problems = new ArrayList<>();

    public TopicRow(String name, String about){
        super();
        topics.put(id,this);


        this.name = name;
        this.about = about;

        Equation eq = new AddEquation(new NullLine());
        eq.add(NumConstEquation.create(5,new NullLine()));
        eq.add(NumConstEquation.create(150,new NullLine()));
        problems.add(new ProblemRow(eq));
        problems.add(new ProblemRow("12"));
        problems.add(new ProblemRow("123"));
        problems.add(new ProblemRow("1234"));
        problems.add(new ProblemRow("1adf adsf"));
        problems.add(new ProblemRow("1adsf asdf"));
        problems.add(new ProblemRow("1adsf asdfas "));
        problems.add(new ProblemRow("1fasdf sad"));
        problems.add(new ProblemRow("1asdf asd sdf a "));
        problems.add(new ProblemRow("123"));
        problems.add(new ProblemRow("1234"));
        problems.add(new ProblemRow("1adf adsf"));
        problems.add(new ProblemRow("1adsf asdf"));
        problems.add(new ProblemRow("1adsf asdfas "));
        problems.add(new ProblemRow("1fasdf sad"));
        problems.add(new ProblemRow("1asdf asd sdf a "));
        problems.add(new ProblemRow("123"));
        problems.add(new ProblemRow("1234"));
        problems.add(new ProblemRow("1adf adsf"));
        problems.add(new ProblemRow("1adsf asdf"));
        problems.add(new ProblemRow("1adsf asdfas "));
        problems.add(new ProblemRow("1fasdf sad"));
        problems.add(new ProblemRow("1asdf asd sdf a "));
        problems.add(new ProblemRow("123"));
        problems.add(new ProblemRow("1234"));
        problems.add(new ProblemRow("1adf adsf"));
        problems.add(new ProblemRow("1adsf asdf"));
        problems.add(new ProblemRow("1adsf asdfas "));
        problems.add(new ProblemRow("1fasdf sad"));
        problems.add(new ProblemRow("1asdf asd sdf a "));



    }

    public ProblemArrayAdapter getAdapter(Context context,ListView parent){
        return new ProblemArrayAdapter(context,problems,parent);
    }
}
