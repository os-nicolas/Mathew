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

import cube.d.n.commoncore.EquationView;
import cube.d.n.commoncore.GS;

/**
 * Created by Colin on 6/23/2015.
 */
public class ProblemArrayAdapter extends ArrayAdapter<Row> {

    private final Context context;
    private final ArrayList<Row> problems;
    public ConcurrentHashMap<Integer, View> views = new ConcurrentHashMap<>();

    public ProblemArrayAdapter(Context context, ArrayList<Row> itemsArrayList, final ViewGroup parent) {

        super(context, R.layout.topic_row, itemsArrayList);

        this.context = context;
        this.problems = itemsArrayList;


//        AsyncTask ast = new AsyncTask<Object,Integer,Object>() {
//            @Override
//            protected Object doInBackground(Object[] x) {
////                try {
////                    Thread.sleep(2000);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//                for (int i = 0; i < problems.size(); i++) {
//                    publishProgress(i);
////                    try {
////                        Thread.sleep(500);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//                }
//                return null;
//            }
//
//            protected void onProgressUpdate(Integer... progress) {
//                makeView(progress[0], parent);
//
//                Log.d("loading issues","made view");
//            }
//        };
//
//        ast.execute();

        addViews(parent);

        Log.d("loading issues","I am not blocked");

//        Thread th = new Thread() {
//            public void run() {
//
//            }
//        };
//        th.start();
    }

    private void addViews( final ViewGroup parent) {
        final GS<Integer> i =new GS<>(0);

        makeView(i.get(), parent,new Runnable() {
            @Override
            public void run() {
                i.set(i.get() + 1);
                if (i.get()< problems.size()) {
                    makeView(i.get(),parent,this);
                }
            }
        });
    }

    private View makeView(int i, ViewGroup parent){
        return  makeView(i,parent, new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private View makeView(int i, ViewGroup parent, Runnable afterAdded) {
        View rowView = problems.get(i).makeView(context,parent,i);
        rowView.animate().alpha(0);
        views.put(i, rowView);
        rowView.animate().alpha(0xff).setDuration(1000).withLayer().withEndAction(afterAdded);
        return rowView;
    }

    public Row getProblem(int pos){
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

    public void updatePrecents() {
        for (Row v:problems){
            if (v instanceof CanUpdatePrecent){
                ((CanUpdatePrecent)v).updatePrecent();
            }
        }

    }
}
