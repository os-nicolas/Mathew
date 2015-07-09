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

        myProblem=Problem.problems.get(problemId).getRow();




        Main main;
        if (myProblem.myProblem.equation == null) {
            setContentView(R.layout.problem_activity_wi);
            main  = (Main) findViewById(R.id.problem_main);
            //main.initWI();
        }else {
            setContentView(R.layout.problem_activity_we);
            main  = (Main) findViewById(R.id.problem_main);
            main.initWE(myProblem.myProblem.equation);
        }

        main.getProblemImage().setTitle(myProblem.myProblem.name);
        main.getProblemImage().setBody(myProblem.myProblem.text);
    }
}
