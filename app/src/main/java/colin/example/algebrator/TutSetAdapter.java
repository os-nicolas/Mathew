package colin.example.algebrator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import cube.d.n.commoncore.TutFrag;

/**
 * Created by Colin_000 on 5/2/2015.
 */
public class TutSetAdapter extends FragmentPagerAdapter {
    ArrayList<TutFrag> frags = new ArrayList<>();

    public TutSetAdapter(FragmentManager fm) {
        super(fm);
        frags.add(TutFrag.make("Hi","this is a test","android.resource://com.example.algebrator/raw/widthfixed.mp4"));
        frags.add(TutFrag.make("2","this is also test","android.resource://com.example.algebrator/raw/widthfixed.mp4"));
    }

    @Override
    public Fragment getItem(int i) {
        return frags.get(i);
    }

    @Override
    public int getCount() {
        return frags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
