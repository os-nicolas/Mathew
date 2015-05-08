package colin.example.algebrator;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;


/**
 * Created by Colin_000 on 5/2/2015.
 */
public class TutActivity extends FragmentActivity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    ViewPager mViewPager;

    public static final String PREFS_NAME = "tuts";

    public void onResume() {
        super.onResume();
        final TutSetAdapter adptr = (TutSetAdapter) mViewPager.getAdapter();
        final Activity that = this;
        if (mViewPager.getCurrentItem() == adptr.getCount() - 1) {
            Thread th = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        that.runOnUiThread(new Runnable() {
                            public void run() {
                                mViewPager.setCurrentItem(adptr.getCount() - 2, true);
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            th.start();


        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences settings = Algebrator.getAlgebrator().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("startup_tut", true);

        editor.commit();


        setContentView(R.layout.tut);


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.

        mViewPager = (ViewPager) findViewById(R.id.pager);

        if (mViewPager.getAdapter() == null) {
            TutSetAdapter adapter =
                    new TutSetAdapter(
                            getSupportFragmentManager());
            mViewPager.setAdapter(adapter);

            final Context that = this;

            mViewPager.setOnPageChangeListener(
                    new ViewPager.SimpleOnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            // When swiping between pages, select the
                            // corresponding tab.
                            TutSetAdapter adptr = (TutSetAdapter) mViewPager.getAdapter();

//                                Log.i("OnPageChangeListener", adptr+"");
//
//                                if ( adptr.getFragment(position) instanceof TutFrag){
//                                    ((TutFrag)adptr.getFragment(position)).start(adptr.getFragment(position).getView());
//                                }

                            if (position == adptr.getCount() - 1) {
                                //if we have reach the last tap
                                Intent myIntent = new Intent(that, WriteScreen.class);
                                that.startActivity(myIntent);
                            }

                            Log.i("tab selected", position + "");
                        }
                    });
        }

    }
}

