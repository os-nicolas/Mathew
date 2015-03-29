package colin.example.algebrator.Actions.SovleScreen;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import colin.algebrator.eq.Equation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.BothSidesScreen;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.BothSidesView;
import colin.example.algebrator.EquationButton;

/**
 * Created by Colin_000 on 3/28/2015.
 */
public class BothSides extends Action<ColinView> {

    public BothSides(ColinView colinView) {
        super(colinView);
    }

    public static Equation mine = null;
    @Override
    protected void privateAct() {

        mine = myView.stupid.copy();

        AsyncTask<Void, Void, Long> task = new AsyncTask<Void, Void, Long>() {
            Intent myIntent;
            Context myContext;

            protected Long doInBackground(Void... v) {


                Equation newEq = BothSides.mine;

                myContext = myView.getContext();
                BothSidesView bothSidesView = new BothSidesView(myContext);
               bothSidesView.history.add(new EquationButton(newEq,bothSidesView));
                //bothSidesView.centerEq();
                Algebrator.getAlgebrator().addBothView = bothSidesView;
                //((MainActivity) c).lookAt(colinView);

                myIntent = new Intent(myContext, BothSidesScreen.class);
                myContext.startActivity(myIntent);
                return 1L;
            }

            protected void onProgressUpdate(Void v) {
            }

            protected void onPostExecute(Long v) {
            }
        };
        myView.disabled = true;
        task.execute();
    }

    @Override
    public boolean canAct() {
        return true;
    }
}
