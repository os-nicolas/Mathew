package cube.d.n.commoncore.eq.any;

import android.util.Log;

import java.util.ArrayList;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.GS;
import cube.d.n.commoncore.SelectedRow;
import cube.d.n.commoncore.SelectedRowButtons;
import cube.d.n.commoncore.SeletedRowEquationButton;
import cube.d.n.commoncore.Util;
import cube.d.n.commoncore.eq.BinaryOperator;
import cube.d.n.commoncore.eq.FlexOperation;
import cube.d.n.commoncore.eq.MultiCountData;
import cube.d.n.commoncore.eq.MyPoint;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.lines.EquationLine;

public class AddEquation extends FlexOperation implements BinaryOperator {

    @Override
    public void integrityCheck() {
        if (size() < 2) {
            Log.e("ic", "this should be at least size 2");
        }
    }

    public boolean canFlatten(Equation a, Equation b) {
        Equation lcc = a.lowestCommonContainer(b);
        return (lcc.addContain(a) && lcc.addContain(b));
    }

    public void flatten(Equation a, Equation b) {
        Equation lcc = a.lowestCommonContainer(b);
        // we need to work our way up from a to lcc
        // and on each step pull everthing from the level we are at up a level
        for (Equation x : new Equation[]{a, b}) {
            Equation at = x;
            while (!at.equals(lcc)) {
                if (at instanceof AddEquation) {
                    Equation oldEq = at.parent;
                    int loc = oldEq.indexOf(at);
                    at.justRemove();
                    for (Equation e : at) {
                        oldEq.add(loc++, e);
                    }
                }
                at = at.parent;
            }
        }
    }

    public AddEquation(EquationLine owner, AddEquation addEq) {
        super(owner, addEq);
        init();
        this.display = addEq.getDisplay(-1);

    }

    @Override
    public Equation copy() {
        Equation result = new AddEquation(this.owner, this);
        return result;
    }

    public String getDisplay(int pos) {
        if (pos == -1) {
            return display;
        }
        Equation at = get(pos);
        while (at.size() > 1) {
            at = at.get(0);
        }
        if (at instanceof MonaryEquation && !((MonaryEquation) at).drawSign()) {
            if (at instanceof MinusEquation) {
                return at.getDisplay();
            }
            if (at instanceof PlusMinusEquation) {
                return "\u00B1";
            }
        }
        return display;
    }

    public AddEquation(EquationLine owner) {
        super(owner);
        init();
    }

    private void init() {
        display = "+";
    }

    // addition is a little more spread out
    @Override
    protected float myWidthAdd() {
        return (float) (8 * super.myWidthAdd());
    }

    public void tryOperator(ArrayList<Equation> eqs) {
        //TODO handle inbeddedness
        Equation a = eqs.get(0);
        Equation b = eqs.get(1);
        int at = Math.min(indexOf(a), indexOf(b));
        if (indexOf(a) > indexOf(b)) {
            Equation temp = a;
            a = b;
            b = temp;
        }

        operateRemove(eqs);

        Equation result = Operations.Add(new MultiCountData(a), new MultiCountData(b), owner);
        handleResult(at, result);
    }

    @Override
    public SelectedRow getSelectedRow() {
        ArrayList<SelectedRowButtons> buttons = new ArrayList<>();
        final AddEquation that = this;
        if (this.size() == 2) {
            final Equation a = get(0);
            final Equation b = get(1);
            final ArrayList<Equation> eqs = new ArrayList<>();
            eqs.add(a);
            eqs.add(b);



            final MultiCountData left = new MultiCountData(a);
            final MultiCountData right = new MultiCountData(b);

            if (Operations.add_canAddNumber(left, right, owner)) {
                buttons.add(new SeletedRowEquationButton(Operations.add_AddNumber(new MultiCountData(left),new MultiCountData(right), owner), new Action(owner) {
                    @Override
                    protected void privateAct() {
                        MyPoint p = that.getNoneNullLastPoint(that.getX(),that.getY());
                        operateRemove(eqs);
                        Equation result = Operations.add_AddNumber(left, right, owner);
                        handleResult(0, result);
                        changed(p);
                    }
                }));
            }

            if (Operations.add_canCommonDenom(left, right, owner)) {
                buttons.add(new SeletedRowEquationButton(Operations.add_CommonDenom(new MultiCountData(left),new MultiCountData(right), owner), new Action(owner) {
                    @Override
                    protected void privateAct() {
                        MyPoint p = that.getNoneNullLastPoint(that.getX(),that.getY());
                        operateRemove(eqs);
                        Equation result = Operations.add_CommonDenom(left, right, owner);
                        handleResult(0, result);

                        changed(p);
                    }
                }));
            }


            if (Operations.add_canCommon(left, right, owner) && !Operations.add_canAddNumber(left, right, owner)) {
                buttons.add(new SeletedRowEquationButton(Operations.add_Common(new MultiCountData(left),new MultiCountData(right), owner), new Action(owner) {
                    @Override
                    protected void privateAct() {
                        MyPoint p = that.getNoneNullLastPoint(that.getX(),that.getY());
                        operateRemove(eqs);
                        Equation result = Operations.add_Common(left, right, owner);
                        handleResult(0, result);

                        changed(p);
                    }
                }));
            }
       }

        // we try to reduce too
        tryToReduce(buttons, that);


        if (buttons.size() != 0) {
            SelectedRow sr = new SelectedRow(1f / 9f);
            sr.addButtonsRow(buttons, 0, 1);
            return sr;
        } else {
            return null;
        }
    }

    private void handleResult(int at, Equation result) {
        if (result instanceof AddEquation) {
            int i = 0;
            for (Equation e : result) {
                if ((this.size() == 0 && result.indexOf(e) == result.size() - 1) || !Operations.sortaNumber(result) || Operations.getValue(result).doubleValue() != 0) {
                    add(at + i, e);
                    i++;
                }
            }
        } else if (this.size() == 0 || !Operations.sortaNumber(result) || Operations.getValue(result).doubleValue() != 0) {
            add(at, result);
        }
        if (this.size() == 1) {
            // if this is a zero
            if (Operations.sortaNumber(this.get(0)) && Operations.getValue(this.get(0)).doubleValue() == 0 && this.parent instanceof AddEquation) {
                this.remove();
            } else {
                this.replace(this.get(0));
            }
        }
    }

    @Override
    public void tryOperator(int i) {
        if (i != size() - 1) {
            ArrayList<Equation> toOp = new ArrayList<Equation>();
            toOp.add(get(i));
            toOp.add(get(i + 1));
            tryOperator(toOp);
        }
    }

    public void smartAdd(Equation equation) {
        if (equation instanceof  AddEquation){
            for (Equation eq: equation){
                add(eq);
            }
        }else{
            add(equation);
        }
    }
}
