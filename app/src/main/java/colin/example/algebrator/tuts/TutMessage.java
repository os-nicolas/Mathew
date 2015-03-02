package colin.example.algebrator.tuts;

import android.content.SharedPreferences;

import colin.example.algebrator.Algebrator;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public abstract class TutMessage {
    static TutMessage[] tuts = new TutMessage[]{
        new SolvedTut(),
        new HitSolveMessage(),
        new WriteMessage(),
        new SolveMessage(),
          new HistoryTut()
    };
    public static final String PREFS_NAME = "tuts";
    protected abstract String getSp_key();

    public TutMessage(){

        // for testing only so i can get my tut message all the time
//        SharedPreferences settings = Algebrator.getAlgebrator().getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putBoolean(getSp_key(), false);
//
//        editor.commit();
    }

    public static void tryShowAll(SuperView view){
        for (TutMessage tm:tuts){
            tm.tryShow(view);
        }
    }

    public void tryShow(SuperView view){
        if (shouldShow(view)){
            show(view);
        }
    }

    public boolean shouldShow(SuperView view){

        SharedPreferences settings = Algebrator.getAlgebrator().getSharedPreferences(PREFS_NAME, 0);
        boolean alreadyShown = settings.getBoolean(getSp_key(), false);
        if (!alreadyShown){
            return privateShouldShow(view);
        }
        return false;
    }

    protected abstract boolean privateShouldShow(SuperView view);

    public void show(SuperView view){
        privateShow(view);

        SharedPreferences settings = Algebrator.getAlgebrator().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(getSp_key(), true);

        editor.commit();

    }

    protected abstract void privateShow(SuperView view);

}
