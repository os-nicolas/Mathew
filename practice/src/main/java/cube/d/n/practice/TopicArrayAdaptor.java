package cube.d.n.practice;


        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.concurrent.ConcurrentHashMap;

        import android.content.Context;
        import android.graphics.Typeface;
        import android.os.AsyncTask;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import cube.d.n.commoncore.EquationView;
        import cube.d.n.commoncore.eq.any.VarEquation;
        import cube.d.n.commoncore.lines.NullLine;

public class TopicArrayAdaptor extends ArrayAdapter<TopicRow> {

    private final Context context;
    private final ArrayList<TopicRow> topics;
    public ConcurrentHashMap<Integer,View> views = new ConcurrentHashMap<>();

    public TopicArrayAdaptor(Context context, ArrayList<TopicRow> itemsArrayList,final ViewGroup parent ) {

        super(context, R.layout.topic_row, itemsArrayList);

        this.context = context;
        this.topics = itemsArrayList;


        Thread th = new Thread(){
            public void run() {
                for (int i=0;i< topics.size();i++){
                    makeView(i, parent);
                }
            }
        };
        th.start();
    }

    private View makeView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View res = inflater.inflate(R.layout.topic_row, parent, false);

        TextView title = (TextView) res.findViewById(R.id.row_title);

        Typeface djLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        title.setTypeface(djLight);


        EquationView equationView = (EquationView) res.findViewById(R.id.row_subtitle);
        if (topics.get(position).equation==null) {
            equationView.setEquation(new VarEquation(topics.get(position).about, new NullLine()), .5f);//,
        }else{
            equationView.setEquation(topics.get(position).equation,.5f);
        }
        equationView.setFont(Mathilda.getMathilda().getDJV());
        equationView.setColor(0xff888888);

        CircleView cir = (CircleView) res.findViewById(R.id.topic_circle);

        // we don
        int p = position+1;
        cir.setColors((p<=9?"0":"")+p+"",CircleView.getBkgColor(p),CircleView.getTextColor(p));


        title.setText(topics.get(position).name);


        views.put(position,res);
        return  res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (views.containsKey(position)){
            return views.get(position);
       }else{
            return makeView(position, parent);
        }
    }
}