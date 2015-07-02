package cube.d.n.practice;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import cube.d.n.commoncore.EquationView;

/**
 * Created by Colin on 6/23/2015.
 */
public class ProblemArrayAdapter extends ArrayAdapter<ProblemRow> {

    private final Context context;
    private final ArrayList<ProblemRow> problems;
    public ConcurrentHashMap<Integer, View> views = new ConcurrentHashMap<>();

    public ProblemArrayAdapter(Context context, ArrayList<ProblemRow> itemsArrayList, final ViewGroup parent) {

        super(context, R.layout.topic_row, itemsArrayList);

        this.context = context;
        this.problems = itemsArrayList;

        Thread th = new Thread() {
            public void run() {
                for (int i = 0; i < problems.size(); i++) {
                    makeView(i, parent);
                }
            }
        };
        th.start();
    }

    private View makeView(int i, ViewGroup parent) {
        ProblemRow pr = problems.get(i);
        View rowView;

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (pr.myProblem != null) {
            if (pr.myProblem.equation != null) {


                // 2. Get rowView from inflater
                rowView = inflater.inflate(R.layout.problem_eq_row, parent, false);

                EquationView equationView = (EquationView) rowView.findViewById(R.id.problem_eq_view);
                equationView.setEquation(pr.myProblem.equation);


            } else {
                // 2. Get rowView from inflater
                rowView = inflater.inflate(R.layout.problem_row, parent, false);

                // 3. Get the two text view from the rowView
                TextView title = (TextView) rowView.findViewById(R.id.problem_name);

                Typeface dj = Typeface.createFromAsset(context.getAssets(),
                        "fonts/DejaVuSans-ExtraLight.ttf");
                title.setTypeface(dj);
                title.setText(pr.myProblem.name);

            }
        } else {
            // 2. Get rowView from inflater
            rowView = inflater.inflate(R.layout.problem_row, parent, false);

            // 3. Get the two text view from the rowView
            TextView title = (TextView) rowView.findViewById(R.id.problem_name);

            Typeface dj = Typeface.createFromAsset(context.getAssets(),
                    "fonts/DejaVuSans-ExtraLight.ttf");
            title.setTypeface(dj);
            title.setText(pr.title);
        }


        CircleView cir = (CircleView) rowView.findViewById(R.id.problem_circle);
        int p = i + 1;
        cir.setColors(pr.getCircleText(), CircleView.getBkgColor(p), CircleView.getTextColor(p));

        views.put(i, rowView);
        return rowView;
    }

    public ProblemRow getProblem(int pos){
        return problems.get(pos);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (views.containsKey(position)) {
            return views.get(position);
        } else {
            return makeView(position, parent);
        }
    }
}
