package cube.d.n.commoncore.Action;

import cube.d.n.commoncore.ErrorReporter;
import cube.d.n.commoncore.eq.PlaceholderEquation;
import cube.d.n.commoncore.eq.any.BinaryEquation;
import cube.d.n.commoncore.eq.any.DivEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.LeafEquation;
import cube.d.n.commoncore.eq.any.MonaryEquation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.lines.AlgebraLine;
import cube.d.n.commoncore.lines.EquationLine;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin_000 on 5/8/2015.
 */
public abstract class Action extends SuperAction {

    public final EquationLine owner;

    public Action(EquationLine owner){
        this.owner =owner;
    }



    public void act(){
        ErrorReporter.log("tried to act " + name(), owner.stupid.get().toString());
        super.act();
    }

    protected String name(){
        return this.getClass().getName();
    }

    protected void setNewStupid(EqualsEquation myStup) {
        myStup.fixIntegrety();
        owner.stupid.set(myStup);
        ((AlgebraLine)owner).getSelected().setSelected(false);
        ((AlgebraLine)owner).changed();
        ((AlgebraLine)owner).updatePopUpButtons();
        ((AlgebraLine)owner).updateHistory();
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

    protected static boolean canMoveRight(Equation current) {
        return canMove(false,current);
    }

    protected static boolean canMoveLeft(Equation current) {
        return canMove(true,current);
    }

    private static boolean canMove(boolean left,Equation selected){
        // this seems kinda simple
        // you go up util you can go over
        Equation last = selected;
        Equation current = selected.parent;
        if ((left&&current.indexOf(last)>0) || (!left && current.indexOf(last) < current.size()-1)){
        }else{
            do {
                if (current == null){
                    return false;
                }
                last = current;
                current = current.parent;
            }while (!(current instanceof WritingEquation || (current instanceof BinaryEquation &&
                    (
                            (left && current.indexOf(last)==1) ||
                                    (!left && current.indexOf(last)==0)
                    ))));
        }
        // then you go over
        if (current instanceof WritingEquation && !current.contains(selected)){
            return true;
        }else {
            int at = current.indexOf(last);
            at = (left ? at - 1 : at + 1);

            if (at ==-1 || at == current.size()){
                return false;
            }
            return true;
        }



        // we go up
//        Equation last = selected;
//        Equation current = selected.parent;
//        while (true){
//            if (current == null){
//                // we can't move left
//                return false;
//            }
//            if ((left&&current.indexOf(last)==0) || (!left && current.indexOf(last) == current.size()-1)){
//                last = current;
//                current = current.parent;
//            }else{
//                break;
//            }
//        }
//        // we check and see if we can go over
//        int at =  current.indexOf(last);
//        if (left){
//            return at != 0f;
//        }else{
//            return at != current.size()-1;
//        }
    }

//    private static Equation getMoveCurrent(boolean left,Equation current) {
//        while (current.parent != null && current.parent.indexOf(current) == (left?0:current.parent.size()-1)) {
//            current = current.parent;
//            if (current instanceof BinaryEquation) {
//                return current;
//            }
//        }
//        return null;
//    }

    public static void tryMoveRight(Equation current) {
        tryMove(false ,current);
    }

    public static void tryMoveLeft(Equation current) {
        tryMove(true,current);
    }

    public static void  tryMove(boolean left,Equation selected) {
        // this seems kinda simple
        // you go up util you can go over
        Equation last = selected;
        Equation current = selected.parent;
        if ((left&&current.indexOf(last)>0) || (!left && current.indexOf(last) < current.size()-1)){
        }else{
             do {
                if (current == null){
                    return;
                }
                last = current;
                current = current.parent;
            }while (!(current instanceof WritingEquation || (current instanceof BinaryEquation &&
                    (
                            (left && current.indexOf(last)==1) ||
                            (!left && current.indexOf(last)==0)
                    ))));
        }
        // then you go over
        if (current instanceof WritingEquation && !current.contains(selected)){
            int at = current.indexOf(last);
            at = (left ? at : at + 1);

            selected.remove();
            current.add(at,selected);
        }else {

            int at = current.indexOf(last);
            at = (left ? at - 1 : at + 1);

            // then you go down util you hit a writing equation
//            if (at == current.size()) {
//                selected.remove();
//                current.add(selected);
//            } else {
                Equation target = current.get(at);
                if (target instanceof LeafEquation) {
                    selected.remove();
                    current.add(at, selected);
                } else {
                    if (left) {
                        while (!(target instanceof WritingEquation)) {
                            target = target.get(target.size() - 1);
                        }
                        selected.remove();
                        target.add(selected);
                    } else {
                        while (!(target instanceof WritingEquation)) {
                            target = target.get(0);
                        }
                        selected.remove();
                        target.add(0, selected);
                    }
                }
//            }
        }
    }

    public void updateOffset() {
        if (owner instanceof InputLine) {
            InputLine il = (InputLine) owner;
           il.updateOffset();
        }
    }

}
