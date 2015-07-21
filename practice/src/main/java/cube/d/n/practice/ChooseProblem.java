package cube.d.n.practice;

import cube.d.n.practice.util.SystemUiHider;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ChooseProblem extends Activity {

    TopicRow myTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.problem_select);

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

        String topicName = getIntent().getStringExtra("topic");

        myTopic = TopicRow.topics.get(topicName);

        TextView tv = (TextView) findViewById(R.id.problem_title_text);
        Typeface dj = Typeface.createFromAsset(this.getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        tv.setTypeface(dj);
        tv.setText(topicName);






        final TopicRow tt = myTopic;



//        new Thread(){
//            @Override
//            public void run() {
//                ArrayList<Row> probs =tt.getProbs();
//                for (final Row prob: probs ) {
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            adapter.add(prob);
//                        }
//                    });
////                    try {
////                        sleep(400);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//                }
//        }}.start();

        ListView listView = (ListView) findViewById(R.id.problem_listView);

        final ProblemArrayAdapter adapter = myTopic.getAdapter(this, listView);

        listView.setAdapter(adapter);

        final Activity that = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> unused, View v, int position, long arg3) {

                Row item = adapter.getItem(position);

                if (item instanceof ProblemRow) {
                    Intent intent = new Intent(that, ProblemActivity.class);
                    //based on item add info to intent
                    intent.putExtra("problem", ((ProblemRow) item).myProblem.myId);
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        // we need to update if we solved anything
        ListView listView = (ListView) findViewById(R.id.problem_listView);
        ((ProblemArrayAdapter) listView.getAdapter()).updatePrecents();
    }

}
