package colin.example.algebrator.Actions.WriteScreen;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import cube.d.n.commoncore.eq.EqualsEquation;
import cube.d.n.commoncore.eq.Equation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.WritingEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.SolveScreen;
import colin.example.algebrator.tuts.HitSolveMessage;
import colin.example.algebrator.tuts.TutMessage;


public class Solve extends Action {

    public Solve(EmilyView emilyView) {
        super(emilyView);
    }

    public static Equation mine;

    @Override
    public boolean canAct() {
        // we need to copy stupid
        mine = myView.getStupid().copy();

        // we want to remove the place holder
        if (myView.selected instanceof PlaceholderEquation) {
            Equation at = myView.getStupid();
            Equation myAt = mine;
            while (!at.equals(myView.selected)) {
                int index = at.deepIndexOf(myView.selected);
                at = at.get(index);
                myAt = myAt.get(index);
            }
            if (myAt.parent != null && myAt.parent.size() != 1) {
                myAt.remove();
            } else {
                return false;
            }
        }

        // we need to follow the path to selected
        // and remove it from mine
        if (mine instanceof WritingEquation) {

            if (((WritingEquation) mine).deepLegal() ) {//&& countEquals(mine) == 1
                return true;
            }
        }
        return false;
    }

    @Override
    protected void privateAct() {
        TutMessage.getMessage(HitSolveMessage.class).alreadyDone();
        AsyncTask<Void, Void, Long> task = new AsyncTask<Void, Void, Long>() {
            Intent myIntent;
            Context myContext;

            protected Long doInBackground(Void... v) {


                Equation newEq = ((WritingEquation) Solve.mine).convert();

                myContext = myView.myActivity;
                ColinView colinView = new ColinView(myContext);
                colinView.setStupid(newEq);
                if (newEq instanceof EqualsEquation) {
                    colinView.centerEq();
                }
                Algebrator.getAlgebrator().solveView = colinView;
                //((MainActivity) c).lookAt(colinView);

                myIntent = new Intent(myContext, SolveScreen.class);
                myContext.startActivity(myIntent);
                return 1L;
            }

            protected void onProgressUpdate(Void v) {
            }

            protected void onPostExecute(Long v) {
            }
        };
        //myView.disabled = true;
        task.execute();

    }
}