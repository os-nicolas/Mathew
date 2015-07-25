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
    public SelectedRow getSelectedRow() {
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
        }

        if (buttons.size() != 0){
            SelectedRow sr = new SelectedRow(1f/9f);
            sr.addButtonsRow(buttons,0,1);
            return sr;
        }else{
            return null;
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
        }else if (get(0) instanceof AddEquation){
            replace(negateAll(get(0).copy()));
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
