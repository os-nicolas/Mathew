package cube.d.n.practice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        final TopicArrayAdaptor adapter = new TopicArrayAdaptor(this, Mathilda.getMathilda().getTopics(),listView);

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


}
