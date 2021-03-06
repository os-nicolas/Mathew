package cube.d.n.practice;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.MainActions.LastAction;
import cube.d.n.commoncore.Action.MainActions.NextAction;
import cube.d.n.commoncore.Action.MainActions.ResetAction;
import cube.d.n.commoncore.Action.MainActions.UpAction;
import cube.d.n.commoncore.Action.SolvedAction;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Button;
import cube.d.n.commoncore.CircleView;
import cube.d.n.commoncore.HappyView;
import cube.d.n.commoncore.ISolveController;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.MessageButton;
import cube.d.n.commoncore.Nextmanager;
import cube.d.n.commoncore.YayProblemView;
import cube.d.n.commoncore.lines.EquationLine;

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


        if (myProblem.view != null) {
            if (myProblem.view.getParent() != null) {
                ((ViewGroup) myProblem.view.getParent()).removeView(myProblem.view);
            }

            setContentView(myProblem.view);



            main = (Main) myProblem.view.findViewById(R.id.problem_main);
        } else {

            if (myProblem.input) {
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
                Bundle settings = ActivityOptions.makeCustomAnimation(that,R.anim.inright,R.anim.outleft).toBundle();
                startActivity(intent, settings);
                that.overridePendingTransition(R.anim.outleft, R.anim.inright);
                that.finish();
            }

            @Override
            public  void finish(){
                that.finish();
            }

            @Override
            public boolean hasLast(){
                return myProblem.last()!= null;
            }

            @Override
            public void last() {
                Intent intent = new Intent(that, ProblemActivity.class);
                //based on item add info to intent
                intent.putExtra("problem", myProblem.last().myId);
                Bundle settings = ActivityOptions.makeCustomAnimation(that,R.anim.inleft,R.anim.outright).toBundle();
                startActivity(intent,settings);
                that.overridePendingTransition(R.anim.outright,R.anim.inleft);
                that.finish();
            }
        };

        //((YayProblemView) myProblem.view.findViewById(R.id.problem_yay)).reset();
        BaseApp.getApp().recordScreen(myProblem.topic + "-" + myProblem.myId);
    }





    private void setUp(Main main) {
        ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add(new Button("←",new LastAction(main)));
        buttons.add(new Button("Reset",new ResetAction(main)));
        buttons.add(new Button("Menu",new UpAction(main)));
        buttons.add(new Button("→",new NextAction(main)));
        if (myProblem.equation == null || myProblem.input) {
            main.initWI(buttons);
        } else {
            main.initWE(myProblem.equation, buttons );
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
            main.solvable(myProblem.solution, this);
        } //R.id.problem_yay,
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
    public void solved(Main m,Runnable runnable) {
        myProblem.setSolved(true);
        m.getProblemImage().circleDrawer.setPrecent(1);
        m.getProblemImage().circleDrawer.setSubText("SOLVED");

        SolvedAction sa= new SolvedAction((EquationLine)m.lastLine());
        MessageButton mb = new MessageButton(sa.getDisplay(),sa,6000);
        m.message(mb);

        solvedLooper.mHandler.post(runnable);
        HappyView hv = (HappyView)myProblem.view.findViewById(R.id.happy);
        hv.start();
    }

    @Override
    public void reset(final Main main,final Runnable r) {
        // todo

        View root = (View)main.getParent();
//        while (root.getParent() != null && root.getParent() instanceof View){
//            root = (View)root.getParent();
//        }
        //final View overlay = root.findViewById(R.id.problem_yay);


        final Activity context = (Activity)main.getContext();

//        headerLooper.mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//
//                overlay.animate().alpha(0).setDuration(400).withLayer().withEndAction(new Runnable() {
//                    @Override
//                    public void run() {
//                        context.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                overlay.setVisibility(View.GONE);
//                            }
//                        });
//                    }
//                });
//            }
//        });

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
//                                        overlay.setVisibility(View.GONE);
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
