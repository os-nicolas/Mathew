package cube.d.n.commoncore.tuts;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cube.d.n.commoncore.FadeInTextView;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.R;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.HiddenInputLine;

/**
 * Created by Colin_000 on 7/3/2015.
 */
public class TutMainFrag  extends TutFrag {

    String title;
    String equation;
    String goal;
    boolean allowPopUps;
    boolean allowRevert;
    private String[] steps;
    private LooperThread looperThread;
    public LooperThread headerLooper;


    public static TutMainFrag make(String title,String equation, String goal, boolean allowRevert, boolean allowPopUps) {
            TutMainFrag result = new TutMainFrag();
            Bundle args = new Bundle();
            args.putString("TITLE",title);
            args.putString("EQUATION",equation);
            args.putString("GOAL",goal);
            args.putBoolean("REVERT",allowRevert);
            args.putBoolean("POPUP",allowPopUps);
            result.setArguments(args);
            Log.i("make", "set arguments " + result.hashCode() + " args " + result.getArguments());
            return result;
        }



    public void updateData(Bundle args){
        this.equation = args.getString("EQUATION");
        this.title = args.getString("TITLE");
        this.goal = args.getString("GOAL");
        this.allowRevert = args.getBoolean("REVERT",true);
        this.allowPopUps = args.getBoolean("POPUP",true);
        this.ribbonColor = args.getInt("RIBBON_COLOR",0xffff0000);
        this.steps = args.getStringArray("STEPS");
    }

    @Override
    protected void pstart(View rootView){
        ((FadeInTextView) rootView.findViewById(R.id.tut_body)).start();

        if (extaTimeOut != -1){
            ((FadeInTextView) rootView.findViewById(R.id.tut_body)).hangTime +=extaTimeOut;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Log.i("hey","create view "+this+"");
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.tuttry, container, false);

        looperThread = new LooperThread();
        looperThread.start();
        headerLooper = new LooperThread();
        headerLooper.start();


        Log.i("make","got arguments " +hashCode() +" args "+getArguments());
        updateData(getArguments());

       ((TextView) rootView.findViewById(R.id.tut_body)).setText(title);

       rootView.findViewById(R.id.tut_body).setBackgroundColor(ribbonColor);
       rootView.findViewById(R.id.ribbon).setBackgroundColor(ribbonColor);

        Main main = (Main)rootView.findViewById(R.id.tuttry_main);
        setUp(main);


        if (goal != "") {
            main.solvable(Util.stringEquation(goal.split(",")), R.id.tuttry_yay,this);
        }
        main.allowRevert = allowRevert;
        main.allowPopups = allowPopUps;

        if (drawOnStart){start(rootView);}

        return rootView;
    }


    public void setStep(String s) {
        Bundle args = getArguments();
        if (args== null) {
            args = new Bundle();
            setArguments(args);
        }
        String[] strings = args.getStringArray("STEPS");
        if (strings == null){
            strings = new String[0];
        }
        String[] toPut = new String[strings.length+1];
        for (int i=0;i<strings.length;i++){
            toPut[i] = strings[i];
        }
        toPut[strings.length] = s;
        args.putStringArray("STEPS", toPut);
    }

    public TutMainFrag withStep(String s) {
        setStep(s);
        return this;
    }

    public void reset(final Main main, final Runnable r) {


        View root = (View)main.getParent();
//        while (root.getParent() != null && root.getParent() instanceof View){
//            root = (View)root.getParent();
//        }
        final View overlay = root.findViewById(R.id.tuttry_yay);

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

        final View whiteout = root.findViewById(R.id.tuttry_white_out);
        whiteout.setVisibility(View.VISIBLE);
        whiteout.setAlpha(0);


        looperThread.mHandler.post(new Runnable() {
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

    private void setUp(Main main) {
        main.initE(Util.stringEquation(equation.split(",")));

        if (steps != null){
            for (String s:steps){
                main.addStep(Util.stringEquation(s.split(",")));
            }
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
}
