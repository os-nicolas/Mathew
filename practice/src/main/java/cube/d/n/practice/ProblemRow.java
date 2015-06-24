package cube.d.n.practice;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cube.d.n.commoncore.EquationView;
import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin on 6/23/2015.
 */
public class ProblemRow {
    final public String name;
    public Equation equation;

    public ProblemRow(String name){
        this.name = name;
    }

    public ProblemRow(Equation equation){
        this.equation = equation;
        this.name="";
    }


    public View getView(Context context, View convertView, ViewGroup parent) {
        if (convertView == null){
            if (this.equation != null){
                // 1. Create inflater
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // 2. Get rowView from inflater
                View rowView = inflater.inflate(R.layout.problem_eq_row, parent, false);

                // 3. Get the two text view from the rowView
                EquationView equationView = (EquationView) rowView.findViewById(R.id.problem_eq_view);

                // 4. Set the text for textView
                equationView.setEquation(this.equation);

                convertView= rowView;

            }else {

                // 1. Create inflater
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // 2. Get rowView from inflater
                View rowView = inflater.inflate(R.layout.problem_row, parent, false);

                // 3. Get the two text view from the rowView
                TextView title = (TextView) rowView.findViewById(R.id.problem_name);

                Typeface dj = Typeface.createFromAsset(context.getAssets(),
                        "fonts/DejaVuSans-ExtraLight.ttf");
                title.setTypeface(dj);

                // 4. Set the text for textView
                title.setText(name);

                convertView= rowView;
            }

        }
        return convertView;
    }
}
