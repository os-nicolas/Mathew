package cube.d.n.practice;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.Circle;

import java.util.HashMap;

import cube.d.n.commoncore.EquationView;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.VarEquation;
import cube.d.n.commoncore.lines.NullLine;

/**
 * Created by Colin on 6/23/2015.
 */
public class ProblemRow {

    public  static HashMap<Integer,ProblemRow> problems = new HashMap<>();

    private  static int IDcount = 0;
    public final int myId = IDcount++;

    public final String topic;
    public final String name;
    public final String text;
    public final Equation equation;
    public final boolean input;

    public ProblemRow(String line) {
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

    }


    public void setFont(Typeface dj){
        equation.getPaint().setTypeface(dj);
    }

    public void setColor(int color){
        equation.setColor(color);
    }
}
