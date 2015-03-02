package colin.example.algebrator.Actions;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.Algebrator;
import colin.example.algebrator.ColinView;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.SolveScreen;


public class Solve extends Action {

    public Solve(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        if (emilyView.stupid instanceof WritingEquation)


            if (((WritingEquation) emilyView.stupid).deepLegal() && countEquals(emilyView.stupid) == 1) {

                AsyncTask<Void,Void,Long> task = new AsyncTask<Void,Void,Long>(){
                    Intent myIntent;
                    Context myContext;

                    protected Long doInBackground(Void... v) {
                        Equation toPass = emilyView.stupid.copy();
                        // we want to remove the place holder
                        if (emilyView.selected instanceof PlaceholderEquation){
                            Equation at = emilyView.stupid;
                            Equation myAt = toPass;
                            while (!at.equals(emilyView.selected)) {
                                int index = at.deepIndexOf(emilyView.selected);
                                at = at.get(index);
                                myAt = myAt.get(index);
                            }
                            myAt.remove();
                        }

                        Equation newEq = ((WritingEquation) toPass).convert();

                        myContext = emilyView.getContext();
                        ColinView colinView = new ColinView(myContext);
                        colinView.stupid = newEq;
                        colinView.centerEq();
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
                emilyView.disabled = true;
                task.execute();
            }
    }
}