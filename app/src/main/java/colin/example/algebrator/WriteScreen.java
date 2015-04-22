package colin.example.algebrator;

import android.os.Bundle;
import android.view.ViewGroup;

import cube.d.n.commoncore.eq.Equation;
import cube.d.n.commoncore.eq.PlaceholderEquation;

/**
 * Created by Colin on 2/6/2015.
 */
public class WriteScreen
        extends SuperScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenName = "WriteScreen";
    }

    @Override
    protected void onResume(){
        super.onResume();

        if (Algebrator.getAlgebrator().writeView == null) {
            myView = new EmilyView(this);
            Algebrator.getAlgebrator().writeView = (EmilyView)myView;
        }else{
            myView = Algebrator.getAlgebrator().writeView;
            if(myView.getParent() != null) {
                ((ViewGroup) myView.getParent()).removeView(myView);
            }
        }
        myView.disabled = false;

        lookAt(myView);
    }
}
