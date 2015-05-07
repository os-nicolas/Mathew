package cube.d.n.commoncore;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Colin_000 on 5/4/2015.
 */
public abstract class TutFrag extends Fragment {




    boolean hasStarted = false;
public void start(View rootView){
    if (!hasStarted){
        hasStarted = true;
        pstart(rootView);
    }
}

    protected void pstart(View rootView){

        Log.i("hey","start "+this+"");

    ((FadeInTextView) rootView.findViewById(R.id.tut_title)).start();
    ((FadeInTextView) rootView.findViewById(R.id.tut_at)).start();


        if (extaTimeOut != -1){
            ((FadeInTextView) rootView.findViewById(R.id.tut_title)).hangTime +=extaTimeOut;
            ((FadeInTextView) rootView.findViewById(R.id.tut_at)).hangTime +=extaTimeOut;
        }



    }

    protected boolean drawOnStart = false;
    public void startOnDraw() {
        drawOnStart = true;
    }

    long extaTimeOut =-1;
    public Fragment withExtaTimeOut(int i) {
        extaTimeOut = i;
        return this;
    }


        @Override
    public void onPause(){
        super.onPause();
        Log.i("TutFrag.onPause",this+"");
        hasStarted = false;
            drawOnStart = false;
    }
}
