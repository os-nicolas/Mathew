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


    public final Problem myProblem;
    public final String title;


    public ProblemRow(String line){
        myProblem = new Problem(line);
        title = "";
        myProblem.setRow(this);
    }

    public ProblemRow(String name,String circleText){
        myProblem =null;
        setCircleText(circleText);
        title = name;
    }


    private  String circleText="";
    public String getCircleText() {
        if (!"".equals(circleText)){
            return circleText;
        }else if (myProblem!=null){
            int p =myProblem.myId+1;
            return (p<10?"0":"")+p;
        }else{
            return "";
        }
    }
    public void setCircleText(String circleText){
        this.circleText = circleText;
    }
    public ProblemRow withCircleText(String circleText){
        setCircleText(circleText);
        return this;
    }
}
