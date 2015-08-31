package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.Pro.TrigEquation;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.InputLine;

public class DeleteAction extends Action {

    public String num;

    public DeleteAction(InputLine owner) {
        super(owner);
    }

    @Override
    public boolean canAct() {

        Equation l = ((InputLine) owner).left();
        if (l != null) {
            return true;
        }
        if (l==null &&((InputLine) owner).getSelected().parent != null) {
            if (((InputLine) owner).getSelected().parent.parent instanceof TrigEquation) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void privateAct() {
        //TODO

        ((InputLine) owner).getSelected().goDark();
        Equation l = ((InputLine) owner).left();
        // this first case is pretty unique
        // its Sin(|) you hit delete
        // it's "( Sin( "(|) ) )
        Equation selected  =((InputLine) owner).getSelected();

        if (selected.parent != null && (selected.parent).indexOf(selected)== 0 && selected.parent.parent instanceof TrigEquation) {
            selected.parent.parent.replace(selected);
        } else {
            if (l.parent instanceof BinaryEquation) {
                //keep the left kid kill the right
                BinaryEquation be = (BinaryEquation) (l.parent);
                WritingEquation superHolder = (WritingEquation) (l.parent.parent);
                int index = superHolder.indexOf(be);
                ((Equation) be).remove();
                ((InputLine) owner).getSelected().justRemove();
                superHolder.add(index, ((InputLine) owner).getSelected());
                WritingEquation leftSide = (WritingEquation) (((Equation) be).get(0));
                for (int i = leftSide.size() - 1; i >= 0; i--) {
                    superHolder.add(index, leftSide.get(i));
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
            } else if (l == null && ((InputLine) owner).getSelected().parent != null) {
                WritingEquation holder = (WritingEquation) (((InputLine) owner).getSelected().parent);
                if (holder.parent instanceof TrigEquation) {
                    TrigEquation trigEq = (TrigEquation) (holder.parent);
                    WritingEquation superHolder = (WritingEquation) (trigEq.parent);
                    int index = superHolder.indexOf(trigEq);
                    trigEq.remove();
                    for (int i = holder.size() - 1; i >= 0; i--) {
                        superHolder.add(index, holder.get(i));
                    }
                }
            } else {
                l.remove();
            }
            updateOffset();
        }
    }

}
