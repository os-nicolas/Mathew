package cube.d.n.commoncore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Colin_000 on 7/3/2015.
 */
public class TutMainFrag  extends TutFrag {

    String title;
    String equation;

    public static TutMainFrag make(String title,String equation) {
            TutMainFrag result = new TutMainFrag();
            Bundle args = new Bundle();
            args.putString("TITLE",title);
            args.putString("EQUATION",equation);
            result.setArguments(args);
            Log.i("make", "set arguments " + result.hashCode() + " args " + result.getArguments());
            return result;
        }

    public void updateData(Bundle args){
        this.equation = args.getString("EQUATION");
        this.title = args.getString("TITLE");
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

        Main main = (Main)rootView.findViewById(R.id.tuttry_main);

        main.initE(Util.stringEquation(equation.split(",")));

        if (drawOnStart){start(rootView);}

        return rootView;
    }

}
