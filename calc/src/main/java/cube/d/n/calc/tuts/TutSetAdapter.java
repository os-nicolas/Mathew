package cube.d.n.calc.tuts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import cube.d.n.calc.Mathilda;
import cube.d.n.calc.R;
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
//        frags.add(new TutStart());
        frags.add(TutTextFrag.make(
                Mathilda.getApp().getResources().getString(R.string.tut_1_title),
                Mathilda.getApp().getResources().getString(R.string.tut_1_body),
                Mathilda.getApp().getResources().getString(R.string.tut_1_atl)).withExtaTimeOut(650));
        frags.add(TutVideoFrag.make(
                Mathilda.getApp().getResources().getString(R.string.tut_2_title),
                Mathilda.getApp().getResources().getString(R.string.tut_2_body),
                "android.resource://cube.d.n.calc/raw/" + R.raw.overview,
                Mathilda.getApp().getResources().getString(R.string.tut_2_atl)));
        frags.add(TutVideoFrag.make(
                Mathilda.getApp().getResources().getString(R.string.tut_3_title),
                Mathilda.getApp().getResources().getString(R.string.tut_3_body),
                "android.resource://cube.d.n.calc/raw/" + R.raw.doubletap,
                Mathilda.getApp().getResources().getString(R.string.tut_3_atl)));
        frags.add(TutVideoFrag.make(
                Mathilda.getApp().getResources().getString(R.string.tut_4_title),
                Mathilda.getApp().getResources().getString(R.string.tut_4_body),
                "android.resource://cube.d.n.calc/raw/" + R.raw.draganddrop,
                Mathilda.getApp().getResources().getString(R.string.tut_4_atl)));
        frags.add(TutVideoFrag.make(
                Mathilda.getApp().getResources().getString(R.string.tut_6_title),
                Mathilda.getApp().getResources().getString(R.string.tut_6_body),
                "android.resource://cube.d.n.calc/raw/" + R.raw.calc,
                Mathilda.getApp().getResources().getString(R.string.tut_6_atl)));
        frags.add(TutTextFrag.make(
                Mathilda.getApp().getResources().getString(R.string.tut_5_title),
                Mathilda.getApp().getResources().getString(R.string.tut_5_body),
                Mathilda.getApp().getResources().getString(R.string.tut_5_atl)));
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
