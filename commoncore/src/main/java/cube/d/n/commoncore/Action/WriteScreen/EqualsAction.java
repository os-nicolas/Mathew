package cube.d.n.commoncore.Action.WriteScreen;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.eq.write.WritingLeafEquation;
import cube.d.n.commoncore.eq.write.WritingPraEquation;
import cube.d.n.commoncore.lines.InputLine;

/**
 * Created by Colin on 1/7/2015.
 */
public class EqualsAction extends Action {
    public EqualsAction(InputLine owner) {
        super(owner);
    }

    @Override
    public boolean canAct() {

            Equation l = ((InputLine)owner).left();
            // we can't add it if there is nothing to the left && nothing we can pull from the last line
            boolean can = true;
            if (l== null){
                //if ( owner.owner.getLast() == null) {
                    can = false;
                //}
            }

            // we can't add if the last char was an op
            if (l instanceof WritingLeafEquation) {
                can = can && !((WritingLeafEquation) l).isOpLeft();
            }
            // if the root equation only hold one
            can = can && countEquals(((InputLine)owner).stupid.get()) == 0;

            // we can't add if we have (|
            can = can && !(l instanceof WritingPraEquation && ((WritingPraEquation) l).left);
            // we can't add if we are not adding to the rootWriteEquation
            if (l !=  null && can) {
                can = l.parent.parent == null;
                // but we can move out
                if (((InputLine)owner).getSelected().right() == null) {
                    can = true;
                }
            }
            return can;


    }

    @Override
    protected void privateAct() {
        ((InputLine)owner).getSelected().goDark();
        Equation l = ((InputLine)owner).left();
        Equation newEq = new WritingLeafEquation("=", (InputLine) owner);
//        if (l== null){
//            Equation oldEq =owner.owner.getLast();
//
//            if (oldEq instanceof WritingEquation) {
//                for (Equation e:oldEq) {
//                    ((InputLine) owner).insert(e);
//                }
//            }else{
//                ((InputLine) owner).insert(oldEq);
//            }
//            ((InputLine) owner).insert(newEq);
//        }else {

            if (((InputLine) owner).getSelected().right() == null) {
                while (canMoveRight(((InputLine) owner).getSelected())) {
                    tryMoveRight(((InputLine) owner).getSelected());
                }
            }
            ((InputLine) owner).insert(newEq);
//        }
        updateOffset();
    }
}


