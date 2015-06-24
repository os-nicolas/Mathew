package cube.d.n.practice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Colin on 6/23/2015.
 */
public class ProblemArrayAdapter extends ArrayAdapter<ProblemRow> {

    private final Context context;
    private final ArrayList<ProblemRow> problems;

    public ProblemArrayAdapter(Context context, ArrayList<ProblemRow> itemsArrayList) {

        super(context, R.layout.topic_row, itemsArrayList);

        this.context = context;
        this.problems = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 5. retrn rowView
        return  problems.get(position).getView(context,convertView,parent);
    }
}
