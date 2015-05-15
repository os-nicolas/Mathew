package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.NumConstEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.InputLine;

public class NumberAction extends Action {

    public String num;

    public NumberAction(InputLine emilyView, String num) {
        super(emilyView);
        this.num = num;
    }

    @Override
    public boolean canAct(){
        return true;
    }

    @Override
    protected void privateAct() {
            ((InputLine)owner).getSelected().goDark();

            Equation l = ((InputLine)owner).left();
            Equation r = ((InputLine)owner).getSelected().right();
            if (l != null) {
                if (!(l.parent instanceof BinaryEquation)) {
                    if ((l instanceof NumConstEquation) && (l.parent.equals(((InputLine)owner).getSelected().parent))) {
                        if (l instanceof NumConstEquation && !l.getDisplay(-1).equals("0")) {
                            l.setDisplay(((NumConstEquation) l).getDisplaySimple() + num);
                        } else if (l instanceof NumConstEquation && l.getDisplay(-1).equals("0")) {
                            l.setDisplay(num);
                        }
                    } else if (((InputLine)owner).getSelected().parent instanceof BinaryEquation) {
                        Equation oldEq = ((InputLine)owner).getSelected();
                        Equation holder = new WritingEquation(((InputLine)owner));
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), ((InputLine)owner));
                        oldEq.replace(holder);
                        holder.add(newEq);
                        holder.add(oldEq);
                        oldEq.setSelected(true);
                    } else {
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), ((InputLine)owner));
                        ((InputLine)owner).insert(newEq);
                    }
                } else {
                    if ((r instanceof NumConstEquation) && (((InputLine)owner).getSelected().parent.equals(r.parent)) && !(((InputLine)owner).getSelected().parent instanceof BinaryEquation)) {
                        if (!r.getDisplay(-1).equals("0")) {
                            r.setDisplay(num + ((NumConstEquation) r).getDisplaySimple());
                        } else {
                            r.setDisplay(num);
                        }
                    } else if (((InputLine)owner).getSelected().parent instanceof BinaryEquation) {
                        Equation oldEq = ((InputLine)owner).getSelected();
                        Equation holder = new WritingEquation(((InputLine)owner));
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), ((InputLine)owner));
                        oldEq.replace(holder);
                        holder.add(newEq);
                        holder.add(oldEq);
                        oldEq.setSelected(true);
                    } else {
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), ((InputLine)owner));
                        ((InputLine)owner).insert(newEq);
                    }
                }
            } else {
                if (((InputLine)owner).getSelected().parent instanceof BinaryEquation) {
                    Equation oldEq = ((InputLine)owner).getSelected();
                    Equation holder = new WritingEquation(((InputLine)owner));
                    Equation newEq = new NumConstEquation(Integer.parseInt(num), ((InputLine)owner));
                    oldEq.replace(holder);
                    holder.add(newEq);
                    holder.add(oldEq);
                    oldEq.setSelected(true);
                } else if (r instanceof NumConstEquation) {
                    if (!r.getDisplay(-1).equals("0")) {
                        r.setDisplay(num + ((NumConstEquation) r).getDisplaySimple());
                    } else {
                        r.setDisplay(num);
                    }
                } else {
                    Equation newEq = new NumConstEquation(Integer.parseInt(num), ((InputLine)owner));
                    ((InputLine)owner).insert(newEq);
                }
            }
    }

}
