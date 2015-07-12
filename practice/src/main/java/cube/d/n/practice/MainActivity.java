package cube.d.n.practice;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cube.d.n.commoncore.InputLineEnum;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.lines.NullLine;

public class MainActivity extends Activity {

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
//        // Remember that you should never show the action bar if the
//        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


//        Main main = new Main(this, InputLineEnum.TUT_EK);
//        main.initE(Util.stringEquation("=,5,(,+,(,/,a,2,),3,)".split(",")));//"*,5,(,/,(,+,10,9,-7,),6,)"
//        main.allowPopups = false;
//        main.trackFinger = true;
//        setContentView(main);

//        if (steps != null){
//            for (String s:steps){
//                main.addStep(Util.stringEquation(s.split(",")));
//            }
//        }


        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listview);

        final TopicArrayAdaptor adapter = new TopicArrayAdaptor(this, getMainRows(),listView);

        LinearLayout ll = (LinearLayout)findViewById(R.id.main_header);


        TextView tv = (TextView)findViewById(R.id.main_title_text);
        Typeface dj = Typeface.createFromAsset(this.getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        tv.setTypeface(dj);

        listView.setAdapter(adapter);

        final Activity that = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> unused,View v, int position,long arg3){

                Row item = adapter.getItem(position);

                if (item instanceof Goable){
                    ((Goable)item).go(that);
                }
            }
        });
    }

    private ArrayList<Row> getMainRows() {
        ArrayList<TopicRow> topics = Mathilda.getMathilda().getTopics();
        ArrayList<Row> rows = new ArrayList<>();

        rows.add(new MainRow("Tutorial", "how do i use this thing anyway",TutActivity.class));

        rows.add(new Divider());

        for (TopicRow t: topics){
            rows.add(t);
        }

        rows.add(new Divider());

        rows.add(new MainRow("Feedback", "got an idea, let us know",FeedBack.class));
        rows.add(new MainRow("Support", "got an idea, let us know",Support.class));


        return rows;
    }


}
