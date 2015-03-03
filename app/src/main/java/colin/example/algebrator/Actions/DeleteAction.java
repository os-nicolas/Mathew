package colin.example.algebrator.Actions;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.NumConstEquation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.example.algebrator.EmilyView;

public class DeleteAction extends Action {

    public String num;

    public DeleteAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation)emilyView.selected).goDark();
            Equation l = emilyView.left();
            if (l != null) {
                if (l.parent instanceof BinaryEquation) {
                    l.parent.replace(l);
                    // didn't we just check this? confusing i know but since the replace our l.parent has changed
                    if (!(l.parent instanceof  BinaryEquation)) {
                        int pos = l.parent.indexOf(l);
                        l.parent.add(pos + 1, emilyView.selected);
                    }else{
                        Equation write = new WritingEquation(emilyView);
                        l.replace(write);
                        write.add(l);
                        write.add(emilyView.selected);
                    }
                } else if (l instanceof NumConstEquation) {
                    if (((NumConstEquation) l).getDisplaySimple().length() != 0) {
                        String display =((NumConstEquation) l).getDisplaySimple();
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
        } else if (emilyView.selected != null) {
            // if they have a stack of stuff selected kill it all and replace it wiht a new Placeholder
            Equation newEq = new PlaceholderEquation(emilyView);
            if (emilyView.selected.parent == null) {
                Equation writeEq = new WritingEquation(emilyView);
                writeEq.add(newEq);
                newEq = writeEq;
            }
            emilyView.selected.replace(newEq);
            if (newEq instanceof PlaceholderEquation) {
                newEq.setSelected(true);
            } else if (newEq.get(0) instanceof PlaceholderEquation) {
                newEq.get(0).setSelected(true);
            }
        }
        //else if (emilyView.selected instanceof EqualsEquation){
        //	emilyView.selected.remove();
        //}
    }

}
