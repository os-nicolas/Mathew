package cube.d.n.practice;

import android.content.Context;
import android.graphics.Typeface;
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

    final public String name;
    public Equation equation;
    public final int myId;

    public ProblemRow(String name){
        this.name = name;
        this.equation = new VarEquation(name,new NullLine());
        myId= IDcount++;
        problems.put(myId,this);
    }

    public ProblemRow(Equation equation){
        this.equation = equation;
       setFont( Mathilda.getMathilda().getDJV());
        setColor(0xff888888);
        this.name="";
        myId= IDcount++;

        problems.put(myId,this);
    }

    public void setFont(Typeface dj){
        equation.getPaint().setTypeface(dj);
    }

    public void setColor(int color){
        equation.setColor(color);
    }
}
