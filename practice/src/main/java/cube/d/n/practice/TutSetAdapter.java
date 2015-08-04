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
                "Tutorial",
                "Swipe right to get started",
                "1 of 12")
                .withBackgroundColor(0xff6C0304));
        // ##### DOUBLE TAP
        frags.add(TutVideoFrag.make(
                "Double Tap",
                "Double Tap to add subtract multiply and much more",
                "android.resource://cube.d.n.practice/raw/" + R.raw.double_tap,
                "2 of 12").withBackgroundColor(0xff6C0304));
        frags.add(TutMainFrag.make("Give it a Try!",
                "Double tap to evaluate the expression below.",
                "*,5,(,/,(,+,10,9,-7,),6,)",
                "10",
                "3 of 12")
                .withRevert(false)
                .withPopup(false)
                .withDrag(false)
                .withBackgroundColor(0xff6C0304)
        );
        // ##### REVERT
        frags.add(TutVideoFrag.make(
                "Tap and Hold",
                "Tap and Hold to revert to a previous step",
                "android.resource://cube.d.n.practice/raw/" + R.raw.revert,
                "4 of 12").withBackgroundColor(0xff6C0304));
        frags.add(((TutMainFrag)TutMainFrag.make(
                        "Give it a Try!",
                        "Tap and hold on the first line to revert to it",
                        "*,5,(,/,(,+,10,9,-7,),6,)",
                        "*,5,(,/,(,+,10,9,-7,),6,)",
                        "5 of 12")
                        .withPopup(false)
                        .withDrag(false)
                        .withBackgroundColor(0xff6C0304))
                .withStep("*,5,(,/,(,+,19,-7,),6,)")
                .withStep("*,5,(,/,12,6,)")
                .withStep("*,5,2")
                .withStep("10")
        );
        // ##### DRAG AND DROP
        frags.add(TutVideoFrag.make(
                "Drag and Drop",
                "Drag and Drop to move terms around",
                "android.resource://cube.d.n.practice/raw/" + R.raw.drag,
                "6 of 12").withBackgroundColor(0xff6C0304));
        frags.add(TutMainFrag.make("Give it a try!",
                "Solve the equation below by dragging terms around",
                "=,5,(,+,(,/,a,2,),3,)",
                "=,4,a",
                "7 of 12")
                .withPopup(false)
                .withBackgroundColor(0xff6C0304));

        // ##### BOTH SIDES
        frags.add(TutVideoFrag.make(
                "Modify Both Sides",
                "Modify Both Sides using the buttons on the bottom of the screen",
                "android.resource://cube.d.n.practice/raw/" + R.raw.both_sides,
                "8 of 12").withBackgroundColor(0xff6C0304));
        frags.add(TutMainFrag.make(
                "Give it a Try!",
                "Solve the equation below by using the buttons at the bottom to modify both sides",
                "=,5,(,+,(,/,a,2,),3,)",
                "=,4,a",
                "9 of 12")
                //.withDrag(false)
                .withPopup(false)
                .withKeyboard(true)
                .withBackgroundColor(0xff6C0304));

        // ##### POP UPS
        frags.add(TutVideoFrag.make(
                "Pop up Buttons",
                "",
                "android.resource://cube.d.n.practice/raw/" + R.raw.pop_up,
                "10 of 12").withBackgroundColor(0xff6C0304));
        frags.add(TutMainFrag.make("Give it a Try!",
                "Use the buttons that pop up at the bottom of the screen to evaluate the expression below",
                "*,5,(,/,(,+,10,9,-7,),6,)",
                "10",
                "11 of 12")
                .withDouble(false)
                .withKeyboard(true)
                .withBackgroundColor(0xff6C0304));

        frags.add(TutTextFrag.make(
                "That's It.",
                "Swipe right to return to the main menu",
                "12 of 12")
                .withBackgroundColor(0xff6C0304));
        frags.add(new TutEnd()
                .withBackgroundColor(0xff6C0304));
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
