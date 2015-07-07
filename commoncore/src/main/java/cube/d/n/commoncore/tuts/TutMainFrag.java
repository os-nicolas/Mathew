package cube.d.n.commoncore.tuts;

import android.os.Bundle;
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

/**
 * Created by Colin_000 on 7/3/2015.
 */
public class TutMainFrag  extends TutFrag {

    String title;
    String equation;
    String goal;
    boolean allowPopUps;
    boolean allowRevert;


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


        Log.i("make","got arguments " +hashCode() +" args "+getArguments());
        updateData(getArguments());

       ((TextView) rootView.findViewById(R.id.tut_body)).setText(title);

       rootView.findViewById(R.id.tut_body).setBackgroundColor(ribbonColor);
       rootView.findViewById(R.id.ribbon).setBackgroundColor(ribbonColor);

        Main main = (Main)rootView.findViewById(R.id.tuttry_main);
        main.initE(Util.stringEquation(equation.split(",")));
        
        if (goal != "") {
            main.solvable(Util.stringEquation(goal.split(",")), R.id.tuttry_yay);
        }
        main.allowRevert = allowRevert;
        main.allowPopups = allowPopUps;

        if (drawOnStart){start(rootView);}

        return rootView;
    }

    public TutMainFrag withStep(String s) {
        return null;
    }
}
