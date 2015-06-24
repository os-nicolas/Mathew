package cube.d.n.practice;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.Circle;

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


    public View getView(int position, Context context, View convertView, ViewGroup parent) {
        if (convertView == null){
            if (this.equation != null){
                // 1. Create inflater
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // 2. Get rowView from inflater
                View rowView = inflater.inflate(R.layout.problem_eq_row, parent, false);

                EquationView equationView = (EquationView) rowView.findViewById(R.id.problem_eq_view);
                equationView.setEquation(this.equation);

                CircleView cir = (CircleView) rowView.findViewById(R.id.problem_circle);
                cir.setColors(position+"",CircleView.getBkgColor(position),CircleView.getTextColor(position));

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
                title.setText(name);


                CircleView cir = (CircleView) rowView.findViewById(R.id.problem_circle);

                cir.setColors(position+"",CircleView.getBkgColor(position),CircleView.getTextColor(position));

                // 4. Set the text for textView


                convertView= rowView;
            }

        }
        return convertView;
    }
}
