package cube.d.n.practice;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cube.d.n.commoncore.ISolveController;
import cube.d.n.commoncore.Main;

/**
 * Created by Colin_000 on 6/27/2015.
 */
public class ProblemActivity extends Activity implements ISolveController {

    Problem myProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        solvedLooper = new LooperThread();
        solvedLooper.start();

        int problemId= getIntent().getIntExtra("problem", -1);

        myProblem=Problem.problems.get(problemId).getRow().myProblem;

        Main main;
        if (myProblem.equation == null) {
            setContentView(R.layout.problem_activity_wi);
            main  = (Main) findViewById(R.id.problem_main);
            //main.initWI();
        }else {
            setContentView(R.layout.problem_activity_we);
            main  = (Main) findViewById(R.id.problem_main);
            main.initWE(myProblem.equation);
        }

        if (myProblem.name.equals("")){
            main.getProblemImage().setTitle(myProblem.text);
            main.getProblemImage().setBody("");
        }else {
            main.getProblemImage().setTitle(myProblem.name);
            main.getProblemImage().setBody(myProblem.text);
        }

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

        Log.d("myProblem.solution",""+myProblem.solution);
        if (myProblem.solution != null){
            main.solvable(myProblem.solution,R.id.problem_yay,this);
        }
    }


    public class LooperThread extends Thread {
        public Handler mHandler;

        @Override
        public void run() {
            Looper.prepare();

            mHandler = new Handler();

            Looper.loop();
        }
    }

    LooperThread solvedLooper;

    @Override
    public void solved(Runnable runnable) {
        myProblem.setSolved(true);
        solvedLooper.mHandler.post(runnable);
    }

    @Override
    public void reset(Main main, Runnable r) {
        // todo
        Log.d("reset","ProblemActivty.reset is not yet implemented");
    }
}
