package cube.d.n.commoncore.eq.any;



import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.SelectedRow;
import cube.d.n.commoncore.SelectedRowButtons;
import cube.d.n.commoncore.SeletedRowEquationButton;
import cube.d.n.commoncore.eq.MyPoint;
import cube.d.n.commoncore.eq.Operations;
import cube.d.n.commoncore.lines.EquationLine;

import java.util.ArrayList;

/**
 * Created by Colin on 1/4/2015.
 */
public class MinusEquation extends MonaryEquation implements SignEquation{

    public MinusEquation(EquationLine owner2) {
        super(owner2);
        init();
    }

    @Override
    protected boolean willOperateOn(ArrayList<Equation> onsList) {
        return true;
    }

    @Override
    public ArrayList<SelectedRow> getSelectedRow() {


        ArrayList<SelectedRowButtons> buttons = new ArrayList<>();
        final Equation that = this;
        if (this.get(0) instanceof AddEquation){
            buttons.add(new SeletedRowEquationButton(negateAll(that.get(0).copy()),new Action(owner) {
                @Override
                protected void privateAct() {
                    MyPoint p = that.getNoneNullLastPoint(that.getX(),that.getY());
                    that.replace(negateAll(that.get(0).copy()));
                    changed(p);
                }
            }));
        }else if (this.get(0) instanceof MultiDivSuperEquation){
            final Equation yo = that.get(0).copy();
            yo.get(0).replace(yo.get(0).negate());

            buttons.add(new SeletedRowEquationButton(yo,new Action(owner) {
                @Override
                protected void privateAct() {
                    MyPoint p = that.getNoneNullLastPoint(that.getX(),that.getY());
                    that.replace(yo.copy());
                    changed(p);
                }
            }));
        }else if (get(0) instanceof MinusEquation){
            buttons.add(new SeletedRowEquationButton(that.get(0).get(0).copy(),new Action(owner) {
                @Override
                protected void privateAct() {
                    MyPoint p = that.getNoneNullLastPoint(that.getX(),that.getY());
                    that.replace(that.get(0).get(0).copy());
                    changed(p);
                }
            }));
        }else if (get(0) instanceof PlusMinusEquation){
            buttons.add(new SeletedRowEquationButton(that.get(0).copy(),new Action(owner) {
                @Override
                protected void privateAct() {
                    MyPoint p = that.getNoneNullLastPoint(that.getX(),that.getY());
                    that.replace(that.get(0).copy());
                    changed(p);
                }
            }));
        }else if (Operations.sortaNumber(get(0)) && Operations.getValue(get(0)).doubleValue() ==0){
            buttons.add(new SeletedRowEquationButton(NumConstEquation.create(0,owner),new Action(owner) {
                @Override
                protected void privateAct() {
                    MyPoint p = that.getNoneNullLastPoint(that.getX(),that.getY());
                    that.replace(NumConstEquation.create(0,owner));
                    changed(p);
                }
            }));
        }

        // we try to reduce too
        tryToReduce(buttons, that);

        if (buttons.size() != 0) {
            SelectedRow sr = new SelectedRow(1f / 9f);
            sr.addButtonsRow(buttons, 0, 1);
            ArrayList<SelectedRow> res = new ArrayList<SelectedRow>();
            res.add(sr);
            return res;
        }else{
            ArrayList<SelectedRow> startWith = super.getSelectedRow();
            return startWith;
        }

    }


    @Override
    public Equation negate() {
        return this.get(0).copy();
    }

    @Override
    public Equation plusMinus() {
        return this.get(0).plusMinus();
    }

    private void init() {
        display ="-";
    }

    public MinusEquation(EquationLine owner, MinusEquation minEq){
        super (owner,minEq);
        init();
    }

    @Override
    public Equation copy() {
        Equation result = new MinusEquation(this.owner,this);
        return result;
    }

    @Override
    public void tryOperator(ArrayList<Equation> eqs) {
        if (parent instanceof MinusEquation){
            parent.replace(get(0));
        }else
        if (get(0) instanceof MinusEquation){
            replace(get(0).get(0));
        }else if (get(0) instanceof PlusMinusEquation){
            replace(get(0));
        }else if (get(0) instanceof MultiDivSuperEquation){
            final Equation yo = get(0).copy();
            yo.get(0).replace(yo.get(0).negate());
            replace(yo);
        }else if (get(0) instanceof AddEquation){
            replace(negateAll(get(0).copy()));
        }else if (Operations.sortaNumber(get(0)) && Operations.getValue(get(0)).doubleValue() ==0){
            replace(NumConstEquation.create(0,owner));
        }
    }

    private Equation negateAll(Equation copy) {
        //if (get(0).size()>1){
            for (Equation e: copy){
                e.replace(e.negate());
            }
        return copy;
        //}
    }
}
