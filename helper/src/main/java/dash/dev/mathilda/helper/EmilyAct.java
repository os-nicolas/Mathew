package dash.dev.mathilda.helper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.Main;
import cube.d.n.commoncore.eq.Pro.SineEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InputLine;
import dash.dev.mathilda.helper.tuts.TutActivity;


public class EmilyAct extends Activity {

    private static final String PREFS_NAME = "tuts";
    public static final String screenName = "EmilyAct";

    //public static boolean hasShown = false;

    private static WeakReference<EmilyAct> instance=new WeakReference<EmilyAct>(null);

    public static EmilyAct getInstance(){
        return instance.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance =new WeakReference<EmilyAct>(this);

        SharedPreferences settings = Mathilda.getApp().getSharedPreferences(PREFS_NAME, 0);
        boolean alreadyShown = settings.getBoolean("write", false);
        final Activity that = this;
        if (!alreadyShown) {
            Intent myIntent = new Intent(that, TutActivity.class);
            //hasShown = true;
            that.finish();
            that.startActivity(myIntent);
        }
        setContentView(Mathilda.getAndRemoveView(screenName,this));
    }

    @Override
    protected void onResume(){
        super.onResume();
       View myView = Mathilda.justGetView(screenName, this);
       if (myView instanceof  Main ){
            for (int i=0;i < ((Main) myView).getLinesSize();i++){


                EquationLine el = ((EquationLine)((Main) myView).getLine(i));

//                Equation write = new WritingEquation(el);
//                Equation eq = new SineEquation(el);
//                    Equation div = new DivEquation(el);
//                    div.add(NumConstEquation.create(1,el));
//                    div.add(NumConstEquation.create(2,el));
//                eq.add(div);
//                write.add(eq);
//                if (el instanceof InputLine) {
//                    write.add(((InputLine) el).getSelected());
//                }
//
//                Log.d("setting Eq:", write.toString());
//
//                el.stupid.set(write);
                Equation st = el.stupid.get();
                if (st != null) {
                    st.deepNeedsUpdate();
                }
            }

       }
        BaseApp.getApp().recordScreen("input");
    }

    @Override
    public void finish(){
        instance = null;
        super.finish();
    }
}
