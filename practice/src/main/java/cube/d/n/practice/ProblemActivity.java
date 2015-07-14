package cube.d.n.practice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import cube.d.n.commoncore.ISolveController;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Nextmanager;

/**
 * Created by Colin_000 on 6/27/2015.
 */
public class ProblemActivity extends FullAct implements ISolveController {

    Problem myProblem;
    private LooperThread headerLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        solvedLooper = new LooperThread();
        solvedLooper.start();
        headerLooper = new LooperThread();
        headerLooper.start();

        int problemId = getIntent().getIntExtra("problem", -1);

        myProblem = Problem.problems.get(problemId).getRow().myProblem;

        if (myProblem.view != null) {
            if (myProblem.view.getParent() != null) {
                ((ViewGroup) myProblem.view.getParent()).removeView(myProblem.view);
            }
            setContentView(myProblem.view);
        } else {

            Main main;
            if (myProblem.equation == null) {
                setContentView(R.layout.problem_activity_wi);
                main = (Main) findViewById(R.id.problem_main);
                //main.initWI();
            } else {
                setContentView(R.layout.problem_activity_we);
                main = (Main) findViewById(R.id.problem_main);
            }

            setUp(main);

            final Activity that = this;
            main.next = new Nextmanager(){
              @Override
              public boolean hasNext(){
                  return myProblem.next()!= null;
              }
                @Override
                public void next(){
                    Intent intent = new Intent(that, ProblemActivity.class);
                    //based on item add info to intent
                    intent.putExtra("problem", myProblem.next().myId);
                    startActivity(intent);
                }
            };

            View at = main;
            while(at.getParent() != null && at.getParent() instanceof View){
                at = (View)at.getParent();
            }
            myProblem.view = at;
        }
    }

    private void setUp(Main main) {
        if (myProblem.equation == null) {
            //main.initWI();
        } else {;
            main.initWE(myProblem.equation);
        }

        if (myProblem.name.equals("")) {
            main.getProblemImage().setTitle(myProblem.text);
            main.getProblemImage().setBody("");
        } else {
            main.getProblemImage().setTitle(myProblem.name);
            main.getProblemImage().setBody(myProblem.text);
        }

        Log.d("myProblem.solution", "" + myProblem.solution);
        if (myProblem.solution != null) {
            main.solvable(myProblem.solution, R.id.problem_yay, this);
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
    public void reset(final Main main,final Runnable r) {
        // todo
        Log.d("reset", "ProblemActivty.reset is not yet implemented");

        View root = (View)main.getParent();
//        while (root.getParent() != null && root.getParent() instanceof View){
//            root = (View)root.getParent();
//        }
        final View overlay = root.findViewById(R.id.problem_yay);

        final Activity context = (Activity)main.getContext();

        headerLooper.mHandler.post(new Runnable() {
            @Override
            public void run() {

                overlay.animate().alpha(0).setDuration(400).withLayer().withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                overlay.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });

        final View whiteout = root.findViewById(R.id.problem_white_out);
        whiteout.setVisibility(View.VISIBLE);
        whiteout.setAlpha(0);


        headerLooper.mHandler.post(new Runnable() {
            @Override
            public void run() {

                main.allowTouch = false;
                whiteout.animate().alpha(1).setDuration(300).withLayer().withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        Thread th = new Thread();
                        try {
                            th.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                r.run();
                                setUp(main);
                            }
                        });
                        whiteout.animate().alpha(0).setDuration(300).withLayer().withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        overlay.setVisibility(View.GONE);
                                        main.allowTouch = true;
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

}
