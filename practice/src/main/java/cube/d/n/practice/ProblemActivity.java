package cube.d.n.practice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.CircleView;
import cube.d.n.commoncore.ISolveController;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.Nextmanager;
import cube.d.n.commoncore.YayProblemView;

/**
 * Created by Colin_000 on 6/27/2015.
 */
public class ProblemActivity extends FullAct implements ISolveController {

    Problem myProblem;
    private LooperThread headerLooper;
    Main main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        solvedLooper = new LooperThread();
        solvedLooper.start();
        headerLooper = new LooperThread();
        headerLooper.start();

        int problemId = getIntent().getIntExtra("problem", -1);

        myProblem = Problem.problems.get(problemId).getRow().myProblem;

        hideStatusBar();

        if (myProblem.view != null) {
            if (myProblem.view.getParent() != null) {
                ((ViewGroup) myProblem.view.getParent()).removeView(myProblem.view);
            }

            setContentView(myProblem.view);



            main = (Main) myProblem.view.findViewById(R.id.problem_main);
        } else {

            if (myProblem.equation == null) {
                setContentView(R.layout.problem_activity_wi);
                main = (Main) findViewById(R.id.problem_main);
                //main.initWI();
            } else {
                setContentView(R.layout.problem_activity_we);
                main = (Main) findViewById(R.id.problem_main);
            }

            setUp(main);



            View at = main;
            while(at.getParent() != null && at.getParent() instanceof View){
                at = (View)at.getParent();
            }

            myProblem.view = at.findViewById(R.id.problem_root);
        }


    }

    @Override
    public void onResume() {
        super.onResume();

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
                that.finish();
            }

            @Override
            public  void finish(){
                that.finish();
            }
        };

        ((YayProblemView) myProblem.view.findViewById(R.id.problem_yay)).reset();
        BaseApp.getApp().recordScreen(myProblem.topic + "-" + myProblem.myId);
    }





    private void setUp(Main main) {
        if (myProblem.equation == null || myProblem.input) {
            //main.initWI();
        } else {
            main.initWE(myProblem.equation);
        }

        if (myProblem.name.equals("")) {
            main.getProblemImage().setTitle(myProblem.text);
            main.getProblemImage().setBody("");
        } else {
            main.getProblemImage().setTitle(myProblem.name);
            main.getProblemImage().setBody(myProblem.text);
        }

        int myIndex =  myProblem.getIndex();

        int size = myProblem.getTopic().getProblems().size();

        main.getProblemImage().circleDrawer.setColors((myIndex<10?"0":"")+ myIndex,
                CircleView.getBkgColor(myIndex,size),//BaseApp.colorFade(CircleView.getBkgColor(myIndex), 0xffffffff, 1.2f),
                CircleView.getTextColor(myIndex,size)); //BaseApp.colorFade(CircleView.getTextColor(myIndex),0xffffffff,1.2f));
        main.getProblemImage().circleDrawer.supText = new String(myProblem.getTopic().shortName).toUpperCase();
        main.getProblemImage().circleDrawer.setPrecent((myProblem.getSolved() ? 1 : 0));
        if (myProblem.getSolved()){
            main.getProblemImage().circleDrawer.setSubText("SOLVED");
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
        main.getProblemImage().circleDrawer.setPrecent(1);
        main.getProblemImage().circleDrawer.setSubText("SOLVED");
        solvedLooper.mHandler.post(runnable);
        HappyView hv = (HappyView)myProblem.view.findViewById(R.id.happy);
        hv.start();
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
                                main.initOffset();

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
