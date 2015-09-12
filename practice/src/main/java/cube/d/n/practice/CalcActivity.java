package cube.d.n.practice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import cube.d.n.commoncore.BaseApp;

/**
 * Created by Colin_000 on 9/11/2015.
 */
public class CalcActivity extends FullAct {

        private static final String PREFS_NAME = "tuts";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(Mathilda.getView("calc",this));
        }

        @Override
        public void onResume(){
            super.onResume();
            BaseApp.getApp().recordScreen("calc");
        }
}

