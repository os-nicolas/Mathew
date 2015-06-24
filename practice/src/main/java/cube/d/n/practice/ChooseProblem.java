package cube.d.n.practice;

import cube.d.n.practice.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


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

        int topicId= getIntent().getIntExtra("topic", -1);

        myTopic=TopicRow.topics.get(topicId);

        TextView tv = (TextView)findViewById(R.id.problem_title_text);
        Typeface dj = Typeface.createFromAsset(this.getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        tv.setTypeface(dj);

        // 1. pass context and data to the custom adapter
        final ProblemArrayAdapter adapter = myTopic.getAdapter(this);

        // 2. Get ListView from activity_main.xml
        ListView listView = (ListView) findViewById(R.id.problem_listView);

        // 3. setListAdapter
        listView.setAdapter(adapter);

    }
}
