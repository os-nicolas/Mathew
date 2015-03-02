package colin.example.algebrator;

import android.os.Bundle;
import android.view.ViewGroup;

/**
 * Created by Colin on 2/6/2015.
 */
public class SolveScreen extends SuperScreen {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenName = "SolveScreen";

    }

    @Override
    protected void onResume(){
        super.onResume();
        myView = Algebrator.getAlgebrator().solveView;
        if (myView.getParent() != null) {
            ((ViewGroup) myView.getParent()).removeView(myView);
        }

        lookAt(myView);
    }
}
