package cube.d.n.practice;

import cube.d.n.practice.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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


        ListView listView = (ListView) findViewById(R.id.problem_listView);

        final ProblemArrayAdapter adapter = myTopic.getAdapter(this,listView);

        listView.setAdapter(adapter);
        final Activity that = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> unused,View v, int position,long arg3){

                ProblemRow item = adapter.getItem(position);

                Intent intent = new Intent(that,ProblemActivity.class);
                //based on item add info to intent
                intent.putExtra("problem",item.myProblem.myId);
                startActivity(intent);

            }
        });

    }
}
