package cube.d.n.practice;

/**
 * Created by Colin_000 on 7/3/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import cube.d.n.commoncore.tuts.TutEnd;
import cube.d.n.commoncore.tuts.TutFrag;
import cube.d.n.commoncore.tuts.TutMainFrag;
import cube.d.n.commoncore.tuts.TutStart;
import cube.d.n.commoncore.tuts.TutVideoFrag;

/**
 * Created by Colin_000 on 5/2/2015.
 */
public class TutSetAdapter extends FragmentPagerAdapter {

    public ArrayList<Fragment> frags = new ArrayList<>();

    public TutSetAdapter(FragmentManager fm) {
        super(fm);
        frags.add(TutVideoFrag.make(
                "Double Tap",
                "Double Tap to add subtract multiply and much more",
                "android.resource://cube.d.n.practice/raw/" + R.raw.double_tap,
                "# of #").withBackgroundColor(0xff6C0304));
        frags.add(TutMainFrag.make("give it a try!", "*,5,(,/,(,+,10,9,-7,),6,)","10",false,false).withBackgroundColor(0xff6C0304));
        frags.add(TutVideoFrag.make(
                "Tap and Hold",
                "Tap and Hold to revert to a previous step",
                "android.resource://cube.d.n.practice/raw/" + R.raw.revert,
                "# of #").withBackgroundColor(0xff6C0304));
        frags.add(((TutMainFrag)TutMainFrag.make("give it a try!", "*,5,(,/,(,+,10,9,-7,),6,)","*,5,(,/,(,+,10,9,-7,),6,)",true,false).withBackgroundColor(0xff6C0304))
                .withStep("*,5,(,/,(,+,19,-7,),6,)")
                .withStep("*,5,(,/,12,6,)")
                .withStep("*,5,2")
                .withStep("10")
        );
        frags.add(TutVideoFrag.make(
                "Drag and Drop",
                "Drag and Drop to move terms around",
                "android.resource://cube.d.n.practice/raw/" + R.raw.drag,
                "# of #").withBackgroundColor(0xff6C0304));
        frags.add(TutMainFrag.make("give it a try!", "=,5,(,+,(,/,a,2,),3,)","=,4,a",true,false).withBackgroundColor(0xff6C0304));
        frags.add(new TutEnd().withBackgroundColor(0xff6C0304));
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
