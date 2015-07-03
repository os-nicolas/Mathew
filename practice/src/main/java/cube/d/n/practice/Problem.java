package cube.d.n.practice;

import android.graphics.Typeface;

import java.util.HashMap;

import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin_000 on 7/1/2015.
 */
public class Problem {

    public  static HashMap<Integer,Problem> problems = new HashMap<>();

    private  static int IDcount = 0;
    public final int myId = IDcount++;

    public final String topic;
    public final String name;
    public final String text;
    public final Equation equation;
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
            String[] eqSplit = split[3].substring(1,split[3].length()-1).split(",");
            this.equation = Util.stringEquation(eqSplit);
            setFont( Mathilda.getMathilda().getDJV());
            setColor(0xff888888);

        }else{
            this.equation = null;
        }
        this.input = split[4].equals("yes");


        new Foo().bar.innerCall();
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
}
