package colin.example.algebrator.Actions.SovleScreen;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import cube.d.n.commoncore.eq.Equation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.BothSidesScreen;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.BothSidesView;

/**
 * Created by Colin_000 on 3/28/2015.
 */
public class BothSides extends Action<ColinView> {


    private BothSidesView.BothSidesMode myBothSidesMode;

    public BothSides(BothSidesView.BothSidesMode myBothSidesMode,
                             ColinView colinView) {
        super(colinView);
        this.myBothSidesMode = myBothSidesMode;
    }

    public static Equation mine = null;
    @Override
    protected void privateAct() {

        mine = myView.getStupid().copy();

        AsyncTask<Void, Void, Long> task = new AsyncTask<Void, Void, Long>() {
            Intent myIntent;
            Context myContext;

            protected Long doInBackground(Void... v) {

                myContext = myView.myActivity;
                BothSidesView bothSidesView = new BothSidesView(myContext);
                bothSidesView.setUp(myBothSidesMode,BothSides.mine);
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
        task.execute();
    }

    @Override
    public boolean canAct() {
        return true;
    }
}
