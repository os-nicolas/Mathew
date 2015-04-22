package colin.example.algebrator.Actions;

import cube.d.n.commoncore.eq.BinaryEquation;
import cube.d.n.commoncore.eq.Equation;
import cube.d.n.commoncore.eq.MonaryEquation;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.WritingEquation;
import cube.d.n.commoncore.eq.WritingLeafEquation;
import cube.d.n.commoncore.eq.WritingPraEquation;
import colin.example.algebrator.SuperView;

/**
 * Created by Colin_000 on 3/25/2015.
 */

public abstract class Action<myView extends SuperView> {
    public myView myView;

    public Action(myView myView) {
        this.myView = myView;
    }

    public boolean canAct(){
        return true;
    }

    public void act(){
        if (canAct()){
            privateAct();
        }
    }

    protected abstract void privateAct();

    protected boolean hasMatch() {
        int depth = 1;
        Equation current = myView.selected;
        current = current.left();
        while (true) {
            if (current != null) {
                if (current instanceof WritingPraEquation) {
                    if (!((WritingPraEquation) current).left) {
                        depth++;
                    } else {
                        depth--;
                        if (depth == 0) {
                            return true;
                        }
                    }
                }
                current = current.left();
            } else {
                return false;
            }
        }

    }


    protected void addToBlock(Equation numEq) {
        PlaceholderEquation phe = new PlaceholderEquation(myView);
        if (myView.selected.parent instanceof WritingEquation) {
            // add to the parent
            int at = myView.selected.parent.indexOf(myView.selected);
            myView.selected.parent.add(at + 1, numEq);
            myView.selected.parent.add(at + 2, phe);
            phe.setSelected(true);
        } else if (myView.selected instanceof WritingEquation) {
            // add to what is selected
            myView.selected.add(numEq);
            myView.selected.add(phe);
            phe.setSelected(true);
        } else {
            // replace selected with a new WritingEqution that contains selects
            Equation write = new WritingEquation(myView);
            Equation oldEq = myView.selected;
            myView.selected.replace(write);
            write.add(oldEq);
            write.add(numEq);
            write.add(phe);
            phe.setSelected(true);
        }
    }

    protected void tryMoveRight() {
        tryMove(false);
    }

    public void tryMoveLeft() {
        tryMove(true);
    }

    void tryMove(boolean left) {
        Equation current = getMoveCurrent(left);
        if (current != null){
            Equation oldEq = myView.selected;
            oldEq.remove();
            int at = current.parent.indexOf(current);
            if (current.parent instanceof BinaryEquation) {
                Equation newEq = new WritingEquation(current.owner);
                current.replace(newEq);
                if (left){
                    newEq.add(oldEq);
                    newEq.add(current);
                }else{
                    newEq.add(current);
                    newEq.add(oldEq);
                }
            }else{
                current.parent.add(at +(left?0:1), oldEq);
            }
        }else {
            Equation next = (left? myView.selected.left(): myView.selected.right());
            if (next != null) {
                Equation oldEq = myView.selected;

                while (next.size() != 0) {
                    next = next.get((left?next.size() - 1:0));
                }
                if (next.parent.equals(myView.selected.parent)) {
                    int at = next.parent.indexOf(next);
                    oldEq.justRemove();
                    // at does not need to be adjusted since we remove the old equation
                    next.parent.add(at, oldEq);
                } else {
                    if (next.parent instanceof BinaryEquation || next instanceof MonaryEquation) {
                        Equation oldNext = next;
                        Equation newEq = new WritingEquation(myView);
                        next.replace(newEq);
                        if (left){
                            newEq.add(oldNext);
                            oldEq.remove();
                            newEq.add(oldEq);
                        }else{
                            oldEq.remove();
                            newEq.add(oldEq);
                            newEq.add(oldNext);

                        }
                    } else {
                        int at = next.parent.indexOf(next) + (left?1:0);
                        oldEq.remove();
                        next.parent.add(at, oldEq);
                    }
                }
            }
        }
    }


    private Equation getMoveCurrent(boolean left) {
        Equation current = myView.selected;
        while (current.parent != null && current.parent.indexOf(current) == (left?0:current.parent.size()-1)) {
            current = current.parent;
            if (current instanceof BinaryEquation) {
                return current;
            }
        }
        return null;
    }

    protected boolean canMoveRight() {
        return canMove(false);
    }

    protected boolean canMoveLeft() {
        return canMove(true);
    }

    private boolean canMove(boolean left){
        return getMoveCurrent(left) instanceof BinaryEquation;
    }

    public static int countEquals(Equation stupid) {
        int count = 0;
        for (Equation e : stupid) {
            if (e instanceof WritingLeafEquation && e.getDisplay(-1).equals("=")) {
                count++;
            }
        }
        return count;
    }

}