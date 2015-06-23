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
    private final ArrayList<ProblemRow> topics;

    public ProblemArrayAdapter(Context context, ArrayList<ProblemRow> itemsArrayList) {

        super(context, R.layout.topic_row, itemsArrayList);

        this.context = context;
        this.topics = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.problem_row, parent, false);

        // 3. Get the two text view from the rowView
        TextView title = (TextView) rowView.findViewById(R.id.row_title);

        // 4. Set the text for textView
        title.setText(topics.get(position).name);

        // 5. retrn rowView
        return rowView;
    }
}
