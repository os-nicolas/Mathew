package cube.d.n.practice;


        import java.util.ArrayList;

        import android.content.Context;
        import android.graphics.Typeface;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

public class TopicArrayAdaptor extends ArrayAdapter<TopicRow> {

    private final Context context;
    private final ArrayList<TopicRow> topics;
    private View myView=null;

    public TopicArrayAdaptor(Context context, ArrayList<TopicRow> itemsArrayList) {

        super(context, R.layout.topic_row, itemsArrayList);

        this.context = context;
        this.topics = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.topic_row, parent, false);

        // 3. Get the two text view from the rowView
        TextView title = (TextView) rowView.findViewById(R.id.row_title);

        Typeface djLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        title.setTypeface(djLight);

        TextView subtitle = (TextView) rowView.findViewById(R.id.row_subtitle);

        Typeface dj = Typeface.createFromAsset(context.getAssets(),
                "fonts/DejaVuSans.ttf");
        subtitle.setTypeface(dj);

        // 4. Set the text for textView
        title.setText(topics.get(position).name);
        subtitle.setText(topics.get(position).about);

        // 5. retrn rowView
            convertView= rowView;
        }
        return  convertView;
    }
}