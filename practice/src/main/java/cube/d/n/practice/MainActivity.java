package cube.d.n.practice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.lines.NullLine;

public class MainActivity extends Activity {

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listview);

        final TopicArrayAdaptor adapter = new TopicArrayAdaptor(this, generateData(),listView);

        // 2. Get ListView from activity_main.xml


        LinearLayout ll = (LinearLayout)findViewById(R.id.main_header);


        TextView tv = (TextView)findViewById(R.id.main_title_text);
        Typeface dj = Typeface.createFromAsset(this.getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        tv.setTypeface(dj);


        // 3. setListAdapter
        listView.setAdapter(adapter);

        final Activity that = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> unused,View v, int position,long arg3){

                TopicRow item = adapter.getItem(position);

                Intent intent = new Intent(that,ChooseProblem.class);
                //based on item add info to intent
                intent.putExtra("topic",item.myId);
                startActivity(intent);

            }
        });
    }

    private ArrayList<TopicRow> generateData() {
        ArrayList<TopicRow> topics = new ArrayList<TopicRow>();
        Equation eq = new DivEquation(new NullLine());
        eq.add(NumConstEquation.create(1,new NullLine()));
        eq.add(NumConstEquation.create(2,new NullLine()));
        topics.add(new TopicRow("Item 1", eq));
        Equation eq2 = new AddEquation(new NullLine());
        eq2.add(NumConstEquation.create(1,new NullLine()));
        eq2.add(NumConstEquation.create(2,new NullLine()));
        topics.add(new TopicRow("Item 2", eq2));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));
        topics.add(new TopicRow("Item 4", "First Item on the list"));
        topics.add(new TopicRow("Item 5", "Second Item on the list"));
        topics.add(new TopicRow("Item 6", "Third Item on the list"));
        topics.add(new TopicRow("Item 7", "First Item on the list"));
        topics.add(new TopicRow("Item 8", "Second Item on the list"));
        topics.add(new TopicRow("Item 9", "Third Item on the list"));
        topics.add(new TopicRow("Item 10", "First Item on the list"));
        topics.add(new TopicRow("Item 11", "Second Item on the list"));
        topics.add(new TopicRow("Item 12", "Third Item on the list"));
        topics.add(new TopicRow("Item 13", "First Item on the list"));
        topics.add(new TopicRow("Item 14", "Second Item on the list"));
        topics.add(new TopicRow("Item 15", "Third Item on the list"));
        topics.add(new TopicRow("Item 16", "First Item on the list"));
        topics.add(new TopicRow("Item 17", "Second Item on the list"));
        topics.add(new TopicRow("Item 18", "Third Item on the list"));
        topics.add(new TopicRow("Item 19", "First Item on the list"));
        topics.add(new TopicRow("Item 20", "Second Item on the list"));
        topics.add(new TopicRow("Item 21", "Third Item on the list"));
        topics.add(new TopicRow("Item 22", "First Item on the list"));
        topics.add(new TopicRow("Item 23", "Second Item on the list"));
        topics.add(new TopicRow("Item 24", "Third Item on the list"));
        topics.add(new TopicRow("Item 25", "First Item on the list"));
        topics.add(new TopicRow("Item 26", "Second Item on the list"));
        topics.add(new TopicRow("Item 27", "Third Item on the list"));
        topics.add(new TopicRow("Item 28", "First Item on the list"));
        topics.add(new TopicRow("Item 29", "Second Item on the list"));
        topics.add(new TopicRow("Item 30", "Third Item on the list"));
        topics.add(new TopicRow("Item 31", "First Item on the list"));
        topics.add(new TopicRow("Item 32", "Second Item on the list"));
        topics.add(new TopicRow("Item 33", "Third Item on the list"));

        return topics;
    }
}
