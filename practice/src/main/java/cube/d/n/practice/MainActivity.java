package cube.d.n.practice;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);

        // 1. pass context and data to the custom adapter
        final TopicArrayAdaptor adapter = new TopicArrayAdaptor(this, generateData());

        // 2. Get ListView from activity_main.xml
        ListView listView = (ListView) findViewById(R.id.listview);

        // 3. setListAdapter
        listView.setAdapter(adapter);

        final Activity that = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> unused,View v, int position,long arg3){

                TopicRow item = adapter.getItem(position);

                Intent intent = new Intent(that,ChooseProblem.class);
                //based on item add info to intent
                intent.putExtra("topic",item.name);
                startActivity(intent);

            }
        });
    }

    private ArrayList<TopicRow> generateData() {
        ArrayList<TopicRow> topics = new ArrayList<TopicRow>();
        topics.add(new TopicRow("Item 1", "First Item on the list"));
        topics.add(new TopicRow("Item 2", "Second Item on the list"));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));
        topics.add(new TopicRow("Item 1", "First Item on the list"));
        topics.add(new TopicRow("Item 2", "Second Item on the list"));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));
        topics.add(new TopicRow("Item 1", "First Item on the list"));
        topics.add(new TopicRow("Item 2", "Second Item on the list"));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));
        topics.add(new TopicRow("Item 1", "First Item on the list"));
        topics.add(new TopicRow("Item 2", "Second Item on the list"));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));
        topics.add(new TopicRow("Item 1", "First Item on the list"));
        topics.add(new TopicRow("Item 2", "Second Item on the list"));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));
        topics.add(new TopicRow("Item 1", "First Item on the list"));
        topics.add(new TopicRow("Item 2", "Second Item on the list"));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));
        topics.add(new TopicRow("Item 1", "First Item on the list"));
        topics.add(new TopicRow("Item 2", "Second Item on the list"));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));
        topics.add(new TopicRow("Item 1", "First Item on the list"));
        topics.add(new TopicRow("Item 2", "Second Item on the list"));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));
        topics.add(new TopicRow("Item 1", "First Item on the list"));
        topics.add(new TopicRow("Item 2", "Second Item on the list"));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));
        topics.add(new TopicRow("Item 1", "First Item on the list"));
        topics.add(new TopicRow("Item 2", "Second Item on the list"));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));
        topics.add(new TopicRow("Item 1", "First Item on the list"));
        topics.add(new TopicRow("Item 2", "Second Item on the list"));
        topics.add(new TopicRow("Item 3", "Third Item on the list"));

        return topics;
    }
}
