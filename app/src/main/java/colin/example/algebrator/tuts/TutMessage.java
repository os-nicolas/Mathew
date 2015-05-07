package colin.example.algebrator.tuts;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

import colin.example.algebrator.Algebrator;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin on 2/27/2015.
 */
public abstract class TutMessage {

//    private static HashMap<Class, TutMessage> instances = new HashMap<Class, TutMessage>();

    static TutMessage[] tuts = new TutMessage[]{
            new WriteMessage(),
//            new TypeEqTut(),
//            new TapToSkip(),
            new HitSolveMessage(),
            new SolveMessage(),
            new SolvedTut(),
            new HistoryTut(),
            new PowerTut(),
            new PowerDragTut(),
            new RootTut(),
            new BothSidesTut(),
//            new AddTut(),
//            new DivTut(),
//            new MultiTut(),
            new RootDragTut()
    };
    public static final String PREFS_NAME = "tuts";
    public static final long shortTime = 2200l;
    public static final long aveTime = 2 * shortTime;
    public static final long longTime = 3 * shortTime;

    public static TutMessage getMessage(Class c){
        for (TutMessage tm: tuts){
            if (tm.getClass().equals(c)){
                return tm;
            }
        }
        return null;
    }


//    public static TutMessage getSingle(Class c) {
//        if (instances.containsKey(c)) {
//            try {
//                TutMessage toAdd= (TutMessage) c.newInstance();
//                Log.i("TutMessage", "adding: " + (toAdd==null?"null":toAdd.toString()));
//                instances.put(c,toAdd);
//            } catch (InstantiationException e) {
//                Log.e("TutMessage",e.toString());
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                Log.e("TutMessage",e.toString());
//                e.printStackTrace();
//            }
//        }
//        return instances.get(c);
//    }

    protected abstract String getSp_key();

    protected TutMessage() {
        //for testing only so i can get my tut message all the time
        SharedPreferences settings = Algebrator.getAlgebrator().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(getSp_key(), false);

        editor.commit();
    }

    public static void tryShowAll(SuperView view) {
        for (TutMessage tm : tuts) {
            tm.tryShow(view);
        }
    }

    public void tryShow(SuperView view) {

        if (shouldShow(view)) {
            show(view);
        }
//        else if (privateShouldShow(view)){
//            alwaysShow(view);
//        }
    }

//    protected SuperView lastShownFor;
//    protected boolean alreadyShown(SuperView view) {
//        return view.equals(lastShownFor);
//    }

//    private void alwaysShow(SuperView view){
//        privateAlwaysShow(view);
//        lastShownFor = view;
//    }

    protected void privateAlwaysShow(SuperView view) {

    }

    private boolean shouldShow(SuperView view) {

        SharedPreferences settings = Algebrator.getAlgebrator().getSharedPreferences(PREFS_NAME, 0);
        boolean alreadyShown = settings.getBoolean(getSp_key(), false);
        if (!alreadyShown) {
            return privateShouldShow(view);
        }
        return false;
    }

    protected abstract boolean privateShouldShow(SuperView view);

    private void show(SuperView view) {
//        alwaysShow(view);
        privateShow(view);

        alreadyDone();
    }

    public void alreadyDone() {
        SharedPreferences settings = Algebrator.getAlgebrator().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(getSp_key(), true);

        editor.commit();
    }

    protected abstract void privateShow(SuperView view);

}

