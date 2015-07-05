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
        View rowView = problems.get(i).makeView(context,parent,i);
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
