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

public class TopicArrayAdaptor extends ArrayAdapter<Row> {

    private final Context context;
    private final ArrayList<Row> topics;
    public ConcurrentHashMap<Integer,View> views = new ConcurrentHashMap<>();

    public TopicArrayAdaptor(Context context, ArrayList<Row> itemsArrayList,final ViewGroup parent ) {

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
        View res = topics.get(position).makeView(context,parent,position);
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