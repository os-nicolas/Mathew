package cube.d.n.commoncore.eq.any;


import android.util.Log;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.SelectedRow;
import cube.d.n.commoncore.SelectedRowButtons;
import cube.d.n.commoncore.SeletedRowEquationButton;
import cube.d.n.commoncore.eq.BinaryOperator;
import cube.d.n.commoncore.eq.FlexOperation;
import cube.d.n.commoncore.eq.MultiCountDatas;
import cube.d.n.commoncore.eq.MyPoint;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.eq.write.WritingEquation;
import cube.d.n.commoncore.lines.EquationLine;

import java.util.ArrayList;

public class MultiEquation extends FlexOperation implements MultiDivSuperEquation, BinaryOperator {

    public MultiEquation(EquationLine owner, MultiEquation equations) {
        super(owner, equations);
        init();
    }

    @Override
    public Equation negate() {
        Equation result = this.copy();
        result.set(0,result.get(0).negate());
        return result;
    }

    @Override
    public Equation plusMinus() {
        Equation result = this.copy();
        result.set(0,result.get(0).plusMinus());
        return result;
    }

    @Override
    public void integrityCheck() {
        super.integrityCheck();
        if (size() < 2) {
            Log.e("ic", "this should be at least size 2");
        }
    }

    @Override
    public Equation copy() {
        Equation result = new MultiEquation(this.owner,this);
        return result;
    }

    /**
     * returns true is the given equation could be "on top" if this MultiEquation was written as A/B
     *
     * @param equation - a child of this
     * @return true if the child would be on top false if the child would be on bottom or if the equation is not a child
     */
    @Override
    public boolean onTop(Equation equation) {
        boolean currentTop = true;
        Equation current = equation;
        String debug = current.hashCode() + ", ";
        while (true) {

            if (current.equals(this)) {
                return currentTop;
            }
            if (current.parent instanceof DivEquation) {
                currentTop = ((DivEquation) current.parent).onTop(current) == currentTop;
            }
            current = current.parent;
            if (current == null) {
                Log.i("bad", debug + " this is: " + this.hashCode());
            }
            debug += current.hashCode() + ",";
        }
    }

    public MultiEquation(EquationLine owner) {
        super(owner);
        init();
    }

    private void init() {
        char[] timesUnicode = { '\u00D7'};
        display = new String(timesUnicode);
    }

    @Override
    public void tryOperator(int i) {

        if (i!= size()-1) {
            ArrayList<Equation> toOp = new ArrayList<Equation>();
            toOp.add(get(i));
            toOp.add(get(i + 1));
            tryOperator(toOp);
        }
    }

    public void tryOperator(ArrayList<Equation> eqs) {


        int at = Math.min(indexOf(eqs.get(0)), indexOf(eqs.get(1)));
        operateRemove(eqs);
        Equation result = getMutiplyEquation(eqs.get(0),eqs.get(1));


        Equation noChangeResult = new MultiEquation(owner);
        noChangeResult.addAll(eqs);

        if (result.same(noChangeResult)){
            result = noChangeResult;
        }

        if (result instanceof MultiEquation){
            addAll(at,result);
        }else {
            add(at, result);
            if (this.size() == 1) {
                this.replace(this.get(0));
            }
        }
    }

    private Equation getMutiplyEquation(Equation a, Equation b) {



        Equation result = null;

        ArrayList<Equation> eqs = new ArrayList<>();
        eqs.add(a);
        eqs.add(b);


        // for the bottom and the top
        // find all the equations on each side


        MultiCountDatas left= new MultiCountDatas(eqs.get(0));

        MultiCountDatas right= new MultiCountDatas(eqs.get(1));

        boolean simplify = eqs.get(0).removeSign() instanceof AddEquation && eqs.get(1).removeSign() instanceof AddEquation;
        // multiply && combine like terms
        MultiCountDatas fullSet = Operations.Multiply(left, right, simplify, owner);


        result = fullSet.getEquation(owner);
        return result;
    }


    @Override
    public SelectedRow getSelectedRow() {


        ArrayList<SelectedRowButtons> buttons = new ArrayList<>();

        final MultiEquation that = this;
        if (this.size() == 2) {
            final Equation a = get(0);
            final Equation b = get(1);
            final ArrayList<Equation> eqs = new ArrayList<>();
            eqs.add(a);
            eqs.add(b);


            if (multi_canMulti(eqs)) {
                Equation temp = getMutiplyEquation(a.copy(), b.copy());
                buttons.add(new SeletedRowEquationButton(temp, new Action(owner) {
                    @Override
                    protected void privateAct() {
                        MyPoint p = that.getNoneNullLastPoint(that.getX(),that.getY());
                        that.tryOperator(eqs);
                        changed(p);
                    }
                }));
            }
        }


        if (buttons.size() != 0) {
            SelectedRow sr = new SelectedRow(1f / 9f);
            sr.addButtonsRow(buttons, 0, 1);
            return sr;
        } else {
            return null;
        }
    }

    private boolean multi_canMulti(ArrayList<Equation> eqs) {

        Equation result = null;

        // for the bottom and the top
        // find all the equations on each side

        MultiCountDatas left= new MultiCountDatas(eqs.get(0).copy());

        MultiCountDatas right= new MultiCountDatas(eqs.get(1).copy());

        boolean simplify = eqs.get(0).copy().removeSign() instanceof AddEquation && eqs.get(1).copy().removeSign() instanceof AddEquation;

        MultiCountDatas fullSet =  Operations.Multiply(left, right, simplify,owner);

        result = fullSet.getEquation(owner);

        Equation noChangeResult = new MultiEquation(owner);
        for (Equation e: eqs){
            noChangeResult.add(e.copy());
        }

        return !result.same(noChangeResult);
    }

    public boolean hasSign(int i) {
        Equation left = get(i);
        while (left instanceof MultiEquation){
            left = left.get(left.size()-1);
        }

//        if (left.parenthesis()){
//            return false;
//        }
        Equation right =get(i+1);// left.right();
        while (right instanceof MultiEquation){
            right = right.get(0);
        }
//        if (right.parenthesis()){
//            return false;
//        }
        if ((left instanceof NumConstEquation || left instanceof MonaryEquation) && (right instanceof NumConstEquation || right instanceof MonaryEquation)){
            return true;
        }
        if (right instanceof MonaryEquation){
            return true;
        }
        if (left instanceof PowerEquation && Operations.sortaNumber(left.get(0))){
            return true;
        }
        if (right instanceof PowerEquation && Operations.sortaNumber(right.get(0))){
            return true;
        }
        if (right instanceof PlusMinusEquation){
            return true;
        }
        // this matter in bothsides screen
        if (right instanceof WritingEquation){
            return true;
        }

        return false;
    }
}

