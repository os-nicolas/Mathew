package colin.example.algebrator;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;



/**
 * Created by Colin_000 on 5/2/2015.
 */
public class TutActivity extends FragmentActivity {
        // When requested, this adapter returns a DemoObjectFragment,
        // representing an object in the collection.
        TutSetAdapter adapter;
        ViewPager mViewPager;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.tut);

            // ViewPager and its adapters use support library
            // fragments, so use getSupportFragmentManager.
            adapter =
                    new TutSetAdapter(
                            getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(adapter);

            mViewPager.setOnPageChangeListener(
                    new ViewPager.SimpleOnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            // When swiping between pages, select the
                            // corresponding tab.
                            adapter.getItem(position);
                        }
                    });
        }

}
