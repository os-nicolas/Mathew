package colin.example.algebrator.Actions;

import colin.algebrator.eq.BinaryEquation;
import colin.algebrator.eq.Equation;
import colin.algebrator.eq.PlaceholderEquation;
import colin.algebrator.eq.WritingEquation;
import colin.algebrator.eq.WritingLeafEquation;
import colin.algebrator.eq.WritingPraEquation;
import colin.example.algebrator.EmilyView;

public class ParenthesesAction extends Action {

    private boolean left;

    public ParenthesesAction(EmilyView emilyView, boolean left) {
        super(emilyView);
        this.left = left;
    }

    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            ((PlaceholderEquation)emilyView.selected).goDark();

            Equation l = emilyView.left();
            Equation newEq = new WritingPraEquation(left, emilyView);
            if (l != null) {
                if (left) {
                    if (!(l.parent instanceof BinaryEquation)) {
                        emilyView.insert(newEq);
                    } else {
                        Equation oldEq = emilyView.selected;
                        Equation holder = new WritingEquation(emilyView);
                        oldEq.replace(holder);
                        holder.add(newEq);
                        holder.add(oldEq);
                        oldEq.setSelected(true);
                    }
                } else {
                    boolean can = true;
                    boolean op = false;
                    if (l instanceof WritingLeafEquation) {
                        op = ((WritingLeafEquation) l).isOpLeft();
                    }
                    can &= !op;
//                    if (!hasMatch()) {
//                        can = false;
//                    }

                    if (can) {

                        if (!(l.parent instanceof BinaryEquation)) {
                            emilyView.insert(newEq);
                        } else {
                            Equation oldEq = emilyView.selected;
                            Equation holder = new WritingEquation(emilyView);
                            oldEq.replace(holder);
                            holder.add(newEq);
                            holder.add(oldEq);
                            oldEq.setSelected(true);
                        }


                        // if we are right things are a little more complex
                        // we have to find our other half
//                        Equation leftAt = emilyView.left();
//                        while ((leftAt != null) && !(leftAt instanceof WritingPraEquation && ((WritingPraEquation) leftAt).left)) {
//                            leftAt = leftAt.left();
//                        }
//                        if (leftAt != null) {
//                            Equation rightAt = emilyView.selected;
//                            while (rightAt != null && !(rightAt.parent.equals(leftAt.parent))) {
//                                rightAt = rightAt.right();
//                            }
//                            if (rightAt == null) {
//                                // we add at the right end of leftAt.parent
//                                emilyView.insertAt(leftAt.parent, leftAt.parent.size(), newEq);
//                            } else {
//                                // we add left of rightAt
//                                emilyView.insertAt(leftAt.parent, rightAt.parent.indexOf(rightAt), newEq);
//                            }
//                        }
                    }
                }
            } else {
                if (left) {
                    emilyView.insert(newEq);
                }
            }

        } else {
            //if (left) {
            //    Equation numEq = new WritingPraEquation(left, emilyView);
            //    addToBlock(numEq);
            //}
        }
    }


}
