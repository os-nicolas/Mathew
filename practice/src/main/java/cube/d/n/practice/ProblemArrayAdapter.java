package cube.d.n.practice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import cube.d.n.commoncore.CircleView;
import cube.d.n.commoncore.EquationView;
import cube.d.n.commoncore.GS;

/**
 * Created by Colin on 6/23/2015.
 */
public class ProblemArrayAdapter extends ArrayAdapter<Row> {

    private final Context context;
    private final ArrayList<Row> problems;
    public ConcurrentHashMap<Integer, View> views = new ConcurrentHashMap<>();
    private boolean done = false;

    public ProblemArrayAdapter(Context context, ArrayList<Row> itemsArrayList, final ViewGroup parent) {

        super(context, R.layout.topic_row, new ArrayList<Row>());

        this.context = context;
        this.problems = itemsArrayList;




        addViews(parent,itemsArrayList);

        Log.d("loading issues","I am not blocked");

//        Thread th = new Thread() {
//            public void run() {
//
//            }
//        };
//        th.start();
    }

    private void addViews(final ViewGroup parent, final ArrayList<Row> itemsArrayList) {
        final ProblemArrayAdapter that = this;
        AsyncTask ast = new AsyncTask<Object,Integer,Object>() {
            @Override
            protected Object doInBackground(Object[] x) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                for (int i = 0; i < problems.size(); i++) {
                    publishProgress(i);
                    View rowView = problems.get(i).makeView(context,parent,i);
                    views.put(i, rowView);
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                that.done = true;
                return null;
            }

            protected void onProgressUpdate(Integer... progress) {
                int i = progress[0];
                that.add(itemsArrayList.get(i));


//                that.add(itemsArrayList.get(i));
//                makeView(i, parent);

                Log.d("loading issues","made view");
            }
        };

        ast.execute();

//        final GS<Integer> i =new GS<>(0);
//
//        makeView(i.get(), parent,new Runnable() {
//            @Override
//            public void run() {
//                i.set(i.get() + 1);
//                if (i.get()< problems.size()) {
//                    makeView(i.get(),parent,this);
//                }
//            }
//        });
    }

//    private View makeView(int i, ViewGroup parent){
//        return  makeView(i,parent, new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//    }

    private View makeView(int i, ViewGroup parent) {//, Runnable afterAdded
        View rowView = problems.get(i).makeView(context,parent,i);
//        rowView.setAlpha(0);
        views.put(i, rowView);
        return rowView;
    }

    public Row getProblem(int pos){
        return problems.get(pos);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (views.containsKey(position)) {
            View res = views.get(position);
            if (!done) {
                ((CircleView)res.findViewById(R.id.problem_circle)).circleDrawer.dontAnimate();
            }
            return res;
        } else {
            return makeView(position, parent);
        }
    }

    public void updatePrecents() {
        for (Row v:problems){
            if (v instanceof CanUpdatePrecent){
                ((CanUpdatePrecent)v).updatePrecent();
            }
        }

    }
}
