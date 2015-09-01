package cube.d.n.commoncore;

import android.graphics.Canvas;
import android.graphics.Paint;

import cube.d.n.commoncore.Action.Action;
import cube.d.n.commoncore.eq.any.Equation;

/**
 * Created by Colin_000 on 8/28/2015.
 */
public class PopUpEquationButton extends  PopUpButton {
    public Equation myEq;

    public PopUpEquationButton(Equation eq, Action myAction) {
        super("", myAction);
        myEq = eq.copy();
        myEq.parent = null;
    }

    public void draw(Canvas canvas, Paint p){
        drawEqButton(this,canvas, p,myEq);
    }
}
