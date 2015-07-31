package cube.d.n.practice;


        import java.util.ArrayList;
        import java.util.concurrent.ConcurrentHashMap;

        import android.content.Context;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;

public class TwoLineArrayAdaptor extends ArrayAdapter<Row> {

    private final Context context;
    private final ArrayList<Row> rows;
    public ConcurrentHashMap<Integer,View> views = new ConcurrentHashMap<>();

    public TwoLineArrayAdaptor(Context context, ArrayList<Row> itemsArrayList, final ViewGroup parent) {

        super(context, R.layout.two_line_row, itemsArrayList);

        this.context = context;
        this.rows = itemsArrayList;

        //Thread th = new Thread(){
            //public void run() {
                for (int i=0;i< rows.size();i++){
                    makeView(i, parent);
                }
            //}
        //};
        //th.start();
    }

    private View makeView(int position, ViewGroup parent) {
        View res = rows.get(position).makeView(context,parent,position,rows.size());
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

    public void updatePrecents() {
        for (Row v: rows){
            if (v instanceof CanUpdatePrecent){
                ((CanUpdatePrecent)v).updatePrecent();
            }
        }
    }

    public Row getRow(int i) {
        return rows.get(i);
    }
}