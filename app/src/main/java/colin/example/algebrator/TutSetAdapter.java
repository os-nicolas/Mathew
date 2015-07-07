package colin.example.algebrator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import cube.d.n.commoncore.tuts.TutEnd;
import cube.d.n.commoncore.tuts.TutFrag;
import cube.d.n.commoncore.tuts.TutTextFrag;
import cube.d.n.commoncore.tuts.TutVideoFrag;

/**
 * Created by Colin_000 on 5/2/2015.
 */
public class TutSetAdapter extends FragmentPagerAdapter {

    public ArrayList<Fragment> frags = new ArrayList<>();


    public TutSetAdapter(FragmentManager fm) {
        super(fm);
        frags.add(TutTextFrag.make(
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_1_title),
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_1_body),
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_1_at)).withExtaTimeOut(750));
        frags.add(TutVideoFrag.make(
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_2_title),
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_2_body),
                "android.resource://colin.example.algebrator/raw/" + R.raw.overview,
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_2_at)));
        frags.add(TutVideoFrag.make(
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_3_title),
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_3_body),
                "android.resource://colin.example.algebrator/raw/" + R.raw.doubletap,
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_3_at)));
        frags.add(TutVideoFrag.make(
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_4_title),
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_4_body),
                "android.resource://colin.example.algebrator/raw/" + R.raw.draganddrop,
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_4_at)));
        frags.add(TutTextFrag.make(
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_5_title),
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_5_body),
                Algebrator.getAlgebrator().getResources().getString(R.string.tut_5_at)));
        frags.add(new TutEnd());
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
