package colin.example.algebrator.Actions.WriteScreen;

import cube.d.n.commoncore.eq.BinaryEquation;
import cube.d.n.commoncore.eq.Equation;
import cube.d.n.commoncore.eq.NumConstEquation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.WritingEquation;
import colin.example.algebrator.EmilyView;
import colin.example.algebrator.Actions.Action;

public class NumberAction extends Action<EmilyView> {

    public String num;

    public NumberAction(EmilyView emilyView, String num) {
        super(emilyView);
        this.num = num;
    }

    @Override
    public boolean canAct(){
        return myView.selected instanceof PlaceholderEquation;
    }

    @Override
    protected void privateAct() {
            ((PlaceholderEquation) myView.selected).goDark();

            Equation l = myView.left();
            Equation r = myView.selected.right();
            if (l != null) {
                if (!(l.parent instanceof BinaryEquation)) {
                    if ((l instanceof NumConstEquation) && (l.parent.equals(myView.selected.parent))) {
                        if (l instanceof NumConstEquation && !l.getDisplay(-1).equals("0")) {
                            l.setDisplay(((NumConstEquation) l).getDisplaySimple() + num);
                        } else if (l instanceof NumConstEquation && l.getDisplay(-1).equals("0")) {
                            l.setDisplay(num);
                        }
                    } else if (myView.selected.parent instanceof BinaryEquation) {
                        Equation oldEq = myView.selected;
                        Equation holder = new WritingEquation(myView);
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), myView);
                        oldEq.replace(holder);
                        holder.add(newEq);
                        holder.add(oldEq);
                        oldEq.setSelected(true);
                    } else {
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), myView);
                        myView.insert(newEq);
                    }
                } else {
                    if ((r instanceof NumConstEquation) && (myView.selected.parent.equals(r.parent)) && !(myView.selected.parent instanceof BinaryEquation)) {
                        if (!r.getDisplay(-1).equals("0")) {
                            r.setDisplay(num + ((NumConstEquation) r).getDisplaySimple());
                        } else {
                            r.setDisplay(num);
                        }
                    } else if (myView.selected.parent instanceof BinaryEquation) {
                        Equation oldEq = myView.selected;
                        Equation holder = new WritingEquation(myView);
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), myView);
                        oldEq.replace(holder);
                        holder.add(newEq);
                        holder.add(oldEq);
                        oldEq.setSelected(true);
                    } else {
                        Equation newEq = new NumConstEquation(Integer.parseInt(num), myView);
                        myView.insert(newEq);
                    }
                }
            } else {
                if (myView.selected.parent instanceof BinaryEquation) {
                    Equation oldEq = myView.selected;
                    Equation holder = new WritingEquation(myView);
                    Equation newEq = new NumConstEquation(Integer.parseInt(num), myView);
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
                    Equation newEq = new NumConstEquation(Integer.parseInt(num), myView);
                    myView.insert(newEq);
                }
            }
    }

}
