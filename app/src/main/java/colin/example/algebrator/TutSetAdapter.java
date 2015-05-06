package colin.example.algebrator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import cube.d.n.commoncore.TutEnd;
import cube.d.n.commoncore.TutFrag;
import cube.d.n.commoncore.TutTextFrag;
import cube.d.n.commoncore.TutVideoFrag;

/**
 * Created by Colin_000 on 5/2/2015.
 */
public class TutSetAdapter extends FragmentPagerAdapter {

    public ArrayList<Fragment> frags = new ArrayList<>();
    public static int ID = 0;
    public final int id;

    public TutSetAdapter(FragmentManager fm) {
        super(fm);
        this.id = ID++;
        frags.add(TutTextFrag.make("Hi!", "Thanks for installing Mathilda", "1 of 5, swipe right"));
        frags.add(TutVideoFrag.make("Welcome", "To algebra without mistakes, busy-work and frustration", "android.resource://colin.example.algebrator/raw/" + R.raw.overview, "2 of 5"));
        frags.add(TutVideoFrag.make("Double Tap", "To add, subtract, multiple, divide and much much more", "android.resource://colin.example.algebrator/raw/" + R.raw.doubletap, "3 of 5"));
        frags.add(TutVideoFrag.make("Drag and Drop", "To move terms anywhere where they can legally be ", "android.resource://colin.example.algebrator/raw/" + R.raw.draganddrop, "4 of 5"));
        frags.add(TutTextFrag.make("It's that Easy", "swipe right to get started", "5 of 5"));
        frags.add(new TutEnd());
    }


//    protected Hashtable<Integer, WeakReference<Fragment>> fragmentReferences = new Hashtable<>();


    @Override
    public String toString() {
        return "" + id;
    }

    @Override
    public Fragment getItem(int i) {
//        fragmentReferences.put(i, new WeakReference<Fragment>(frags.get(i)));
        return frags.get(i);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        //if (!object.equals(map.get(map.get(position)))) {
            map.put(position, (Fragment) object);
            if (object instanceof TutFrag) {
                if (((TutFrag) object).getView() == null) {
                    ((TutFrag) object).startOnDraw();
                }
                  else {
                    ((TutFrag) object).start(((TutFrag) object).getView());
                }
            }

    }

    private HashMap<Integer, Fragment> map = new HashMap<>();

    public Fragment getFragment(int fragmentId) {
//        WeakReference<Fragment> ref = fragmentReferences.get(fragmentId);
//        return ref == null ? null : ref.get();
        return map.get(fragmentId);
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
