package cube.d.n.practice;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Colin on 6/23/2015.
 */
public class TopicRow  {

    public  static HashMap<String,TopicRow> topics = new HashMap<>();

    public final String name;
    public final String about;
    public final ArrayList<ProblemRow> problems = new ArrayList<>();

    public TopicRow(String name, String about){
        super();
        this.name = name;
        this.about = about;

        problems.add(new ProblemRow("1"));
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

    public ProblemArrayAdapter getAdapter(Context context){
        return new ProblemArrayAdapter(context,problems);
    }
}
