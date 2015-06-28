package cube.d.n.practice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;

/**
 * Created by Colin_000 on 6/27/2015.
 */
public class ProblemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.problem_activity);

        Main main = (Main)findViewById(R.id.problem_main);
        View text = findViewById(R.id.dumb_words);

        Typeface djLight = Typeface.createFromAsset(this.getAssets(),
                "fonts/DejaVuSans-ExtraLight.ttf");
        ((TextView)text).setTypeface(djLight);

        main.addOverLay(text);
    }
}
