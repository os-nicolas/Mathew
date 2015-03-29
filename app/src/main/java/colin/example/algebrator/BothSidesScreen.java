package colin.example.algebrator;

import android.os.Bundle;
import android.view.ViewGroup;

/**
 * Created by Colin_000 on 3/28/2015.
 */
public class BothSidesScreen
            extends SuperScreen {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ScreenName = "BothSidesScreen";
        }

        @Override
        protected void onResume(){
            super.onResume();

            if (Algebrator.getAlgebrator().addBothView == null) {
                myView = new EmilyView(this);
                Algebrator.getAlgebrator().addBothView = (BothSidesView)myView;
            }else{
                myView = Algebrator.getAlgebrator().addBothView;
                if(myView.getParent() != null) {
                    ((ViewGroup) myView.getParent()).removeView(myView);
                }
            }
            myView.disabled = false;

            lookAt(myView);
        }
}
