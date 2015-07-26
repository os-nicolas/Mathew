package cube.d.n.practice;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;

public class MainActivity extends FullAct {

    Main main;

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


//        main = new Main(this, InputLineEnum.TUT_E);
//        main.initE(Util.stringEquation("*,5,(,/,(,+,10,9,-7,),6,)".split(",")));//"*,5,(,/,(,+,10,9,-7,),6,)"
//        main.allowPopups = true;
//        main.trackFinger = true;
//        setContentView(main);



        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listview);

        final TwoLineArrayAdaptor adapter = new TwoLineArrayAdaptor(this, getMainRows(),listView);

        LinearLayout ll = (LinearLayout)findViewById(R.id.main_header);

        if (Mathilda.getMathilda().hasSupported()){
            findViewById(R.id.thank_you).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.thank_you_text)).setTypeface(Mathilda.getMathilda().getDJVL());
        }


//        TextView tv = (TextView)findViewById(R.id.main_title_text);
//        Typeface dj = Typeface.createFromAsset(this.getAssets(),
//                "fonts/DejaVuSans-ExtraLight.ttf");
//        tv.setTypeface(dj);

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

        if (!Mathilda.hasCompletedTut()) {
            rows.add(new MainRow("Tutorial", "Get to know Drag & Drop Algebra", TutActivity.class));

            rows.add(new Divider());
        }

        for (TopicRow t: topics){
            rows.add(t);
        }

        rows.add(new Divider());

        if (Mathilda.hasCompletedTut()) {
            rows.add(new MainRow("Tutorial", "Get to know Drag & Drop Algebra", TutActivity.class));
        }

        rows.add(new MainRow("Feedback", "Send us your comments/ideas",FeedBack.class));
        rows.add(new MainRow("Donate", "",Support.class));

        return rows;
    }

    @Override
    public void onResume(){
        super.onResume();

//        // TODO
//        // TODO
//        // i put this here so the app will crash when i uncomment the real code in onCreate
//        // i need to rememeber to uncomment the code below too
//        // the stuff below "we need to update if we solved anything"
//        boolean trash = main.allowPopups;

        // we need to update if we solved anything
         ListView listView = (ListView) findViewById(R.id.listview);
        ((TwoLineArrayAdaptor) listView.getAdapter()).updatePrecents();

        BaseApp.getApp().recordScreen("main");
    }

}
