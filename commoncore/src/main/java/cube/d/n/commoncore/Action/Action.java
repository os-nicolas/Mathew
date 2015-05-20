package cube.d.n.commoncore.Action;

import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MonaryEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.lines.InputLine;
import cube.d.n.commoncore.lines.Line;

/**
 * Created by Colin_000 on 5/8/2015.
 */
public abstract class Action {

    public final Line owner;

    public Action(Line owner){
        this.owner =owner;
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

    public static int countEquals(Equation stupid) {
        int count = 0;
        for (Equation e : stupid) {
            if (e instanceof WritingLeafEquation && e.getDisplay(-1).equals("=")) {
                count++;
            }
        }
        return count;
    }

    protected static boolean canMoveRight(Equation current) {
        return canMove(false,current);
    }

    protected static boolean canMoveLeft(Equation current) {
        return canMove(true,current);
    }

    private static boolean canMove(boolean left,Equation current){
        return getMoveCurrent(left,current) instanceof BinaryEquation;
    }

    private static Equation getMoveCurrent(boolean left,Equation current) {
        while (current.parent != null && current.parent.indexOf(current) == (left?0:current.parent.size()-1)) {
            current = current.parent;
            if (current instanceof BinaryEquation) {
                return current;
            }
        }
        return null;
    }

    public static void tryMoveRight(Equation current) {
        tryMove(false ,current);
    }

    public static void tryMoveLeft(Equation current) {
        tryMove(true,current);
    }

    public static void  tryMove(boolean left,Equation selected) {
        Equation current = getMoveCurrent(left,selected);
        if (current != null){
            Equation oldEq =selected;
            oldEq.remove();
            int at = current.parent.indexOf(current);
            if (current.parent instanceof BinaryEquation) {
                Equation newEq = new WritingEquation((InputLine)current.owner);
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
            Equation next = (left? selected.left(): selected.right());
            if (next != null) {
                Equation oldEq = selected;

                while (next.size() != 0) {
                    next = next.get((left?next.size() - 1:0));
                }
                if (next.parent.equals(selected.parent)) {
                    int at = next.parent.indexOf(next);
                    oldEq.justRemove();
                    // at does not need to be adjusted since we remove the old equation
                    next.parent.add(at, oldEq);
                } else {
                    if (next.parent instanceof BinaryEquation || next instanceof MonaryEquation) {
                        Equation oldNext = next;
                        Equation newEq = new WritingEquation((InputLine)selected.owner);
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

    public void updateOffset(){
        if (owner instanceof  InputLine){
                InputLine il = (InputLine)owner;
                PlaceholderEquation phe = il.getSelected();
                if (phe.getX() +(phe.measureWidth()/2f) > owner.owner.width- 4*Line.getBuffer()){
                    il.toAddToOffsetX((owner.owner.width- 4*Line.getBuffer())- (phe.getX() +(phe.measureWidth()/2f)));
                }
                if (phe.getX() -(phe.measureWidth()/2f) < 4*Line.getBuffer()){
                    il.toAddToOffsetX((4*Line.getBuffer())- (phe.getX() - (phe.measureWidth()/2f)));
                }
                if (owner.owner.getOffsetY() + (il.stupid.get().measureHeight()/2f)> owner.owner.height - owner.owner.keyBoardManager.get().measureHeight()){
                    owner.owner.toAddToOffsetY((owner.owner.height - owner.owner.keyBoardManager.get().measureHeight())
                            - (owner.owner.getOffsetY() + (il.stupid.get().measureHeight()/2f)));
                }
                // this probably does not really work since getY stopping being update off screen
                // it also probably is not need and will never happen because we already scroll if get the last line offscreen
                if (phe.getY() -phe.measureHeightUpper() < 2*Line.getBuffer()){
                    owner.owner.toAddToOffsetY((2*Line.getBuffer())- (phe.getY() - phe.measureHeightUpper()));
                }

        }
        }

}
