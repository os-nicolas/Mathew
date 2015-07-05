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
public class ProblemRow implements Row {


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

    @Override
    public View makeView(Context context,ViewGroup parent,int i) {
        View rowView;

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (myProblem != null) {
            if (myProblem.equation != null) {


                // 2. Get rowView from inflater
                rowView = inflater.inflate(R.layout.problem_eq_row, parent, false);

                EquationView equationView = (EquationView) rowView.findViewById(R.id.problem_eq_view);
                equationView.setEquation(myProblem.equation);


            } else {
                // 2. Get rowView from inflater
                rowView = inflater.inflate(R.layout.problem_row, parent, false);

                // 3. Get the two text view from the rowView
                TextView title = (TextView) rowView.findViewById(R.id.problem_name);

                Typeface dj = Typeface.createFromAsset(context.getAssets(),
                        "fonts/DejaVuSans-ExtraLight.ttf");
                title.setTypeface(dj);
                title.setText(myProblem.name);

            }
        } else {
            // 2. Get rowView from inflater
            rowView = inflater.inflate(R.layout.problem_row, parent, false);

            // 3. Get the two text view from the rowView
            TextView title = (TextView) rowView.findViewById(R.id.problem_name);

            Typeface dj = Typeface.createFromAsset(context.getAssets(),
                    "fonts/DejaVuSans-ExtraLight.ttf");
            title.setTypeface(dj);
            title.setText(this.title);
        }


        CircleView cir = (CircleView) rowView.findViewById(R.id.problem_circle);
        int p = i + 1;
        cir.setColors(getCircleText(), CircleView.getBkgColor(p), CircleView.getTextColor(p));

        return rowView;
    }
}
