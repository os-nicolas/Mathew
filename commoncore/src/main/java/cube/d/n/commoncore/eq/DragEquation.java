package cube.d.n.commoncore.eq;


import android.util.Log;

import java.util.ArrayList;

import cube.d.n.commoncore.eq.Pro.TrigEquation;
import cube.d.n.commoncore.eq.any.AddEquation;
import cube.d.n.commoncore.eq.any.EqualsEquation;
import cube.d.n.commoncore.eq.any.Equation;
import cube.d.n.commoncore.eq.any.MinusEquation;
import cube.d.n.commoncore.eq.any.MultiDivSuperEquation;
import cube.d.n.commoncore.eq.any.PowerEquation;

public class DragEquation {
    // this is the on we draw
    public Equation eq;
    // this is the one in the equation
    public Equation demo;
    public ArrayList<Equation.Op> ops = new ArrayList<Equation.Op>();
    //public Equation oldDemo;

    // used to tell if the equation has moved
    Equation startParent = null;
    int startIndex = -1;

    public DragEquation(Equation eq) {
        super();
        this.eq = eq.copy();
        // is this a good idea?
        this.eq.parent = null;
        //this.oldDemo = eq;
        this.demo = eq;

        updateOps(eq);
    }

    public void updateOps(Equation equation) {
        ops = new ArrayList<Equation.Op>();
        while (equation.parent instanceof MinusEquation) {
            equation = equation.parent;
        }
        if (equation.parent instanceof MultiDivSuperEquation || equation.parent instanceof EqualsEquation){
            ops.add(Equation.Op.MULTI);
            ops.add(Equation.Op.DIV);
        }
        if (equation.parent instanceof AddEquation || equation.parent instanceof EqualsEquation){
            ops.add(Equation.Op.ADD);
        }
        if (equation.parent instanceof PowerEquation && equation.parent.indexOf(equation) == 1){
            ops.add(Equation.Op.POWER);
        }
        if (equation instanceof TrigEquation && equation.parent instanceof EqualsEquation){
            ops.add(Equation.Op.FUNCTION);
        }
        if (ops.size()==0){
            Log.i("this seems bad", "");
        }
    }
}