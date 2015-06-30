package cube.d.n.practice;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cube.d.n.commoncore.Main;

/**
 * Created by Colin_000 on 6/27/2015.
 */
public class ProblemActivity extends Activity {

    ProblemRow myProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int problemId= getIntent().getIntExtra("problem", -1);

        myProblem=ProblemRow.problems.get(problemId);

        if (myProblem.equation == null) {

            setContentView(R.layout.problem_activity_wi);

            Main main = (Main) findViewById(R.id.problem_main);
            View text = findViewById(R.id.dumb_words);

            Typeface djLight = Typeface.createFromAsset(this.getAssets(),
                    "fonts/DejaVuSans-ExtraLight.ttf");
            ((TextView) text).setTypeface(djLight);

            main.getProblemImage().setSource(text);
        }else {
            setContentView(R.layout.problem_activity_wi);

            Main main = (Main) findViewById(R.id.problem_main);
            View text = findViewById(R.id.dumb_words);

            Typeface djLight = Typeface.createFromAsset(this.getAssets(),
                    "fonts/DejaVuSans-ExtraLight.ttf");
            ((TextView) text).setTypeface(djLight);

            main.getProblemImage().setSource(text);
            main.initWE(myProblem.equation);

        }

    }
}
