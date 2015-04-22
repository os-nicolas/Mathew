package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.BinaryEquation;
import cube.d.n.commoncore.eq.Equation;
import cube.d.n.commoncore.eq.NumConstEquation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.WritingEquation;
import colin.example.algebrator.Actions.Action;
import colin.example.algebrator.EmilyView;

public class DeleteAction extends Action<EmilyView> {

    public String num;

    public DeleteAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public boolean canAct() {
        if (myView.selected instanceof PlaceholderEquation) {

            Equation l = myView.left();
            if (l != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void privateAct() {
        ((PlaceholderEquation) myView.selected).goDark();
        Equation l = myView.left();
        ((PlaceholderEquation) myView.selected).goDark();
        if (l.parent instanceof BinaryEquation) {
            l.parent.replace(l);
            // didn't we just check this? confusing i know but since the replace our l.parent has changed
            if (!(l.parent instanceof BinaryEquation)) {
                int pos = l.parent.indexOf(l);
                l.parent.add(pos + 1, myView.selected);
            } else {
                Equation write = new WritingEquation(myView);
                l.replace(write);
                write.add(l);
                write.add(myView.selected);
            }
        } else if (l instanceof NumConstEquation) {
            if (((NumConstEquation) l).getDisplaySimple().length() != 0) {
                String display = ((NumConstEquation) l).getDisplaySimple();
                String toSet = (String) display.subSequence(0, display.length() - 1);
                if (toSet.length() != 0 && toSet.charAt(0) == '-') {
                    toSet = toSet.substring(1, toSet.length());
                }
                ((NumConstEquation) l).setDisplay(toSet);
            }
            if (((NumConstEquation) l).getDisplaySimple().length() == 0) {
                l.remove();
            }
        } else {
            l.remove();
        }
    }

}
